package com.bxzmod.someusefulthings.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPortableInventory extends INBTSerializable<NBTTagCompound>
{

	int getSlotsAtNum(int num);

	int getInvs();

	ItemStack[] getStacArrayAtNum(int num);

	ItemStack getStackInSlotAtNum(int num, int slot);

	ItemStack insertItemAtNum(int num, int slot, ItemStack stack, boolean simulate);

	ItemStack extractItemAtNum(int num, int slot, int amount, boolean simulate);

	void setStackInSlotAtNum(int num, int slot, ItemStack stack);

	void setStacArrayAtNum(int num, ItemStack[] stack);

	int getOpen();

	void setOpen(int i);

}
