package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.blocks.property.EnumIO;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface IConfigSide
{
	public EnumIO getSideIO(EnumFacing side);

	public EnumIO setSideIO(EnumFacing side, EnumIO io);

	public EnumIO cycleSideIO(EnumFacing side);

	public NBTTagCompound getNBTFromConfig(NBTTagCompound nbt);

	public void getConfigFromNBT(NBTTagCompound nbt);

	public EnumFacing getFacing();

	public void setFacing(EnumFacing facing);

	public EnumFacing rotateY();
}
