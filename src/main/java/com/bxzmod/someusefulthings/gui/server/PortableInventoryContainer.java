package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.StackHelper;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.capability.IPortableInventory;
import com.bxzmod.someusefulthings.network.DataInteraction;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class PortableInventoryContainer extends Container
{
	private StackHelper items = new StackHelper(54);

	private EntityPlayer p;

	private boolean hasSync = false;

	int meta = 0;

	boolean reOpen = false;

	Capability<IPortableInventory> capability = CapabilityLoader.PORTABLE_INVENTORY;

	public PortableInventoryContainer(EntityPlayer player)
	{
		super();
		this.p = player;
		if (player.hasCapability(capability, null))
		{
			meta = player.getCapability(capability, null).getOpen();
			if (meta < 16)
				items.setStackArray(player.getCapability(capability, null).getStacArrayAtNum(meta));
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
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		if (!hasSync)
			syncData(playerIn);
	}

	public void syncData(EntityPlayer playerIn)
	{
		DataInteraction message = new DataInteraction();
		IPortableInventory cp = playerIn.getCapability(CapabilityLoader.PORTABLE_INVENTORY, null);
		IStorage<IPortableInventory> storage = CapabilityLoader.PORTABLE_INVENTORY.getStorage();
		cp.setStacArrayAtNum(meta, items.getStackArray());
		message.nbt = new NBTTagCompound();
		message.nbt = (NBTTagCompound) storage.writeNBT(CapabilityLoader.PORTABLE_INVENTORY, cp, null);
		message.nbt.setString("name", playerIn.getName());
		message.nbt.setBoolean("OpenGui", reOpen);
		NetworkLoader.instance.sendToServer(message);
		hasSync = true;
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

	public EntityPlayer getPlayer()
	{
		return this.p;
	}

	public void setReOpen()
	{
		this.reOpen = true;
	}
}
