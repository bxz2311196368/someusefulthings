package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractContainer extends Container
{
	protected IItemHandler items;
	protected TileEntityBase te;
	protected TileEntity basic_te;
	protected EntityPlayer player;

	public AbstractContainer(EntityPlayer player, TileEntityBase tileEntity, int playerInventoryOffsetX,
			int playerInventoryOffsetY)
	{
		this(player, tileEntity, playerInventoryOffsetX, playerInventoryOffsetY, false);
	}

	public AbstractContainer(EntityPlayer player, TileEntityBase tileEntity, int playerInventoryOffsetX,
			int playerInventoryOffsetY, boolean lockMainHandItem)
	{
		this.player = player;
		this.te = tileEntity;
		this.items = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.addPlayerInventory(player, playerInventoryOffsetX, playerInventoryOffsetY, lockMainHandItem);
	}

	public AbstractContainer(TileEntityBase te)
	{
		this.te = te;
	}

	public AbstractContainer(TileEntity basic_te)
	{
		this.basic_te = basic_te;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = inventorySlots.get(index);

		int slotsSize = this.inventorySlots.size();

		if (slot == null || !slot.getHasStack())
		{
			return null;
		}

		ItemStack newStack = slot.getStack(), oldStack = newStack.copy();

		boolean isMerged = false;

		if (index >= 0 && index < 27)
		{
			isMerged = mergeItemStack(newStack, 36, slotsSize, false) || mergeItemStack(newStack, 27, 36, false);
			// player'inventory clicked(27), move to te's inventory first.
			// if can't,move to player's hot bar(9).
		} else if (index >= 27 && index < 36)
		{
			isMerged = mergeItemStack(newStack, 36, slotsSize, false) || mergeItemStack(newStack, 0, 27, false);
			// player's hot bar clicked(9), move to te's inventory first.
			// if can't,move to player's inventory(27).
		} else if (index >= 36 && index < slotsSize)
		{
			isMerged = mergeItemStack(newStack, 0, 36, true);
			// te's inventory clicked, move to player's inventory(36).
		}

		if (!isMerged)
		{
			return null;
		}

		if (newStack.stackSize == 0)
		{
			slot.putStack(null);
		} else
		{
			slot.onSlotChanged();
		}

		slot.onPickupFromSlot(playerIn, newStack);

		return oldStack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		if (stack == null || stack.stackSize <= 0)
			return false;
		boolean flag = false;
		int i = reverseDirection? endIndex - 1:startIndex;
		ItemStack newStack = Helper.copyStack(stack);
		while (newStack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
		{
			Slot slot = this.getSlot(i);
			i = reverseDirection? i - 1: i + 1;
			if (!slot.isItemValid(newStack))
				continue;
			ItemStack temp = slot.getStack();
			if (temp != null)
			{
				if (Helper.isSameStackIgnoreAmount(newStack, temp))
				{
					int removed = Math.min(temp.getMaxStackSize()- temp.stackSize, newStack.stackSize);
					if (removed > 0)
					{
						newStack.stackSize -= removed;
						temp.stackSize += removed;
						slot.onSlotChanged();
					}
				}
				flag = newStack.stackSize != stack.stackSize;
			} else
			{
				slot.putStack(Helper.copyStack(stack));
				newStack.stackSize = 0;
				flag = true;
			}
		}
		stack.stackSize = newStack.stackSize;
		return flag;
	}

	private void addPlayerInventory(EntityPlayer player, int offsetX, int offsetY, boolean lockMainHandItem)
	{
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, offsetX + j * 18, offsetY + i * 18));
				// add player's inventory(27).
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, offsetX + i * 18, offsetY + 18 * 3 + 4)
			{

				@Override
				public boolean canTakeStack(EntityPlayer playerIn)
				{
					return !lockMainHandItem || playerIn.inventory.currentItem != this.slotNumber;
				}

			});
			// add player's hot bar(9).
		}
	}

	public TileEntityBase getTe()
	{
		return te;
	}

	public void setTe(TileEntityBase te)
	{
		this.te = te;
	}
}
