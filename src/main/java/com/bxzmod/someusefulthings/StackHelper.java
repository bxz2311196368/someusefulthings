package com.bxzmod.someusefulthings;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class StackHelper extends ItemStackHandler
{

	private int size;

	public StackHelper()
	{
		this(1);
	}

	public StackHelper(int size)
	{
		super(size);
		this.size = size;
	}

	public StackHelper(ItemStack[] stacks)
	{
		super(stacks);
		this.size = stacks.length;
	}

	public ItemStack[] getStackArray()
	{
		return this.stacks;
	}

	public void setStackArray(ItemStack[] s)
	{
		this.stacks = s;
	}

	public void setStackAtNum(ItemStack s, int n)
	{
		if (n >= size)
			return;
		this.stacks[n] = s;
	}
}
