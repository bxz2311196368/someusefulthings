package com.bxzmod.someusefulthings.core.energy;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2") })
public class EnergyEUCommon implements IEnergySource, IEnergySink
{
	protected int sinkTier;
	protected int sourceTier;
	protected double energy;
	protected double capacity;
	protected EnergyAmountStorage storage;
	public static boolean isIC2Loaded = Loader.isModLoaded("IC2");

	public EnergyEUCommon(int sinkTier, int sourceTier, double capacity, EnergyAmountStorage storage)
	{
		this.sinkTier = sinkTier;
		this.sourceTier = sourceTier;
		this.capacity = capacity;
		this.storage = storage;
	}

	public EnergyEUCommon(EnergyAmountStorage storage)
	{
		this(Integer.MAX_VALUE, 1, Double.MAX_VALUE, storage);
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side)
	{
		return this.storage.canReceive(side);
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side)
	{
		return this.storage.canExtract(side);
	}

	@Override
	public double getOfferedEnergy()
	{
		return Math.min(Math.pow(4, this.getSourceTier()) * 8, this.storage.getEnergyAomunt() / 10.0D);
	}

	@Override
	public void drawEnergy(double amount)
	{
		this.storage.modifyEnergyAomunt(-amount * 10.0D);
	}

	@Override
	public int getSourceTier()
	{
		return this.sourceTier;
	}

	public void setSourceTier(int sourceTier)
	{
		if (sourceTier > 14)
			this.sourceTier = 14;
		else
			this.sourceTier = sourceTier;
	}

	public int roundSourceTier()
	{
		if (this.sourceTier >= 14)
			this.sourceTier = 1;
		else
			this.setSourceTier(this.sourceTier + 1);
		return this.sourceTier;
	}

	@Override
	public double getDemandedEnergy()
	{
		return this.capacity - this.energy;
	}

	@Override
	public int getSinkTier()
	{
		return this.sinkTier;
	}

	public void setSinkTier(int sinkTier)
	{
		this.sinkTier = sinkTier;
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage)
	{
		return (amount * 10.0D - this.storage.receiveEnergy(directionFrom, amount * 10.0D, false)) / 10.0D;
	}

	public double getCapacity()
	{
		return capacity;
	}

	public void setCapacity(double capacity)
	{
		this.capacity = capacity;
	}

	public void syncEnergy()
	{
		this.energy = this.storage.getEnergyAomunt() / 10.0D;
	}

	public void chargeItem(ItemStack stack)
	{
		if (isIC2Loaded)
			this.chargeEUItem(stack);
	}

	@Optional.Method(modid = "IC2")
	private void chargeEUItem(ItemStack stack)
	{
		if (ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, 14, true, true) > 0.0)
			this.drawEnergy(ElectricItem.manager.charge(stack, this.energy, 14, true, false));
	}

}
