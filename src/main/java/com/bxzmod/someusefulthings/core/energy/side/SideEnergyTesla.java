package com.bxzmod.someusefulthings.core.energy.side;

import com.bxzmod.someusefulthings.core.energy.EnergyTeslaCommon;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;

public class SideEnergyTesla implements ITeslaConsumer, ITeslaProducer, ITeslaHolder
{
	private boolean canReceive;
	private EnergyTeslaCommon tesla;

	public SideEnergyTesla(boolean canReceive, EnergyTeslaCommon tesla)
	{
		this.canReceive = canReceive;
		this.tesla = tesla;
	}

	@Override
	public long getCapacity()
	{
		return this.tesla.getCapacity();
	}

	@Override
	public long getStoredPower()
	{
		return this.tesla.getStoredPower();
	}

	@Override
	public long takePower(long Tesla, boolean simulate)
	{
		return this.canReceive ? this.tesla.takePower(Tesla, simulate) : 0;
	}

	@Override
	public long givePower(long Tesla, boolean simulate)
	{
		return this.canReceive ? 0 : this.tesla.givePower(Tesla, simulate);
	}

}
