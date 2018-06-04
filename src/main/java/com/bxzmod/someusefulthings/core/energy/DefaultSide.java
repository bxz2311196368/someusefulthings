package com.bxzmod.someusefulthings.core.energy;

import java.util.HashMap;

import com.google.common.primitives.Booleans;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class DefaultSide implements IEnergySide
{
	private HashMap<EnumFacing, Boolean> sides = new HashMap<EnumFacing, Boolean>()
	{
		{
			put(EnumFacing.UP, true);
			put(EnumFacing.DOWN, true);
			put(EnumFacing.NORTH, true);
			put(EnumFacing.SOUTH, true);
			put(EnumFacing.EAST, true);
			put(EnumFacing.WEST, true);
		}
	};

	@Override
	public boolean canReceive(EnumFacing side)
	{
		return sides.get(side);
	}

	@Override
	public boolean canExtract(EnumFacing side)
	{
		return !sides.get(side);
	}

	@Override
	public void setSideCanReceive(EnumFacing side, boolean value)
	{
		this.sides.put(side, value);
	}

	@Override
	public void setAllEnergySide(boolean[] sides)
	{
		this.sides.clear();
		for (EnumFacing side : EnumFacing.VALUES)
			this.sides.put(side, sides[side.getIndex()]);
	}

	@Override
	public boolean[] getAllEnergySide()
	{
		return Booleans.toArray(this.sides.values());
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES)
			nbt.setBoolean(side.getName(), this.canReceive(side));
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for (EnumFacing side : EnumFacing.VALUES)
			this.setSideCanReceive(side, nbt.getBoolean(side.getName()));
	}

}
