package com.customCurrency;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CustomCurrencyConfig.Group)
public interface CustomCurrencyConfig extends Config
{
	String Group = "Custom Currency Tracker";

	@ConfigItem(
		keyName = "currencyLabel",
		name = "Currency Name",
		description = "The name of your custom currency."
	)
	default String currencyLabel()
	{
		return "Coins";
	}

	@ConfigItem(
		keyName = "currencyBalance",
		name = "Currency Name",
		description = "Current balance of your currency"
	)
	default int currencyBalance()
	{
		return 0;
	}
	
	@ConfigItem(
		keyName = "currencyIcon",
		name = "Currency Icon",
		description = "Customizable icon for your currency"
	)
	default CurrencyType currencyType()
	{
		return CurrencyType.BLOOD_MONEY;
	}
	
	@ConfigItem(
		keyName = "infopanelConfig",
		name = "Infopanel Config",
		description = "Enable or disable the infopanel overlay"
	)
	default boolean infopanelConfig()
	{
		return true;
	}
	
	@ConfigItem(
		keyName = "currencyReward",
		name = "Currency Reward Selection",
		description = "The name of your custom currency."
	)
	default CurrencyReward currencyReward()
	{
		return CurrencyReward.COLLECTION_LOG_SLOTS;
	}
	
	@ConfigItem(
		keyName = "currencyPayout",
		name = "Currency Payout Default",
		description = "Amount of currency payout per reward trigger"
	)
	default int currencyPayout()
	{
		return 1;
	}
}
