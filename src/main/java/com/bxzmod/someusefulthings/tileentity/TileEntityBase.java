package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.ItemStackHandlerModify;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityBase extends TileEntity implements ITickable
{
	protected ItemStackHandlerModify iInventory;

	public TileEntityBase(ItemStackHandlerModify iInventory)
	{
		super();
		this.iInventory = iInventory;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			try
			{
				this.iInventory.tryInAndOut(this.world, this.pos);
				this.work();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			this.markDirty();
		}
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

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.setDataFromNBT(compound);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		return this.setNBTFromData(super.writeToNBT(compound));
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.setNBTFromData(super.getUpdateTag());
	}

	public abstract void work();

	public void setDataFromNBT(NBTTagCompound compound)
	{
		this.iInventory.deserializeNBT(compound.getCompoundTag("iInventory"));
	}

	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		return compound;
	}
}
