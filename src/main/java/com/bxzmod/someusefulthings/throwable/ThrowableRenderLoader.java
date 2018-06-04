package com.bxzmod.someusefulthings.throwable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ThrowableRenderLoader
{

	public ThrowableRenderLoader(FMLPreInitializationEvent event)
	{
		registerThrowableRenders();
	}

	@SideOnly(Side.CLIENT)
	public static void registerThrowableRenders()
	{
		registerEntityRender(InfinityArrow.class, RenderInfinityArrow.class);
	}

	@SideOnly(Side.CLIENT)
	private static <T extends Entity> void registerEntityRender(Class<T> entityClass, Class<? extends Render<T>> render)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new ThrowableRenderFactory<T>(render));
	}

}
