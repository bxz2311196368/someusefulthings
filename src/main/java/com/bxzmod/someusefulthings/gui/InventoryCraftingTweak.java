package com.bxzmod.someusefulthings.gui;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCraftingTweak extends InventoryCrafting
{

	public InventoryCraftingTweak(Container eventHandlerIn, ItemStack[] stacks)
	{
		super(eventHandlerIn, 3, 3);
		this.stackList = stacks;
	}

	public InventoryCraftingTweak(Container eventHandlerIn)
	{
		super(eventHandlerIn, 3, 3);
	}

	public ItemStack[] getStacks()
	{
		return this.stackList;
	}

	public void setStacks(ItemStack[] stacks)
	{
		this.stackList = stacks;
	}

}
