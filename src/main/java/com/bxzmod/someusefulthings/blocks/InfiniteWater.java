package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.tileentity.InfiniteWaterTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class InfiniteWater extends BaseBlockContainer
{

	public InfiniteWater()
	{
		super("infinite_water", "infiniteWater");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new InfiniteWaterTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && playerIn.getHeldItemMainhand() != null
				&& playerIn.getHeldItemMainhand().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
		{
			FluidStack fluidWater = new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE);
			ItemStack stack = playerIn.getHeldItemMainhand();
			IFluidHandler fluid = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidStack fluidInStack;
			if (stack.stackSize == 1)
				fluidInStack = fluid.drain(Integer.MAX_VALUE, false);
			else
				fluidInStack = fluid.getTankProperties()[0].getContents();
			if (fluidInStack == null)
			{
				if (stack.stackSize == 1)
					playerIn.getHeldItemMainhand().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
							.fill(fluidWater, true);
				else
				{
					ItemStack copy_stack = Helper.copyStack(stack);
					copy_stack.stackSize = 1;
					copy_stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(fluidWater,
							true);
					copy_stack.stackSize = stack.stackSize;
					playerIn.setHeldItem(EnumHand.MAIN_HAND, copy_stack);
				}
			}
		}
		return true;
	}

}
