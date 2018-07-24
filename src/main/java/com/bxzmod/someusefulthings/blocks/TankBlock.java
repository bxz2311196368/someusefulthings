package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.tileentity.TankTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankBlock extends BaseBlockContainer
{

	public TankBlock()
	{
		super("tank", "fluidTank");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{

		return new TankTileEntity();
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.025F);
		ItemStack itemstack = this.getSilkTouchDrop(state);
		if (te != null && te instanceof TankTileEntity)
			itemstack.setTagCompound(((TankTileEntity) te).getTank().writeToNBT(new NBTTagCompound()));
		spawnAsEntity(worldIn, pos, itemstack);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && stack.getTagCompound() != null && te instanceof TankTileEntity)
			((TankTileEntity) te).getTank().readFromNBT(stack.getTagCompound());
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (!worldIn.isRemote)
			if (te != null && te instanceof TankTileEntity)
				if (playerIn.getHeldItemMainhand() == null)
					if (((TankTileEntity) te).getTank().getFluid() != null)
						playerIn.sendMessage(new TextComponentTranslation("tank.msg_0",
								((TankTileEntity) te).getTank().getFluid().getLocalizedName(),
								((TankTileEntity) te).getTank().getFluidAmount()));
					else
						playerIn.sendMessage(new TextComponentTranslation("tank.msg_1"));
				else
				{
					if (playerIn.getHeldItemMainhand().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
							null))
					{
						ItemStack stack = playerIn.getHeldItemMainhand();
						IFluidHandler fluid = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
								null);
						FluidStack fluidInTank = ((TankTileEntity) te).getTank().getFluid();
						FluidStack fluidInStack;
						if (stack.stackSize == 1)
							fluidInStack = fluid.drain(Integer.MAX_VALUE, false);
						else
							fluidInStack = fluid.getTankProperties()[0].getContents();
						if (fluidInTank == null && fluidInStack == null)
							return true;
						else if (fluidInTank == null)
						{
							if (stack.stackSize == 1)
							{
								int temp = ((TankTileEntity) te).getTank().fill(fluidInStack, true);
								playerIn.getHeldItemMainhand()
										.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
										.drain(temp, true);
							} else
							{
								ItemStack copy_stack = Helper.copyStack(stack);
								copy_stack.stackSize = 1;
								IFluidHandler fluid_temp = copy_stack
										.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
								FluidStack fluidInCopyStack = fluid_temp.drain(Integer.MAX_VALUE, false);
								int temp = ((TankTileEntity) te).getTank().fill(fluidInCopyStack, true);
								if (temp > 0)
								{
									copy_stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
											.drain(temp, true);
									;
									stack.stackSize--;
									if (copy_stack != null)
										if (!playerIn.inventory.addItemStackToInventory(copy_stack))
											playerIn.world.spawnEntity(new EntityItem(playerIn.getEntityWorld(),
													playerIn.posX, playerIn.posY, playerIn.posZ, copy_stack));
								}
							}

						} else if (fluidInStack == null)
						{
							if (stack.stackSize == 1)
							{
								int temp = fluid.fill(fluidInTank, false);
								playerIn.getHeldItemMainhand()
										.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
										.fill(fluidInTank, true);
								((TankTileEntity) te).getTank().drain(temp, true);
							} else
							{
								ItemStack copy_stack = Helper.copyStack(stack);
								copy_stack.stackSize = 1;
								IFluidHandler fluid_temp = copy_stack
										.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
								int temp = fluid_temp.fill(fluidInTank, false);
								if (temp > 0)
								{
									fluid_temp.fill(fluidInTank, true);
									((TankTileEntity) te).getTank().drain(temp, true);
									stack.stackSize--;
									if (copy_stack != null)
										if (!playerIn.inventory.addItemStackToInventory(copy_stack))
											playerIn.world.spawnEntity(new EntityItem(playerIn.getEntityWorld(),
													playerIn.posX, playerIn.posY, playerIn.posZ, copy_stack));
								}
							}
						} else
						{
							if (fluidInTank.getFluid() != fluidInStack.getFluid())
								return true;
							else
							{
								if (stack.stackSize == 1)
								{
									int temp = Math.min(Integer.MAX_VALUE - fluidInTank.amount, fluidInStack.amount);
									FluidStack fluidtemp = playerIn.getHeldItemMainhand()
											.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
											.drain(temp, true);
									((TankTileEntity) te).getTank().fill(fluidtemp, true);
								} else
								{
									ItemStack copy_stack = Helper.copyStack(stack);
									copy_stack.stackSize = 1;
									IFluidHandler fluid_temp = copy_stack
											.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
									FluidStack fluidInCopyStack = fluid_temp.drain(Integer.MAX_VALUE, false);
									int temp = Math.min(Integer.MAX_VALUE - fluidInTank.amount,
											fluidInCopyStack.amount);
									FluidStack fluidtemp = copy_stack
											.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
											.drain(temp, true);
									((TankTileEntity) te).getTank().fill(fluidtemp, true);
									stack.stackSize--;
									if (copy_stack != null)
										if (!playerIn.inventory.addItemStackToInventory(copy_stack))
											playerIn.world.spawnEntity(new EntityItem(playerIn.getEntityWorld(),
													playerIn.posX, playerIn.posY, playerIn.posZ, copy_stack));
								}

							}
						}
					}
				}
		return true;
	}
}
