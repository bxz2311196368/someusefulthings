package com.bxzmod.someusefulthings.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;

public class InfiniteWaterTileEntity extends TileFluidHandler implements ITickable
{
	public FluidStack fluid = new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE);

	public InfiniteWaterTileEntity()
	{
		tank = new FluidTank(new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE), Integer.MAX_VALUE)
		{

			@Override
			public int fill(FluidStack resource, boolean doFill)
			{
				return 0;
			}

			@Override
			public int fillInternal(FluidStack resource, boolean doFill)
			{
				return 0;
			}

			@Override
			public boolean canFill()
			{
				return false;
			}

			@Override
			public boolean canFillFluidType(FluidStack fluid)
			{
				return false;
			}
		};
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		fillFluidTo();
		tank.getFluid().amount = Integer.MAX_VALUE;
	}

	public FluidTank getTank()
	{
		return tank;
	}

	public void fillFluidTo()
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos pos = this.pos.offset(facing);
			TileEntity te = world.getTileEntity(pos);
			fluid.amount = Integer.MAX_VALUE;
			int amount = 0;
			if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
				te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).fill(fluid,
						true);

		}
	}

}
