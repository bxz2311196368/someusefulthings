package com.bxzmod.someusefulthings;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class SidedInvWrapperTweak extends SidedInvWrapper
{

	public SidedInvWrapperTweak(ISidedInventory inv, EnumFacing side)
	{
		super(inv, side);
	}

	public ISidedInventory getSideinventory()
	{
		return this.inv;
	}

}
