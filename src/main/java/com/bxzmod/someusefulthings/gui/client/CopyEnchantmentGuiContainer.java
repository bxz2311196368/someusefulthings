package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.AbstractContainer;
import net.minecraft.util.ResourceLocation;

public class CopyEnchantmentGuiContainer extends BaseGuiContainer
{

	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/CopyEnchantment.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public CopyEnchantmentGuiContainer(AbstractContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "copyEnchantment", TEXTURE, 176, 133);
	}

}
