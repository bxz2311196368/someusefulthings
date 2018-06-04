package com.bxzmod.someusefulthings.throwable;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.entity.EntityLoader;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ThrowableLoader
{
	private static int nextID = EntityLoader.nextID;

	public ThrowableLoader(FMLPreInitializationEvent event)
	{
		registerEntity(InfinityArrow.class, "InfinityArrow", 64, 10, true);

	}

	private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, nextID++, Main.instance, trackingRange, updateFrequency,
				sendsVelocityUpdates);
	}

}
