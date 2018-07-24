package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.gui.server.CraftingTableContainer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class InventoryCraftResultTweak extends InventoryCraftResult
{
	private CraftingTableContainer gui;
	private boolean isServerSide = FMLCommonHandler.instance().getSide().isServer();

	public InventoryCraftResultTweak(CraftingTableContainer gui)
	{
		this.gui = gui;
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack)
	{
		super.setInventorySlotContents(index, stack);
		if (isServerSide)
			this.gui.getTe().setRecipe();
	}
}
