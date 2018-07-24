package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RemoveEnchantmentGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/RemoveEnchantment.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public RemoveEnchantmentGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn, "removeEnchantment", TEXTURE, 176, 133);
	}

}
