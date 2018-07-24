package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractContainer extends Container
{
	protected IItemHandler items;
	protected TileEntityBase te;
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = inventorySlots.get(index);

		int itemHandlerSlots = this.items.getSlots();

		if (slot == null || !slot.getHasStack())
		{
			return null;
		}

		ItemStack newStack = slot.getStack(), oldStack = newStack.copy();

		boolean isMerged = false;

		if (index >= 0 && index < 27)
		{
			isMerged = mergeItemStack(newStack, 36, 36 + itemHandlerSlots, false)
					|| mergeItemStack(newStack, 27, 36, false);
			// player'inventory clicked(27), move to te's inventory first.
			// if can't,move to player's hot bar(9).
		} else if (index >= 27 && index < 36)
		{
			isMerged = mergeItemStack(newStack, 36, 36 + itemHandlerSlots, false)
					|| mergeItemStack(newStack, 0, 27, false);
			// player's hot bar clicked(9), move to te's inventory first.
			// if can't,move to player's inventory(27).
		} else if (index >= 36 && index < 36 + itemHandlerSlots)
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
					return lockMainHandItem ? playerIn.inventory.currentItem != this.slotNumber : true;
				}

			});
			// add player's hot bar(9).
		}
	}
}
