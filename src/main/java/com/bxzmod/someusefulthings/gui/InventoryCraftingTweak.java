package com.bxzmod.someusefulthings.gui;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class InventoryCraftingTweak extends InventoryCrafting
{

	public InventoryCraftingTweak(Container eventHandlerIn, ItemStack[] stacks)
	{
		super(eventHandlerIn, 3, 3);
		this.stackList = stacks;
	}

	public InventoryCraftingTweak(ItemStack[] stacks)
	{
		super(null, 3, 3);
		this.stackList = stacks;
	}

	public InventoryCraftingTweak()
	{
		super(null, 3, 3);
	}

	public ItemStack[] getStacks()
	{
		return this.stackList;
	}

	public void setStacks(ItemStack[] stacks)
	{
		this.stackList = stacks;
	}

	@Override
	public int getSizeInventory()
	{
		return 9;
	}

	@Nullable
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.stackList, index, count);
	}
}
