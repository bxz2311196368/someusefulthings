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
				new Callable<PortableInventory.Implementation>()
				{
					@Override
					public PortableInventory.Implementation call() throws Exception
					{
						return new PortableInventory.Implementation();
					}
				});
		CapabilityManager.INSTANCE.register(IGarbagBag.class, new GarbagBagCP.Storage(),
				new Callable<GarbagBagCP.Implementation>()
				{
					@Override
					public GarbagBagCP.Implementation call() throws Exception
					{
						return new GarbagBagCP.Implementation();
					}
				});
		CapabilityManager.INSTANCE.register(ITPLocation.class, new TPLocation.Storage(),
				new Callable<TPLocation.Implementation>()
				{
					@Override
					public TPLocation.Implementation call() throws Exception
					{
						return new TPLocation.Implementation();
					}
				});
	}

}
