package com.bxzmod.someusefulthings.throwable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;

public class RenderInfinityArrow extends RenderSnowball<InfinityArrow>
{

	public RenderInfinityArrow(RenderManager renderManagerIn)
	{
		super(renderManagerIn, Items.SLIME_BALL, Minecraft.getMinecraft().getRenderItem());
	}

}
