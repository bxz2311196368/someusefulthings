package com.bxzmod.someusefulthings.core.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyFECommon implements IEnergyStorage
{

	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	protected EnergyAmountStorage storage;

	public EnergyFECommon(int capacity, int maxReceive, int maxExtract, EnergyAmountStorage storage)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.storage = storage;
	}

	public EnergyFECommon(int capacity, int maxTransfer, EnergyAmountStorage storage)
	{
		this(capacity, maxTransfer, maxTransfer, storage);
	}

	public EnergyFECommon(int capacity, EnergyAmountStorage storage)
	{
		this(capacity, capacity, storage);
	}

	public EnergyFECommon(EnergyAmountStorage storage)
	{
		this(Integer.MAX_VALUE, storage);
	}

	public void setEnergyStored(int energy)
	{
		this.storage.setEnergyAomunt(energy);
	}

	public void modifyEnergyStored(int energy)
	{
		this.storage.modifyEnergyAomunt(energy);
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

	@Override
	public boolean canExtract()
	{
		return true;
	}

	@Override
	public boolean canReceive()
	{
		return true;
	}

	public void syncEnergy()
	{
		if (this.storage.getEnergyAomunt() / 2.5D >= this.capacity)
			this.energy = this.capacity;
		else
			this.energy = MathHelper.floor(this.storage.getEnergyAomunt() / 2.5D);
	}

	public void transferEnergy(EnumFacing side, TileEntity te)
	{
		if (te.hasCapability(CapabilityEnergy.ENERGY, side))
			this.extractEnergy(te.getCapability(CapabilityEnergy.ENERGY, side)
					.receiveEnergy(this.extractEnergy(maxExtract, true), false), true);
		else if (te instanceof IEnergyStorage)
			this.extractEnergy(((IEnergyStorage) te).receiveEnergy(this.extractEnergy(this.maxExtract, true), false),
					true);
	}

	public void chargeItem(ItemStack stack)
	{
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
			this.extractEnergy(stack.getCapability(CapabilityEnergy.ENERGY, null)
					.receiveEnergy(this.extractEnergy(maxExtract, true), false), true);
	}

}
