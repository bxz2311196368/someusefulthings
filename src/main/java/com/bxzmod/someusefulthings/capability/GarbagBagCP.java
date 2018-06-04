package com.bxzmod.someusefulthings.capability;

import com.bxzmod.someusefulthings.Helper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.ItemHandlerHelper;

public class GarbagBagCP
{

	public static class Storage implements Capability.IStorage<IGarbagBag>
	{
		@Override
		public NBTBase writeNBT(Capability<IGarbagBag> capability, IGarbagBag instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IGarbagBag> capability, IGarbagBag instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	public static class Implementation implements IGarbagBag
	{
		protected ItemStack[] stacks = new ItemStack[54];

		boolean enable = false;

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbtTagInv = new NBTTagCompound();
			nbtTagInv.setTag("garbag", Helper.writeStacksToNBT(stacks));
			nbtTagInv.setBoolean("enable", enable);
			return nbtTagInv;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			reset();
			stacks = Helper.loadStacksFromNBT(54, (NBTTagList) nbt.getTag("garbag"));
			enable = nbt.getBoolean("enable");
			onLoad();
		}

		@Override
		public int getSlots()
		{
			return stacks.length;
		}

		@Override
		public ItemStack[] getStacks()
		{
			return stacks;
		}

		@Override
		public ItemStack getStackInSlot(int slot)
		{
			validateSlotIndex(slot);
			return stacks[slot];
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (stack == null || stack.stackSize == 0)
				return null;

			validateSlotIndex(slot);

			ItemStack existing = stacks[slot];

			int limit = getStackLimit(slot, stack);

			if (existing != null)
			{
				if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
					return stack;

				limit -= existing.stackSize;
			}

			if (limit <= 0)
				return stack;

			boolean reachedLimit = stack.stackSize > limit;

			if (!simulate)
			{
				if (existing == null)
				{
					stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
				} else
				{
					existing.stackSize += reachedLimit ? limit : stack.stackSize;
				}
				onContentsChanged(slot);
			}

			return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			if (amount == 0)
				return null;

			validateSlotIndex(slot);

			ItemStack existing = stacks[slot];

			if (existing == null)
				return null;

			int toExtract = Math.min(amount, existing.getMaxStackSize());

			if (existing.stackSize <= toExtract)
			{
				if (!simulate)
				{
					stacks[slot] = null;
					onContentsChanged(slot);
				}
				return existing;
			} else
			{
				if (!simulate)
				{
					stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract);
					onContentsChanged(slot);
				}

				return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
			}
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack)
		{
			validateSlotIndex(slot);
			if (ItemStack.areItemStacksEqual(stacks[slot], stack))
				return;
			stacks[slot] = stack;
			onContentsChanged(slot);
		}

		@Override
		public void setStacArray(ItemStack[] stack)
		{
			stacks = stack;
		}

		@Override
		public boolean isEnable()
		{
			return enable;
		}

		@Override
		public void changeEnable()
		{
			enable = !enable;
		}

		private void validateSlotIndex(int slot)
		{
			if (slot < 0 || slot >= stacks.length)
				throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.length + ")");
		}

		private void onContentsChanged(int slot)
		{

		}

		private int getStackLimit(int slot, ItemStack stack)
		{
			return stack.getMaxStackSize();
		}

		private void reset()
		{
			stacks = new ItemStack[54];
		}

		private void onLoad()
		{

		}

	}

	public static class ProviderPlayer implements ICapabilitySerializable<NBTTagCompound>
	{
		private IGarbagBag garbagBag = new Implementation();
		private IStorage<IGarbagBag> storage = CapabilityLoader.GARBAGBAG.getStorage();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return CapabilityLoader.GARBAGBAG.equals(capability);
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if (CapabilityLoader.GARBAGBAG.equals(capability))
			{
				T result = (T) garbagBag;
				return result;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound compound = (NBTTagCompound) storage.writeNBT(CapabilityLoader.GARBAGBAG, garbagBag, null);
			return compound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound compound)
		{
			storage.readNBT(CapabilityLoader.GARBAGBAG, garbagBag, null, compound);
		}

	}

}
