package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.GreenHouseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GreenHouseContainer extends Container
{
	private GreenHouseTileEntity te;

	public GreenHouseContainer(EntityPlayer player, TileEntity tileEntity)
	{
		super();
		if (tileEntity instanceof GreenHouseTileEntity)
			this.te = (GreenHouseTileEntity) tileEntity;
		else
			throw new RuntimeException("null tileentity!");

		IItemHandler inv = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		this.addSlotToContainer(new SlotItemHandlerHelper(inv, 0, 34, 35));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				this.addSlotToContainer(new SlotItemHandlerHelper(inv, i * 3 + j + 1, 98 + j * 18, 16 + i * 18));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int i = 0; i < 9; ++i)
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
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

		if (slot == null || !slot.getHasStack())
		{
			return null;
		}

		ItemStack newStack = slot.getStack(), oldStack = newStack.copy();

		boolean isMerged = false;

		if (index >= 0 && index < 10)
		{
			isMerged = mergeItemStack(newStack, 10, 46, true);
		} else if (index >= 10 && index < 37)
		{
			isMerged = mergeItemStack(newStack, 0, 1, false) || mergeItemStack(newStack, 37, 46, false);
		} else if (index >= 37 && index < 46)
		{
			isMerged = mergeItemStack(newStack, 0, 1, false) || mergeItemStack(newStack, 10, 37, false);
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

}
