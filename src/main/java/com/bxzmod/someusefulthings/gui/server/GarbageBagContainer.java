package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.StackHelper;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.network.GarbageBagSetting;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GarbageBagContainer extends Container
{
	private StackHelper items = new StackHelper(54);
	private EntityPlayer p;
	private boolean enable = false;

	public GarbageBagContainer(EntityPlayer player)
	{
		super();
		this.p = player;
		if (player != null && player.hasCapability(CapabilityLoader.GARBAGBAG, null))
		{
			items.setStackArray(player.getCapability(CapabilityLoader.GARBAGBAG, null).getStacks());
			enable = player.getCapability(CapabilityLoader.GARBAGBAG, null).isEnable();
		}
		for (int i = 0; i < 6; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new SlotItemHandler(items, j + i * 9, 8 + j * 18, 18 + i * 18));
			}

		}

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 198));
		}
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

		if (index >= 0 && index < 54)
		{
			isMerged = mergeItemStack(newStack, 54, 90, true);
		} else if (index >= 54 && index < 81)
		{
			isMerged = mergeItemStack(newStack, 0, 54, false) || mergeItemStack(newStack, 81, 90, false);
		} else if (index >= 81 && index < 90)
		{
			isMerged = mergeItemStack(newStack, 0, 54, false) || mergeItemStack(newStack, 54, 81, false);
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
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			GarbageBagSetting message = new GarbageBagSetting();
			message.nbt = new NBTTagCompound();
			message.nbt.setTag("garbag", Helper.writeStacksToNBT(items.getStackArray()));
			message.nbt.setString("name", playerIn.getName());
			message.nbt.setBoolean("enable", enable);
			NetworkLoader.instance.sendToServer(message);
		}
	}

	public boolean isEnable()
	{
		return enable;
	}

	public void changeEnable()
	{
		enable = !enable;
		p.getCapability(CapabilityLoader.GARBAGBAG, null).changeEnable();
	}

}
