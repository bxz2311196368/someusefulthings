package com.bxzmod.someusefulthings.capability;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortableInventory
{

	private static final Logger LOGGER = LogManager.getLogger();

	public static class Storage implements Capability.IStorage<IPortableInventory>
	{

		@Override
		public NBTBase writeNBT(Capability<IPortableInventory> capability, IPortableInventory instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IPortableInventory> capability, IPortableInventory instance, EnumFacing side,
				NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}

	}

	public static class Implementation implements IPortableInventory
	{
		protected ItemStack[][] stacks = new ItemStack[16][54];
		protected int backpackToOpen = 0;

		public Implementation()
		{
			this(16, 54);
		}

		public Implementation(int num, int size)
		{
			stacks = new ItemStack[num][size];
		}

		public Implementation(ItemStack[] stacks)
		{
			this.stacks = new ItemStack[][] { stacks };
		}

		public Implementation(ItemStack[][] stacks)
		{
			this.stacks = stacks;
		}

		public void setSize(int num, int size)
		{
			stacks = new ItemStack[num][size];
		}

		@Override
		public int getOpen()
		{
			return backpackToOpen;
		}

		@Override
		public void setOpen(int i)
		{
			backpackToOpen = i;
		}

		@Override
		public ItemStack[] getStacArrayAtNum(int num)
		{
			if (num < stacks.length)
				return this.stacks[num];
			return null;
		}

		@Override
		public void setStacArrayAtNum(int num, ItemStack[] stack)
		{
			if (num < stacks.length)
				this.stacks[num] = stack;
		}

		@Override
		public void setStackInSlotAtNum(int num, int slot, ItemStack stack)
		{
			validateSlotIndex(num, slot);
			if (ItemStack.areItemStacksEqual(this.stacks[num][slot], stack))
				return;
			this.stacks[num][slot] = stack;
			onContentsChanged(slot);
		}

		@Override
		public int getInvs()
		{
			return stacks.length;
		}

		@Override
		public int getSlotsAtNum(int num)
		{
			if (stacks[num] == null)
				return 0;
			return stacks[num].length;
		}

		@Override
		public ItemStack getStackInSlotAtNum(int num, int slot)
		{
			validateSlotIndex(num, slot);
			return this.stacks[num][slot];
		}

		@Override
		public ItemStack insertItemAtNum(int num, int slot, ItemStack stack, boolean simulate)
		{
			if (stack == null || stack.stackSize == 0)
				return null;

			validateSlotIndex(num, slot);

			ItemStack existing = this.stacks[num][slot];

			int limit = getStackLimit(num, slot, stack);

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
					this.stacks[num][slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
				} else
				{
					existing.stackSize += reachedLimit ? limit : stack.stackSize;
				}
				onContentsChanged(slot);
			}

			return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
		}

		@Override
		public ItemStack extractItemAtNum(int num, int slot, int amount, boolean simulate)
		{
			if (amount == 0)
				return null;

			validateSlotIndex(num, slot);

			ItemStack existing = this.stacks[num][slot];

			if (existing == null)
				return null;

			int toExtract = Math.min(amount, existing.getMaxStackSize());

			if (existing.stackSize <= toExtract)
			{
				if (!simulate)
				{
					this.stacks[num][slot] = null;
					onContentsChanged(slot);
				}
				return existing;
			} else
			{
				if (!simulate)
				{
					this.stacks[num][slot] = ItemHandlerHelper.copyStackWithSize(existing,
							existing.stackSize - toExtract);
					onContentsChanged(slot);
				}

				return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
			}
		}

		protected int getStackLimit(int num, int slot, ItemStack stack)
		{
			return stack.getMaxStackSize();
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbtTagInv = new NBTTagCompound();
			NBTTagCompound nbtTagNum = new NBTTagCompound();
			NBTTagList nbtTagList = new NBTTagList();
			NBTTagList nbtTagNull = new NBTTagList();
			NBTTagCompound temp = new NBTTagCompound();
			temp.setInteger("Slot", 0);
			nbtTagNull.appendTag(temp);
			for (int i = 0; i < stacks.length; i++)
			{
				for (int j = 0; j < stacks[i].length; j++)
				{
					if (stacks[i][j] != null)
					{
						NBTTagCompound itemTag = new NBTTagCompound();
						NBTTagCompound itemTagTemp = new NBTTagCompound();
						itemTag = itemTagTemp.copy();
						itemTag.setInteger("Slot", j);
						stacks[i][j].writeToNBT(itemTag);
						nbtTagList.appendTag(itemTag);
					}
				}
				if (!nbtTagList.hasNoTags())
				{
					nbtTagNum.setTag("INV", nbtTagList.copy());
					nbtTagNum.setInteger("Size", stacks[i].length);
					nbtTagList = nbtTagNull.copy();
					nbtTagList.removeTag(0);
				} else
				{
					nbtTagNum.setTag("INV", nbtTagNull);
					nbtTagNum.setInteger("Size", 0);
				}
				nbtTagInv.setTag(EnumDyeColor.byMetadata(i).toString(), nbtTagNum.copy());
				nbtTagNum.removeTag("INV");
				nbtTagNum.removeTag("Size");
			}
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("Items", nbtTagInv.copy());
			nbt.setInteger("Size", stacks.length);
			nbt.setInteger("toOpen", backpackToOpen);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			setSize(stacks.length, stacks[0].length);
			NBTTagCompound invList = nbt.getCompoundTag("Items");

			if (invList != null && !invList.hasNoTags())
				for (int i = 0; i < invList.getSize(); i++)
				{
					NBTTagCompound invNum = invList.getCompoundTag(EnumDyeColor.byMetadata(i).toString()).copy();
					NBTTagList tagList = invNum.getTagList("INV", Constants.NBT.TAG_COMPOUND).copy();
					if (invNum.getInteger("Size") > 0)
					{
						for (int j = 0; j < tagList.tagCount(); j++)
						{
							NBTTagCompound itemTags = tagList.getCompoundTagAt(j);
							int slot = itemTags.getInteger("Slot");

							if (slot >= 0 && slot < stacks[i].length)
							{
								stacks[i][slot] = ItemStack.loadItemStackFromNBT(itemTags);
							}
						}
					}
				}
			backpackToOpen = nbt.getInteger("toOpen");
			onLoad();
		}

		protected void validateSlotIndex(int num, int slot)
		{
			if (num < 0 || num > stacks.length)
				throw new RuntimeException("SlotNum " + num + " not in valid range - [0," + stacks.length + ")");
			if (slot < 0 || slot >= stacks[num].length)
				throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks[num].length + ")");
		}

		protected void onLoad()
		{

		}

		protected void onContentsChanged(int slot)
		{

		}

	}

	public static class ProviderPlayer implements ICapabilitySerializable<NBTTagCompound>
	{
		private IPortableInventory portableInventory = new Implementation();
		private IStorage<IPortableInventory> storage = CapabilityLoader.PORTABLE_INVENTORY.getStorage();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return CapabilityLoader.PORTABLE_INVENTORY.equals(capability);
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if (CapabilityLoader.PORTABLE_INVENTORY.equals(capability))
			{
				T result = (T) portableInventory;
				return result;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound compound = (NBTTagCompound) storage.writeNBT(CapabilityLoader.PORTABLE_INVENTORY,
					portableInventory, null);
			return compound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound compound)
		{
			storage.readNBT(CapabilityLoader.PORTABLE_INVENTORY, portableInventory, null, compound);
		}
	}

}
