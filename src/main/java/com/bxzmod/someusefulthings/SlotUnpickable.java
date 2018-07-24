package com.bxzmod.someusefulthings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotUnpickable extends Slot
{
	public SlotUnpickable(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(@Nullable ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return false;
	}
}
