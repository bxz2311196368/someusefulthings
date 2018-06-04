package com.bxzmod.someusefulthings.gui.server;

import javax.annotation.Nullable;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.FastFurnaceTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FastFurnaceContainer extends Container
{
	FastFurnaceTileEntity te;

	public FastFurnaceContainer(EntityPlayer player, FastFurnaceTileEntity te)
	{
		super();
		this.te = te;
		this.addSlotToContainer(new SlotItemHandlerHelper(te.getiInventory(), 0, 44, 20));
		this.addSlotToContainer(new SlotItemHandlerHelper(te.getiInventory(), 1, 116, 20));
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 109));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Nullable
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
				if (!this.mergeItemStack(itemstack1, 2, 38, true))
					return null;
			} else if (index == 1)
			{
				if (!this.mergeItemStack(itemstack1, 2, 38, true))
					return null;
			} else if (index >= 2 && index < 29)
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false) && !this.mergeItemStack(itemstack1, 29, 38, false))
					return null;
			} else if (index >= 29 && index < 38)
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false) && !this.mergeItemStack(itemstack1, 2, 29, false))
					return null;
			} else if (!this.mergeItemStack(itemstack1, 2, 38, false))
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
