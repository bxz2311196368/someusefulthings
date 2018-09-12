package com.bxzmod.someusefulthings.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.blocks.EyeGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Iterator;

public class EyeGeneratorTileEntity extends TileEntity implements ITickable, IEnergyProvider
{

	private int worktime = 1;

	private boolean canWork = false;

	private static final int MaxEnergyStored = 1000000;

	protected cofh.api.energy.EnergyStorage rfStorage = new cofh.api.energy.EnergyStorage(MaxEnergyStored);
	protected EnergyStorageTweak forgeStorage = new EnergyStorageTweak(MaxEnergyStored);

	public void work()
	{
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) this.world.getEntitiesWithinAABB(EntityPlayer.class,
				new AxisAlignedBB(pos).expand(16, 16, 16));
		canWork = false;
		if (players.isEmpty())
			return;
		for (Iterator<EntityPlayer> it = players.iterator(); it.hasNext();)
		{
			EntityPlayer p = it.next();
			RayTraceResult result = Helper.raytraceFromEntity(world, p, true, 16);
			if (result != null && result.getBlockPos().equals(pos))
			{
				canWork = true;
				return;
			}
		}
	}

	private void sendEnergy()
	{
		for (int i = 0; i < 6; i++)
		{
			if (rfStorage.getEnergyStored() == 0)
				break;
			EnumFacing side = EnumFacing.getFront(i);
			TileEntity handler = getAdjacentTileEntity(getWorld(), getPos(), side);
			if (handler == null)
				continue;
			int lose = 0;
			if (handler instanceof IEnergyReceiver)
				lose += ((IEnergyReceiver) handler).receiveEnergy(side.getOpposite(), rfStorage.getEnergyStored(),
						false);
			if (handler.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite()))
				lose += handler.getCapability(CapabilityEnergy.ENERGY, side.getOpposite())
						.receiveEnergy(rfStorage.getEnergyStored(), false);
			rfStorage.extractEnergy(lose, false);
			forgeStorage.extractEnergy(lose, false);
		}
	}

	public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, EnumFacing dir)
	{

		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability.equals(CapabilityEnergy.ENERGY))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability.equals(CapabilityEnergy.ENERGY))
			return (T) this.forgeStorage;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		rfStorage.setEnergyStored(compound.getInteger("Energy"));
		forgeStorage.setEnergyStored(compound.getInteger("Energy"));

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("Energy", rfStorage.getEnergyStored());
		return compound;
	}

	@Override
	public void update()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		IBlockState state = world.getBlockState(pos);
		work();
		if (canWork)
		{
			world.setBlockState(pos, state.withProperty(EyeGenerator.WORK, Boolean.TRUE));
			this.rfStorage.modifyEnergyStored(worktime++);
			this.forgeStorage.setEnergyStored(rfStorage.getEnergyStored());
		} else
		{
			world.setBlockState(pos, state.withProperty(EyeGenerator.WORK, Boolean.FALSE));
			this.worktime = 1;
		}
		sendEnergy();
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{

		return rfStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{

		return rfStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{

		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{

		return rfStorage.extractEnergy(maxExtract, simulate);
	}

	private class EnergyStorageTweak extends EnergyStorage
	{

		public EnergyStorageTweak(int capacity)
		{
			super(capacity);
		}

		public void modifyEnergyStored(int energy)
		{

			this.energy += energy;

			if (this.energy > capacity)
			{
				this.energy = capacity;
			} else if (this.energy < 0)
			{
				this.energy = 0;
			}
		}

		public void setEnergyStored(int energy)
		{

			this.energy = energy;

			if (this.energy > capacity)
			{
				this.energy = capacity;
			} else if (this.energy < 0)
			{
				this.energy = 0;
			}
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			return 0;
		}

		@Override
		public boolean canReceive()
		{
			return false;
		}

	}
}
