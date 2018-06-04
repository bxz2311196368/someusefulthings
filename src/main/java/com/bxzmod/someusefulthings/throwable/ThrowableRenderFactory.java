package com.bxzmod.someusefulthings.throwable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ThrowableRenderFactory<T extends Entity> implements IRenderFactory<T>
{
	private final Class<? extends Render<T>> renderClass;

	public ThrowableRenderFactory(Class<? extends Render<T>> renderClass)
	{
		this.renderClass = renderClass;
	}

	@Override
	public Render<T> createRenderFor(RenderManager manager)
	{
		try
		{
			return renderClass.getConstructor(RenderManager.class).newInstance(manager);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
