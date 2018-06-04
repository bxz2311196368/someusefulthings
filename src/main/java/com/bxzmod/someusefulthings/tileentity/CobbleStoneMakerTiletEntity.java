package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CobbleStoneMakerTiletEntity extends TileEntity implements ITickable
{
	public boolean make_stone = false;

	protected ItemStackHandler iInventory = new ItemStackHandler(1)
	{

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			return stack;
		}

	};

	public void change()
	{
		make_stone = !make_stone;
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (make_stone)
				iInventory.setStackInSlot(0, new ItemStack(Item.getItemFromBlock(Blocks.STONE), 64, 0));
			else
				iInventory.setStackInSlot(0, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64, 0));
			if (world.getTileEntity(pos.offset(facing)) != null && world.getTileEntity(pos.offset(facing))
					.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
			{
				IItemHandler handler = world.getTileEntity(pos.offset(facing))
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				Helper.mergeItemStack(iInventory.getStackInSlot(0), 0, handler.getSlots(), false, handler);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.iInventory.deserializeNBT(compound.getCompoundTag("iInventory"));
		this.make_stone = compound.getBoolean("type");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		compound.setBoolean("type", make_stone);
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			T result = (T) iInventory;
			return result;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

}
