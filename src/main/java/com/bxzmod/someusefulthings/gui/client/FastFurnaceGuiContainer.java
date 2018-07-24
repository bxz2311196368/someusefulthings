package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.FastFurnaceContainer;
import net.minecraft.util.ResourceLocation;

public class FastFurnaceGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/FastFurnace.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public FastFurnaceGuiContainer(FastFurnaceContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "fastFurnace", TEXTURE, 176, 133);
	}

}
