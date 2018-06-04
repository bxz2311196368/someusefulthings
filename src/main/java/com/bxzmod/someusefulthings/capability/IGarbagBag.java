package com.bxzmod.someusefulthings.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGarbagBag extends INBTSerializable<NBTTagCompound>
{
	int getSlots();

	ItemStack[] getStacks();

	ItemStack getStackInSlot(int slot);

	ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

	ItemStack extractItem(int slot, int amount, boolean simulate);

	void setStackInSlot(int slot, ItemStack stack);

	void setStacArray(ItemStack[] stack);

	boolean isEnable();

	void changeEnable();
}
