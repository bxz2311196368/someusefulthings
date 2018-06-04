package com.bxzmod.someusefulthings.core.energy;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class EnergyRFCommon implements IEnergyStorage, IEnergyReceiver, IEnergyProvider
{
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	protected EnergyAmountStorage storage;

	public EnergyRFCommon(int capacity, int maxReceive, int maxExtract, EnergyAmountStorage storage)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.storage = storage;
	}

	public EnergyRFCommon(int capacity, int maxTransfer, EnergyAmountStorage storage)
	{
		this(capacity, maxTransfer, maxTransfer, storage);
	}

	public EnergyRFCommon(int capacity, EnergyAmountStorage storage)
	{
		this(capacity, capacity, capacity, storage);
	}

	public EnergyRFCommon(EnergyAmountStorage storage)
	{
		this(Integer.MAX_VALUE, storage);
	}

	public EnergyRFCommon setMaxTransfer(int maxTransfer)
	{
		this.setMaxReceive(maxTransfer);
		this.setMaxExtract(maxTransfer);
		return this;
	}

	public EnergyRFCommon setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
		return this;
	}

	public EnergyRFCommon setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
		return this;
	}

	public int getMaxReceive()
	{
		return this.maxReceive;
	}

	public int getMaxExtract()
	{
		return this.maxExtract;
	}

	public void setEnergyStored(int energy)
	{
		this.storage.setEnergyAomunt(energy * 2.5D);
	}

	public void modifyEnergyStored(int energy)
	{
		this.storage.modifyEnergyAomunt(energy * 2.5D);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return MathHelper.floor(this.storage.receiveEnergy(maxReceive * 2.5D, simulate) / 2.5D);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return MathHelper.floor(this.storage.extractEnergy(maxExtract * 2.5D, simulate) / 2.5D);
	}

	@Override
	public int getEnergyStored()
	{
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return this.capacity;
	}

	public void syncEnergy()
	{
		if (this.storage.getEnergyAomunt() / 2.5D >= this.capacity)
			this.energy = this.capacity;
		else
			this.energy = MathHelper.floor(this.storage.getEnergyAomunt() / 2.5D);
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return this.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return this.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return MathHelper.floor(this.storage.extractEnergy(from, maxExtract * 2.5D, simulate) / 2.5D);
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return MathHelper.floor(this.storage.receiveEnergy(from, maxReceive * 2.5D, simulate) / 2.5D);
	}

	public void transferEnergy(EnumFacing side, TileEntity te)
	{
		if (te instanceof IEnergyReceiver)
			this.extractEnergy(
					((IEnergyReceiver) te).receiveEnergy(side, this.extractEnergy(this.maxExtract, true), false),
					false);
	}

	public void chargeItem(ItemStack stack)
	{
		if (stack.getItem() instanceof IEnergyContainerItem)
			this.extractEnergy(((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack,
					this.extractEnergy(this.maxExtract, true), false), false);
	}

}
