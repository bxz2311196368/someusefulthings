package com.bxzmod.someusefulthings.core.energy;

import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.api.energy.IStrictEnergyStorage;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = {
		@Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyAcceptor", modid = "Mekanism"),
		@Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyOutputter", modid = "Mekanism"),
		@Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyStorage", modid = "Mekanism") })
public class EnergyMekanismCommon implements IStrictEnergyStorage, IStrictEnergyOutputter, IStrictEnergyAcceptor
{
	protected double energy;
	protected double capacity;
	protected double maxReceive;
	protected double maxExtract;
	protected EnergyAmountStorage storage;
	public static boolean isMekLoaded = Loader.isModLoaded("Mekanism");

	public EnergyMekanismCommon(double capacity, double maxReceive, double maxExtract, EnergyAmountStorage storage)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.storage = storage;
	}

	public EnergyMekanismCommon(double capacity, double maxTransfer, EnergyAmountStorage storage)
	{
		this(capacity, maxTransfer, maxTransfer, storage);
	}

	public EnergyMekanismCommon(double capacity, EnergyAmountStorage storage)
	{
		this(capacity, capacity, storage);
	}

	public EnergyMekanismCommon(EnergyAmountStorage storage)
	{
		this(Double.MAX_VALUE, storage);
	}

	@Override
	public double acceptEnergy(EnumFacing side, double amount, boolean simulate)
	{
		return this.storage.receiveEnergy(side, amount, simulate);
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side)
	{
		return this.storage.getSideIO(side).canInput();
	}

	@Override
	public boolean canOutputEnergy(EnumFacing side)
	{
		return this.storage.getSideIO(side).canOutput();
	}

	@Override
	public double pullEnergy(EnumFacing side, double amount, boolean simulate)
	{
		return this.storage.extractEnergy(side, amount, simulate);
	}

	@Override
	public double getEnergy()
	{
		return this.storage.getEnergyAomunt();
	}

	@Override
	public double getMaxEnergy()
	{
		return this.capacity;
	}

	@Override
	public void setEnergy(double energy)
	{
		this.storage.setEnergyAomunt(energy);
		this.energy = energy;
	}

	public void syncEnergy()
	{
		this.energy = this.storage.getEnergyAomunt();
	}

	public void transferEnergy(EnumFacing side, TileEntity te)
	{
		if (isMekLoaded)
			this.transferMekEnergy(side, te);
	}

	public void chargeItem(ItemStack stack)
	{
		if (isMekLoaded)
			this.chargeMekItem(stack);
	}

	@Optional.Method(modid = "Mekanism")
	private void transferMekEnergy(EnumFacing side, TileEntity te)
	{
		if (te.hasCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side))
			this.pullEnergy(side, te.getCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side).acceptEnergy(side,
					this.pullEnergy(side, this.maxExtract, true), false), false);
		else if (te instanceof IStrictEnergyAcceptor)
			this.pullEnergy(side, ((IStrictEnergyAcceptor) te).acceptEnergy(side,
					this.pullEnergy(side, this.maxExtract, true), false), false);
	}

	@Optional.Method(modid = "Mekanism")
	private void chargeMekItem(ItemStack stack)
	{
		if (stack.hasCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, null))
			this.pullEnergy(null, stack.getCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, null).acceptEnergy(null,
					this.pullEnergy(null, this.maxExtract, true), false), false);
	}

}
