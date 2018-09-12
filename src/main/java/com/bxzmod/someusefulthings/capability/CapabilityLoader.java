package com.bxzmod.someusefulthings.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Callable;

public class CapabilityLoader
{
	@CapabilityInject(IPortableInventory.class)
	public static Capability<IPortableInventory> PORTABLE_INVENTORY = null;

	@CapabilityInject(IGarbagBag.class)
	public static Capability<IGarbagBag> GARBAGBAG = null;

	@CapabilityInject(ITPLocation.class)
	public static Capability<ITPLocation> TP_MENU = null;

	public CapabilityLoader(FMLPreInitializationEvent event)
	{
		CapabilityManager.INSTANCE.register(IPortableInventory.class, new PortableInventory.Storage(),
				(Callable<PortableInventory.Implementation>) () -> new PortableInventory.Implementation());
		CapabilityManager.INSTANCE.register(IGarbagBag.class, new GarbagBagCP.Storage(),
				(Callable<GarbagBagCP.Implementation>) () -> new GarbagBagCP.Implementation());
		CapabilityManager.INSTANCE.register(ITPLocation.class, new TPLocation.Storage(),
				(Callable<TPLocation.Implementation>) () -> new TPLocation.Implementation());
	}

}
