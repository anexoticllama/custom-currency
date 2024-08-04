package com.customCurrency;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;
import java.io.File;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.ScriptID;
import net.runelite.api.Skill;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.Text;
import static net.runelite.api.widgets.WidgetID.QUEST_COMPLETED_GROUP_ID;

@Slf4j
@PluginDescriptor(
	name = "Llama's Custom Currency"
)
public class CustomCurrencyPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CustomCurrencyConfig config;

	@Inject
	private InfoBoxManager ibMgr;

	public Accountant acc;

	@Inject
	private ItemManager itemMgr;

	//Collection Log

	public boolean messageUpdates;


	@Override
	protected void startUp() throws Exception
	{
		//log.info("CustomCurrencyPlugin started!");
		createInfoPanel();
		updatePlugin();
	}

	@Override
	protected void shutDown() throws Exception
	{
		//log.info("CustomCurrencyPlugin stopped!");
		ibMgr.removeInfoBox(acc);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged c){
		if(c.getGroup().equals(config.Group)){
			updatePlugin();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gs)
	{
		switch(gs.getGameState()){
			case LOGGING_IN:
				//Do something? Not sure if needed
				updatePlugin();
				break;
			case HOPPING:
				updatePlugin();
				//log.info(String.valueOf(client.getVarpValue(2943))); //Current Log Slots
				//log.info(String.valueOf(client.getVarpValue(2944))); //Total Log Slots (ignoring dupes)
				break;
			default:
				break;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage m){
		String msg = m.getMessage();
		if(messageUpdates){
			if(msg.startsWith("New item added to your collection log:")){
				updateInfobox();
			} //add else if for Clues, CAs, Diaries?
		}
	}

	@Provides
	CustomCurrencyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomCurrencyConfig.class);
	}

	public void updatePlugin(){
		updateInfobox();
		checkMessageUpdate();
	}

	private int getItemID(CurrencyType c){
		switch(c){
			case COINS:
				return 995;
    		case BLOOD_MONEY:
				return 13307;
    		case PLATINUM_TOKEN:
				return 13204;
    		case TOKKUL:
				return 6529;
    		case WARRIOR_GUILD_TOKEN:
				return 8851;
    		case GOLDEN_NUGGET:
				return 12012;
    		case MARK_OF_GRACE:
				return 11849;
    		case HALLOWED_MARK:
				return 11849;
    		case PIECES_OF_EIGHT:
				return 8951;
    		case COLLECTION_LOG:
				return 22711;
			default:
				return 995;
		}
	}

	private BufferedImage getCoinIcon(){
		return itemMgr.getImage(getItemID(config.currencyType()), calculateTotal(), false);
	}

	private void createInfoPanel(){
		if(config.infopanelConfig()){
			final BufferedImage img = getCoinIcon();
			int balance = trackBalance();
			acc = new Accountant(this, balance, config.currencyLabel(), img);
			ibMgr.addInfoBox(acc);
		}
	}

	private void updateInfobox(){
		if (acc != null){
			ibMgr.removeInfoBox(acc);
		}
		if (config.infopanelConfig()){
			createInfoPanel();
		}
	}

	private int calculateTotal(){
		int tot = 0;

		switch(config.currencyReward()){
			case COLLECTION_LOG_SLOTS:
				if(validateConfig()){
					tot = calcTieredTotals(config.cLogTiers(), config.cLogTieredPayouts(), getLogSlots());
					tot = (int) Math.floor(tot / config.currencyDivisor());
					log.info("Validated Config. CLog total set to:"+tot);
				} else {
					tot = getLogSlots() * config.currencyPayout();
					tot = (int) Math.floor(tot / config.currencyDivisor());
				}
				break;
			case COMBAT_ACHIEVEMENTS:
				int[] caRewards = getCombatAchievmentRewards();
				int[] ca = getCombatAchievments();

				int caTiers = 6; //Tiers of Achviements / Tasks

				for (int i = 0; i < caTiers; i++){
					tot+= caRewards[i] * ca[i];
				}

				break;
			case AREA_ACHIEVEMENTS:
				int[] diaryRewards = getAreaAchievementRewards();
				int[] diary = getAreaAchievements();

				int areaTiers = 4; //Tiers of Achviements / Tasks

				for (int i = 0; i < areaTiers; i++){
					tot+= diaryRewards[i] * diary[i];
				}
				break;
			case CLUES:
				getClues();
				break;
			case XP:
				getXP();
				break;
			default:
				break;
		}

		return tot;
	}

	private int trackBalance(){
		int newBal = 0;
		newBal = calculateTotal() - config.currencySpent();
		return newBal;
	}

	private int getLogSlots(){
		return client.getVarpValue(2943);
	}

	private int[] getCombatAchievments(){
		int[] CombatAchievements = new int[6];
		return CombatAchievements;
	}

	private int[] getCombatAchievmentRewards(){
		int[] caRewards = new int[6];
		return caRewards;
	}

	private int[] getAreaAchievements(){
		int[] AreaAchievements = new int[4];
		return AreaAchievements;
	}

	private int[] getAreaAchievementRewards(){
		int[] taskRewards = new int[4];
		return taskRewards;
	}

	private int[] getClues(){
		int[] clueCompletes = new int[6];
		return clueCompletes;
	}

	private int getXP(){
		return 0;
	}

	private boolean checkMessageUpdate(){
		messageUpdates = false;
		switch(config.currencyReward()){
			case COLLECTION_LOG_SLOTS:
				messageUpdates = true;
				break;
			case COMBAT_ACHIEVEMENTS:
				messageUpdates = true;
				break;
			case AREA_ACHIEVEMENTS:
				messageUpdates = true;
				break;
			case CLUES:
				messageUpdates = true;
				break;
			case XP:
				messageUpdates = false;
				break;
			default:
				messageUpdates = false;
				break;
		}

		return messageUpdates;
	}

	private boolean validateConfig(){
		boolean v = false;

		long tierCount = config.cLogTiers().chars().filter(ch -> ch == ',').count();
		if(tierCount == config.cLogTieredPayouts().chars().filter(ch -> ch == ',').count()){
			v = true;
		}

		return v;
	}

	private int[] parseConfigTiers(String tiers){
		if (tiers.indexOf(',') == -1){
			log.info("Error with parseConfigTiers index");
			return new int[]{1};
		}
		log.info("Tiers input:"+tiers);
		String[] s = tiers.split(",");
		int[] configTiers = new int[s.length];
		configTiers = Arrays.stream(tiers.split(",")).mapToInt(Integer::parseInt).toArray();

		/*for(int x = 0; x < s.length; x++){
			log.info(s[x]);
			log.info(Integer.toString(configTiers[x]));
		}*/

		return configTiers;
	}

	private int calcTieredTotals(String tiers, String tieredPayouts, int currentStat){
		int totalCurrency = 0;
		int[] parsedTiers = parseConfigTiers(tiers);
		int[] parsedPayouts = parseConfigTiers(tieredPayouts);
				
		for(int x = 0; x < parsedTiers.length; x++){
			if(parsedTiers[x] > currentStat){
				log.info("Break 1. Total now equals:"+totalCurrency);
				break;
			}

			if(x+1 == parsedTiers.length){
				totalCurrency += (currentStat-parsedTiers[x]+1) * parsedPayouts[x];
				log.info("Break 2. Total now equals:"+totalCurrency);
				break;
			}
			
			if(parsedTiers[x+1] <= currentStat){
				totalCurrency+= (parsedTiers[x+1]-parsedTiers[x]) * parsedPayouts[x];
				log.info("Total now equals:"+totalCurrency);	
			} else {
				totalCurrency += (currentStat-parsedTiers[x]+1) * parsedPayouts[x];
				log.info("Total now equals:"+totalCurrency);
				break;
			}
		}
		log.info("Total Currency = "+totalCurrency);
		return totalCurrency;
	}

}
