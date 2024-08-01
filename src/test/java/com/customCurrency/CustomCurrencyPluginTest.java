package com.customCurrency;

import com.customCurrency.CustomCurrencyPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CustomCurrencyPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomCurrencyPlugin.class);
		RuneLite.main(args);
	}
}