package com.bxzmod.someusefulthings.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbenchTweak extends ContainerWorkbench
{

	public ContainerWorkbenchTweak(InventoryPlayer playerInventory, World worldIn, BlockPos posIn)
	{
		super(playerInventory, worldIn, posIn);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37)
			{
				if (!this.mergeItemStack(itemstack1, 1, 10, false) && !this.mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			} else if (index >= 37 && index < 46)
			{
				if (!this.mergeItemStack(itemstack1, 1, 10, false) && !this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}

}
