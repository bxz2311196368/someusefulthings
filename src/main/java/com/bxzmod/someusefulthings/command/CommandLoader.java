package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.config.ConfigLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandLoader
{

	public CommandLoader(FMLServerStartingEvent event)
	{
		if (ConfigLoader.command_easyset)
			event.registerServerCommand(new Easyset());

		event.registerServerCommand(new SpecialCropsSetting());
		if (Loader.isModLoaded("harvestfestival"))
		{
			event.registerServerCommand(new HFtrade());
			event.registerServerCommand(new HFComeHere());
		}
		//TODO here remove
		event.registerServerCommand(new BXZGuiTest());
		//this.regTest(event);
	}

	private void regTest(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new TestCommand());
		event.registerServerCommand(new BXZDebug());
	}

}
