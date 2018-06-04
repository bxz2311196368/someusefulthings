package com.bxzmod.someusefulthings.core.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergySide extends INBTSerializable<NBTTagCompound>
{
	public boolean canReceive(EnumFacing side);

	public boolean canExtract(EnumFacing side);

	public void setSideCanReceive(EnumFacing side, boolean values);

	public void setAllEnergySide(boolean[] sides);
	
	public boolean[] getAllEnergySide();
}
