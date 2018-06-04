package com.bxzmod.someusefulthings.gui;

import com.bxzmod.someusefulthings.ItemStackHandlerModify;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerHelper extends SlotItemHandler
{
	private final int num;
	private ItemStackHandlerModify inv;

	public SlotItemHandlerHelper(IItemHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.num = index;
		try
		{
			this.inv = (ItemStackHandlerModify) itemHandler;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return this.inv.isItemValidForSlot(this.num, stack);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return this.inv.getStackInSlot(num) != null && this.inv.getStackInSlot(num).stackSize > 0;
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		return this.inv.decrStackSize(this.num, amount);
	}

}
