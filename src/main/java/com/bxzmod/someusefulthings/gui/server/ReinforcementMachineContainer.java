package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.ReinforcementMachineTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ReinforcementMachineContainer extends Container
{
	private IItemHandler items;

	protected SlotItemHandlerHelper hasDamgae;
	protected SlotItemHandlerHelper nether_start;
	protected SlotItemHandlerHelper unbreakable;

	protected ReinforcementMachineTileEntity te;

	protected int workTime = 0;

	public ReinforcementMachineContainer(EntityPlayer player, TileEntity tileEntity)
	{
		super();

		this.te = (ReinforcementMachineTileEntity) tileEntity;
		this.items = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		this.addSlotToContainer(this.hasDamgae = new SlotItemHandlerHelper(items, 0, 26, 20)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem().isDamageable()
						&& !(stack.hasTagCompound() && stack.getTagCompound().getByte("Unbreakable") == (byte) 1);
			}

			@Override
			public int getItemStackLimit(ItemStack stack)
			{
				return 1;
			}
		});

		this.addSlotToContainer(this.nether_start = new SlotItemHandlerHelper(items, 1, 62, 20)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack != null && stack.getItem() == Items.NETHER_STAR && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack)
			{
				return 64;
			}
		});

		this.addSlotToContainer(this.unbreakable = new SlotItemHandlerHelper(items, 2, 134, 20)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});

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

		if (index >= 0 && index < 3)
		{
			isMerged = mergeItemStack(newStack, 3, 39, true);
		} else if (index >= 3 && index < 30)
		{
			isMerged = !hasDamgae.getHasStack() && newStack.stackSize <= 1 && mergeItemStack(newStack, 0, 1, false)
					|| mergeItemStack(newStack, 0, 2, false) || mergeItemStack(newStack, 30, 39, false);
		} else if (index >= 30 && index < 39)
		{
			isMerged = !hasDamgae.getHasStack() && newStack.stackSize <= 1 && mergeItemStack(newStack, 0, 1, false)
					|| mergeItemStack(newStack, 0, 2, false) || mergeItemStack(newStack, 3, 30, false);
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
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		this.workTime = te.getWorkTime();

		for (IContainerListener i : this.listeners)
		{
			i.sendProgressBarUpdate(this, GuiLoader.DATA_R_M, this.workTime);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		super.updateProgressBar(id, data);

		switch (id)
		{
		case GuiLoader.DATA_R_M:
			this.workTime = data;
			break;
		default:
			break;
		}
	}

	public int getWorkTime()
	{
		return this.workTime;
	}

	public int getTotalWorkTime()
	{
		return this.te.getTotalWorkTime();
	}

}