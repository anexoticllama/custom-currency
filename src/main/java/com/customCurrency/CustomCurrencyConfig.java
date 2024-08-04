package com.customCurrency;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(CustomCurrencyConfig.Group)
public interface CustomCurrencyConfig extends Config
{
	String Group = "Custom Currency Tracker";

	@ConfigItem(
		keyName = "currencyLabel",
		name = "Currency Name",
		description = "The name of your custom currency.",
		position = 0
	)
	default String currencyLabel()
	{
		return "Coins";
	}

	@ConfigItem(
		keyName = "currencySpent",
		name = "Currency Spent",
		description = "Total amount of your currency that has been spent",
		position = 3
	)
	default int currencySpent()
	{
		return 0;
	}
	
	@ConfigItem(
		keyName = "currencyIcon",
		name = "Currency Icon",
		description = "Customizable icon for your currency",
		position = 1
	)
	default CurrencyType currencyType()
	{
		return CurrencyType.BLOOD_MONEY;
	}
	
	@ConfigItem(
		keyName = "infopanelConfig",
		name = "Infopanel Overlay",
		description = "Enable or disable the infopanel overlay",
		position = 2
	)
	default boolean infopanelConfig()
	{
		return true;
	}
	
	@ConfigItem(
		keyName = "currencyReward",
		name = "Currency Reward Selection",
		description = "What game action rewards you with the custom currency"
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

	@ConfigItem(
		keyName = "currencyDivisor",
		name = "Currency Divisor",
		description = "Currency payout will be divided by this factor. Currency spent will remain 1:1"
	)
	default int currencyDivisor()
	{
		return 1;
	}

	@ConfigSection(
		name = "Collection Log",
		description = "Earn coins through Collection Log slots",
		closedByDefault = true,
		position = 1
	)
	String cLogSection = "cLogSection";

	@ConfigItem(
		keyName = "cLogTiers",
		name = "Collection Log Brackets",
		description = "Collection Log Brackets separated by commas, e.g. four tiers can be entered as '100, 100, 100, 100' -- rewards for 1-100, 101-200, etc. will match tier rewards below",
		section = cLogSection,
		position = 1
	)
	default String cLogTiers(){
		return "0";
	}

	@ConfigItem(
		keyName = "cLogTieredPayouts",
		name = "Collection Log Rewards",
		description = "Collection Log Rewards based on Tiers configured above. e.g. '1, 2, 3, 4' for the four tiers example provided above. Number of fields must match.",
		section = cLogSection,
		position = 2
	)
	default String cLogTieredPayouts(){
		return "0";
	}
}
