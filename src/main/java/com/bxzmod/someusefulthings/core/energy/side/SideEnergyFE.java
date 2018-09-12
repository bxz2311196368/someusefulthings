package com.bxzmod.someusefulthings.core.energy.side;

import com.bxzmod.someusefulthings.core.energy.EnergyFECommon;
import net.minecraftforge.energy.IEnergyStorage;

public class SideEnergyFE implements IEnergyStorage
{
	private boolean canReceive;
	private EnergyFECommon fe;

	public SideEnergyFE(boolean canReceive, EnergyFECommon fe)
	{
		this.canReceive = canReceive;
		this.fe = fe;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return this.canReceive ? this.fe.receiveEnergy(maxReceive, simulate) : 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return this.canReceive ? 0 : this.fe.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored()
	{
		return this.fe.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored()
	{
		return this.fe.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract()
	{
		return !this.canReceive;
	}

	@Override
	public boolean canReceive()
	{
		return this.canReceive;
	}

}
