package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.IConfigSide;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.blocks.property.EnumIO;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity implements IConfigSide, ITickable
{
	protected ItemStackHandlerModify iInventory;
	protected IConfigSide configSide;
	private EnumFacing facing = EnumFacing.NORTH;

	public TileEntityBase(ItemStackHandlerModify iInventory, IConfigSide configSide)
	{
		super();
		this.iInventory = iInventory;
		this.configSide = configSide;
		this.iInventory.setConfigSide(this.configSide);
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			try
			{
				this.work();
				this.iInventory.tryInAndOut(this.world, this.pos);
			} catch (Exception e)
			{
				e.printStackTrace();
				this.invalidate();
			}
			this.markDirty();
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, state, state, 4);
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
		try
		{
			super.readFromNBT(compound);
			this.setDataFromNBT(compound);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		try
		{
			this.setNBTFromData(super.writeToNBT(compound));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public EnumIO getSideIO(EnumFacing side)
	{
		return this.configSide.getSideIO(side);
	}

	@Override
	public EnumIO setSideIO(EnumFacing side, EnumIO io)
	{
		return this.configSide.setSideIO(side, io);
	}

	@Override
	public EnumIO cycleSideIO(EnumFacing side)
	{
		return this.configSide.cycleSideIO(side);
	}

	@Override
	public NBTTagCompound getNBTFromConfig(NBTTagCompound nbt)
	{
		return this.configSide.getNBTFromConfig(nbt);
	}

	@Override
	public void getConfigFromNBT(NBTTagCompound nbt)
	{
		this.configSide.getConfigFromNBT(nbt);
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

	public ItemStackHandlerModify getiInventory()
	{
		return iInventory;
	}

	@Override
	public EnumFacing getFacing()
	{
		return this.configSide.getFacing();
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.configSide.setFacing(facing);
	}

	@Override
	public EnumFacing rotateY()
	{
		return this.configSide.rotateY();
	}

}
