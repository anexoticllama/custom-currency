package com.customCurrency;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	@Override
	protected void startUp() throws Exception
	{
		//log.info("CustomCurrencyPlugin started!");
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
}
