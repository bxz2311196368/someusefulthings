package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.blocks.property.EnumIO;
import com.bxzmod.someusefulthings.core.energy.AbstractEnergyTileEntity;
import com.bxzmod.someusefulthings.core.energy.EnergyAmountStorage;
import net.minecraft.util.EnumFacing;

public class EnergyBlockTileEntity extends AbstractEnergyTileEntity
{
	public EnergyBlockTileEntity()
	{
		super(new DefaultSide()
		{
			@Override
			public EnumIO cycleSideIO(EnumFacing side)
			{
				if (this.getSideIO(side).equals(EnumIO.INPUT))
					return this.setSideIO(side, EnumIO.OUTPUT);
				return this.setSideIO(side, EnumIO.INPUT);
			}
		});
		this.storage = new EnergyAmountStorage(this.configSide);
	}

}
