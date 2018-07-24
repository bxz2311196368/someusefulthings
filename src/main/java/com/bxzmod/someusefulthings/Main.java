package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.server.Common;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;

@Mod(modid = ModInfo.MODID, name = ModInfo.MODNAME, dependencies = ModInfo.dependencies, version = ModInfo.VERSION, acceptedMinecraftVersions = ModInfo.acceptedMinecraftVersions)
public class Main
{
	@SidedProxy(clientSide = "com.bxzmod.someusefulthings.client.Client", serverSide = "com.bxzmod.someusefulthings.server.Common")
	public static Common proxy;

	@Instance(ModInfo.MODID)
	public static Main instance;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws IOException
	{
		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		proxy.loadComplete(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		proxy.serverStarting(event);
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) throws IOException
	{
		proxy.serverStarted(event);
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) throws IOException
	{
		proxy.serverStopping(event);
	}
}
