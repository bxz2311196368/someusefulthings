package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.blocks.property.EnumIO;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

public class DefaultSide implements IConfigSide
{
	private HashMap<EnumFacing, EnumIO> sideIO = Maps.newHashMap();
	private EnumFacing facing = EnumFacing.NORTH;

	public DefaultSide()
	{
		for (EnumFacing side : EnumFacing.VALUES)
			this.setSideIO(side, EnumIO.INPUT);
	}

	@Override
	public EnumIO getSideIO(EnumFacing side)
	{
		return this.sideIO.get(this.getRealSide(side));
	}

	@Override
	public EnumIO setSideIO(EnumFacing side, EnumIO io)
	{
		EnumFacing realSide = this.getRealSide(side);
		this.sideIO.put(realSide, io);
		return this.getSideIO(realSide);
	}

	@Override
	public EnumIO cycleSideIO(EnumFacing side)
	{
		EnumFacing realSide = this.getRealSide(side);
		return this.setSideIO(realSide, this.getSideIO(realSide).next());
	}

	@Override
	public NBTTagCompound getNBTFromConfig(NBTTagCompound nbt)
	{
		NBTTagCompound list = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES)
			list.setString(side.getName(), this.sideIO.get(side).getName());
		nbt.setTag("Sides", list);
		nbt.setInteger("facing", this.facing.getIndex());
		return nbt;
	}

	@Override
	public void getConfigFromNBT(NBTTagCompound nbt)
	{
		NBTTagCompound list = nbt.getCompoundTag("Sides");
		for (EnumFacing side : EnumFacing.VALUES)
			this.sideIO.put(side, EnumIO.getIOByName(list.getString(side.getName())));
		this.facing = EnumFacing.VALUES[nbt.getInteger("facing")];
		if (this.facing.getHorizontalIndex() <= -1)
			this.facing = EnumFacing.NORTH;
	}

	@Override
	public EnumFacing getFacing()
	{
		return this.facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public EnumFacing rotateY()
	{
		this.facing = this.facing.rotateY();
		return this.facing;
	}

	private EnumFacing getRealSide(EnumFacing side)
	{
		if (side.getHorizontalIndex() > -1)
			for (EnumFacing temp = this.facing; temp != EnumFacing.NORTH; temp = temp.rotateYCCW())
				side = side.rotateYCCW();
		return side;
	}
}
