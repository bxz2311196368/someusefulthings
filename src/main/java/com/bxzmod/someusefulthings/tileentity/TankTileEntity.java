package com.bxzmod.someusefulthings.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;

public class TankTileEntity extends TileFluidHandler implements ITickable
{

	public TankTileEntity()
	{
		tank = new FluidTank(Integer.MAX_VALUE);
	}

	@Override
	public void update()
	{
		fillFluidTo();
		checkEmpty();

	}

	public void fillFluidTo()
	{
		BlockPos pos = this.pos.up();
		TileEntity te = world.getTileEntity(pos);
		FluidStack fluid = this.tank.getFluid();
		int amount = 0;
		if (te != null && fluid != null)
			if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
				amount = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN).fill(fluid,
						true);
		if (amount > 0)
			tank.getFluid().amount -= amount;
	}

	public void checkEmpty()
	{
		FluidStack fluid = this.tank.getFluid();
		if (fluid != null && fluid.amount == 0)
			this.tank.setFluid(null);
	}

	public FluidTank getTank()
	{
		return tank;
	}
}
