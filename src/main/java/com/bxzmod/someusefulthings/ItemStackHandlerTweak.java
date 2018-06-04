package com.bxzmod.someusefulthings;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerTweak extends ItemStackHandler
{

	public ItemStackHandlerTweak()
	{
		this(1);
	}

	public ItemStackHandlerTweak(int size)
	{
		super(size);
	}

	public ItemStackHandlerTweak(ItemStack[] stacks)
	{
		super(stacks);
	}

	public ItemStack[] getStacks()
	{
		return this.stacks;
	}

	public void setStacks(ItemStack[] stacks)
	{
		this.stacks = stacks;
	}

}
