package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.core.energy.AbstractEnergyTileEntity;

import net.minecraft.util.EnumFacing;

public class EnergyBlockTileEntity extends AbstractEnergyTileEntity
{

	@Override
	public void setSideCanReceive(EnumFacing side, boolean values)
	{
		this.sideConfig.setSideCanReceive(side, values);
	}

	@Override
	public void setAllEnergySide(boolean[] sides)
	{
		this.sideConfig.setAllEnergySide(sides);
	}

}
