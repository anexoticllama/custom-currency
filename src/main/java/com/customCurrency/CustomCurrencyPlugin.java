package com.customCurrency;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
	private boolean notificationRead;
	public int logSlots;


	@Override
	protected void startUp() throws Exception
	{
		//log.info("CustomCurrencyPlugin started!");
		createInfoPanel();

		//init CurrencyReward Options to set balance
		//logSlots = getLogSlots();
	}

	@Override
	protected void shutDown() throws Exception
	{
		//log.info("CustomCurrencyPlugin stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//Do Nothing
		}
	}

	@Provides
	CustomCurrencyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomCurrencyConfig.class);
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
		return itemMgr.getImage(getItemID(config.currencyType()), config.currencyBalance() * 100, false);
	}

	private void createInfoPanel(){
		if(config.infopanelConfig()){
			final BufferedImage img = getCoinIcon();
			acc = new Accountant(this, config.currencyBalance(), config.currencyLabel(), img);
			ibMgr.addInfoBox(acc);
		}
	}
}
