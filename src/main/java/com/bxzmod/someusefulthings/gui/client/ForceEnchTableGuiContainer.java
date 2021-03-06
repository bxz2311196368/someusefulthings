package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.AbstractContainer;
import net.minecraft.util.ResourceLocation;

public class ForceEnchTableGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/ForceEnchTable.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public ForceEnchTableGuiContainer(AbstractContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "forceEnchTable", TEXTURE, 176, 133);
	}

}
