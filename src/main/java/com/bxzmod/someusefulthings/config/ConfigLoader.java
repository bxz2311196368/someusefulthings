package com.bxzmod.someusefulthings.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigLoader
{
	private static Configuration config;

	private static final Logger LOGGER = LogManager.getLogger();

	public static boolean invincible_ring;
	public static boolean artifact_sword;
	public static boolean limitless_tool;
	public static boolean redstone_apple;
	public static boolean portable_inventory_item;
	public static boolean copy_enchantment;
	public static boolean reinforcement_machine;
	public static boolean remove_enchantment;
	public static boolean bucket_wwwwater;
	public static boolean command_easyset;
	public static boolean need_op;

	public ConfigLoader(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		load();
	}

	public static void load()
	{
		LOGGER.info("Started loading config. ");
		String comment[] = new String[20];
		int id = 0;

		comment[id] = "Enable invincible ring?(default=true)";
		invincible_ring = config.get(Configuration.CATEGORY_GENERAL, "invincible ring", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable artifact sword?(default=true)";
		artifact_sword = config.get(Configuration.CATEGORY_GENERAL, "artifact sword", true, comment[id++]).getBoolean();

		comment[id] = "Enable multitool?(default=true)";
		limitless_tool = config.get(Configuration.CATEGORY_GENERAL, "multitool", true, comment[id++]).getBoolean();

		comment[id] = "Enable redstone apple?(default=true)";
		redstone_apple = config.get(Configuration.CATEGORY_GENERAL, "redstone apple", true, comment[id++]).getBoolean();

		comment[id] = "Enable backpack?(default=true)";
		portable_inventory_item = config.get(Configuration.CATEGORY_GENERAL, "backpack", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable copy enchantment?(default=true)";
		copy_enchantment = config.get(Configuration.CATEGORY_GENERAL, "copy enchantment", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable reinforcement machine?(default=true)";
		reinforcement_machine = config.get(Configuration.CATEGORY_GENERAL, "reinforcement machine", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable remove enchantment?(default=true)";
		remove_enchantment = config.get(Configuration.CATEGORY_GENERAL, "remove enchantment", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable wwwwater?(default=true)";
		bucket_wwwwater = config.get(Configuration.CATEGORY_GENERAL, "wwwwater", true, comment[id++]).getBoolean();

		comment[id] = "Enable command easyset?(default=true)";
		command_easyset = config.get(Configuration.CATEGORY_GENERAL, "command easyset", true, comment[id++])
				.getBoolean();

		comment[id] = "Enable command easyset op required?(default=false)";
		need_op = config.get(Configuration.CATEGORY_GENERAL, "need op", false, comment[id++]).getBoolean();

		config.save();
		LOGGER.info("Finished loading config. ");
	}

}
