package com.bxzmod.someusefulthings.server;

import com.bxzmod.someusefulthings.ModLoadFlag;
import com.bxzmod.someusefulthings.SpecialCrops;
import com.bxzmod.someusefulthings.StackSizeFix;
import com.bxzmod.someusefulthings.TicketManager;
import com.bxzmod.someusefulthings.achievement.AchievementLoader;
import com.bxzmod.someusefulthings.blocks.BlockLoader;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.command.CommandLoader;
import com.bxzmod.someusefulthings.config.ConfigLoader;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.entity.EntityLoader;
import com.bxzmod.someusefulthings.events.EventLoader;
import com.bxzmod.someusefulthings.fakeplayer.FakePlayerLoader;
import com.bxzmod.someusefulthings.fluid.FluidLoader;
import com.bxzmod.someusefulthings.fluid.FluidLoaderHelper;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.oredictionary.OreDictionaryLoader;
import com.bxzmod.someusefulthings.recipes.CraftingLoader;
import com.bxzmod.someusefulthings.throwable.ThrowableLoader;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import com.bxzmod.someusefulthings.tileentity.TileEntityLoader;
import net.minecraftforge.fml.common.event.*;

import java.io.File;
import java.io.IOException;

public class Common
{
	public void preInit(FMLPreInitializationEvent event)
	{
		new ModLoadFlag();
		new ConfigLoader(event);
		SpecialCrops.configFile = new File(event.getModConfigurationDirectory(), "bxz_crops_config.json");
		new CapabilityLoader(event);
		new CreativeTabsLoader(event);
		new FluidLoader(event);
		new FluidLoaderHelper();
		new ItemLoader(event);
		new BlockLoader(event);
		new OreDictionaryLoader(event);
		new TileEntityLoader(event);
		new EntityLoader(event);
		new ThrowableLoader(event);
		new NetworkLoader(event);

	}

	public void init(FMLInitializationEvent event)
	{
		new CraftingLoader(event);
		new AchievementLoader(event);
		new EventLoader(event);
		new GuiLoader(event);
		new FakePlayerLoader(event);

	}

	public void postInit(FMLPostInitializationEvent event) throws IOException
	{
		if (!SpecialCrops.configFile.exists())
		{
			SpecialCrops.init();
			SpecialCrops.saveData();
		} else
			SpecialCrops.loadData();
	}

	public void loadComplete(FMLLoadCompleteEvent event)
	{
		new StackSizeFix();
	}

	public void serverStarting(FMLServerStartingEvent event)
	{
		new CommandLoader(event);
		// TicketManager.saveAndLoad();

	}

	public void serverStarted(FMLServerStartedEvent event) throws IOException
	{
		TicketManager.init();
		MobSummonTileEntity.testEntityList();
	}

	public void serverStopping(FMLServerStoppingEvent event) throws IOException
	{
		TicketManager.saveData();
		TicketManager.clear();
		SpecialCrops.saveData();
	}
}
