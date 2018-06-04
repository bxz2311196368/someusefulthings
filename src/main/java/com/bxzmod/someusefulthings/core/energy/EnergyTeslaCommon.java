package com.bxzmod.someusefulthings.core.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla"),
		@Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaHolder", modid = "tesla"),
		@Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaProducer", modid = "tesla") })
public class EnergyTeslaCommon implements ITeslaConsumer, ITeslaProducer, ITeslaHolder
{
	protected long stored;
	protected long capacity;
	protected long inputRate;
	protected long outputRate;
	protected EnergyAmountStorage storage;
	public static boolean isTeslaLoaded = Loader.isModLoaded("tesla");

	public EnergyTeslaCommon(long capacity, long inputRate, long outputRate, EnergyAmountStorage storage)
	{
		this.capacity = capacity;
		this.inputRate = inputRate;
		this.outputRate = outputRate;
		this.storage = storage;
	}

	public EnergyTeslaCommon(long capacity, long transferRate, EnergyAmountStorage storage)
	{
		this(capacity, transferRate, transferRate, storage);
	}

	public EnergyTeslaCommon(long capacity, EnergyAmountStorage storage)
	{
		this(capacity, capacity, storage);
	}

	public EnergyTeslaCommon(EnergyAmountStorage storage)
	{
		this(Long.MAX_VALUE, storage);
	}

	@Override
	public long getStoredPower()
	{
		return this.stored;
	}

	@Override
	public long getCapacity()
	{
		return this.capacity;
	}

	public long givePower(long Tesla, boolean simulate)
	{
		return MathHelper.lfloor(this.storage.receiveEnergy(Tesla * 2.5D, simulate) / 2.5D);
	}

	public long takePower(long Tesla, boolean simulate)
	{
		return MathHelper.lfloor(this.storage.extractEnergy(Tesla * 2.5D, simulate) / 2.5D);
	}

	public void syncEnergy()
	{
		if (this.storage.getEnergyAomunt() / 2.5D >= this.capacity)
			this.stored = this.capacity;
		else
			this.stored = MathHelper.lfloor(this.storage.getEnergyAomunt() / 2.5D);
	}

	public void transferEnergy(EnumFacing side, TileEntity te)
	{
		if (isTeslaLoaded)
			this.transferTeslaEnergy(side, te);
	}

	public void chargeItem(ItemStack stack)
	{
		if (isTeslaLoaded)
			this.chargeTeslaItem(stack);
	}

	@Optional.Method(modid = "tesla")
	private void transferTeslaEnergy(EnumFacing side, TileEntity te)
	{
		if (te.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, side))
			this.takePower(te.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, side)
					.givePower(this.takePower(this.outputRate, true), false), false);
		else if (te instanceof ITeslaConsumer)
			this.takePower(((ITeslaConsumer) te).givePower(this.takePower(this.outputRate, true), false), false);
	}

	@Optional.Method(modid = "tesla")
	private void chargeTeslaItem(ItemStack stack)
	{
		if (stack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null))
			this.takePower(stack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null)
					.givePower(this.takePower(this.outputRate, true), false), false);
	}
}
