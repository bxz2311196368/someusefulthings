package com.bxzmod.someusefulthings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class SideInventory implements ISidedInventory, INBTSerializable<NBTTagCompound>
{

	protected ItemStack[] slots_input, slots_Output;

	protected int input, output;

	public SideInventory(int input, int output)
	{
		this.input = input;
		this.output = output;
		this.slots_input = new ItemStack[input];
		this.slots_Output = new ItemStack[output];
	}

	public ItemStack[] getStacksInput()
	{
		return slots_input;
	}

	public ItemStack[] getStacksOutput()
	{
		return slots_Output;
	}

	public ItemStack setStackInSlot(int index, ItemStack stack)
	{
		if (index < input)
			this.slots_input[index] = stack;
		else
			this.slots_Output[index - input] = stack;
		return index < input ? this.slots_input[index] : this.slots_Output[index - input];
	}

	@Override
	public int getSizeInventory()
	{
		return this.slots_input.length + this.slots_Output.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index < input ? this.slots_input[index] : this.slots_Output[index - input];
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (getStackInSlot(index) != null)
		{
			ItemStack newStack;
			if (getStackInSlot(index).stackSize <= count)
			{
				newStack = getStackInSlot(index);
				removeStackFromSlot(index);
			} else
			{
				newStack = getStackInSlot(index).splitStack(count);
				if (getStackInSlot(index).stackSize == 0)
				{
					removeStackFromSlot(index);
				}
			}
			return newStack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (getStackInSlot(index) != null)
		{
			ItemStack stack = getStackInSlot(index);
			setStackInSlot(index, null);
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.setStackInSlot(index, stack);
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public void closeInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index >= 0 && index < this.getSizeInventory())
			return true;
		return false;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.getSizeInventory(); i++)
			this.setStackInSlot(i, null);
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (direction == EnumFacing.DOWN)
			return false;
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if (direction == EnumFacing.DOWN)
			return true;
		return false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagList nbtTagListInput = new NBTTagList();
		NBTTagList nbtTagListOutput = new NBTTagList();
		for (int i = 0; i < this.slots_input.length; i++)
		{
			if (slots_input[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				slots_input[i].writeToNBT(itemTag);
				nbtTagListInput.appendTag(itemTag);
			}
		}
		for (int i = 0; i < this.slots_Output.length; i++)
		{
			if (slots_Output[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				slots_Output[i].writeToNBT(itemTag);
				nbtTagListOutput.appendTag(itemTag);
			}
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Items_Input", nbtTagListInput);
		nbt.setInteger("Size_Input", slots_input.length);
		nbt.setTag("Items_Output", nbtTagListOutput);
		nbt.setInteger("Size_Output", slots_Output.length);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		setSize(nbt.hasKey("Size_Input", Constants.NBT.TAG_INT) ? nbt.getInteger("Size_Input") : slots_input.length,
				nbt.hasKey("Size_Output", Constants.NBT.TAG_INT) ? nbt.getInteger("Size_Output") : slots_Output.length);
		NBTTagList tagListInput = nbt.getTagList("Items_Input", Constants.NBT.TAG_COMPOUND);
		NBTTagList tagListOutput = nbt.getTagList("Items_Output", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagListInput.tagCount(); i++)
		{
			NBTTagCompound itemTags = tagListInput.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < slots_input.length)
			{
				slots_input[slot] = ItemStack.loadItemStackFromNBT(itemTags);
			}
		}
		for (int i = 0; i < tagListOutput.tagCount(); i++)
		{
			NBTTagCompound itemTags = tagListOutput.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < slots_Output.length)
			{
				slots_Output[slot] = ItemStack.loadItemStackFromNBT(itemTags);
			}
		}
	}

	private void setSize(int input, int output)
	{
		this.slots_input = new ItemStack[input];
		this.slots_Output = new ItemStack[output];
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side != EnumFacing.DOWN ? this.generateArray(input, 0) : this.generateArray(output, input);
	}

	public int[] generateArray(int size, int start)
	{
		if (size <= 0)
			return null;
		int[] a = new int[size];
		for (int i = 0; i < size; i++)
			a[i] = start + i;
		return a;
	}

}
