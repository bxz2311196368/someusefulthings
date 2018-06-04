package com.bxzmod.someusefulthings.core.energy;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.ModLoadFlag;
import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import com.google.common.primitives.Booleans;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2") })
public abstract class AbstractEnergyTileEntity extends TileEntityBase
		implements ITickable, IEnergySide, IEnergyReceiver, IEnergyProvider, IEnergySink, IEnergySource
{
	public AbstractEnergyTileEntity()
	{
		super(new ItemStackHandlerModify(9).setAllInputSlotChecker(stack -> Helper.isEnergyStack(stack))
				.setAllOutputSlotChecker(stack -> Helper.isFullEnergyStack(stack)).getInventory());
	}

	protected IEnergySide sideConfig = new DefaultSide();
	protected EnergyAmountStorage storage = new EnergyAmountStorage(this.sideConfig);
	protected boolean addedToEUnet = false;

	@Override
	public void work()
	{
		if (!this.addedToEUnet && ModLoadFlag.isIC2Load)
			this.euNetLoad();
		this.storage.transferEnergy(this.world, this.pos);
		this.storage.chargeItem(this.iInventory);
	}

	@Override
	public void setDataFromNBT(NBTTagCompound compound)
	{
		super.setDataFromNBT(compound);
		this.storage.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		return this.storage.writeToNBT(super.setNBTFromData(compound));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return this.storage.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (this.storage.hasCapability(capability, facing))
			return this.storage.getCapability(capability, facing);
		return super.getCapability(capability, facing);
	}

	// RF Start
	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return this.storage.rf.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return this.storage.rf.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return this.storage.rf.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return this.storage.rf.getMaxEnergyStored();
	}
	// RF end

	// IC2 Start
	@Optional.Method(modid = "IC2")
	public void euNetLoad()
	{
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		this.addedToEUnet = true;
	}

	@Optional.Method(modid = "IC2")
	public void euNetUnload()
	{
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
		this.addedToEUnet = false;
	}

	@Optional.Method(modid = "IC2")
	private void refreshEuNet()
	{
		euNetUnload();
		euNetLoad();
	}

	@Override
	public void invalidate()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer() && ModLoadFlag.isIC2Load)
			this.euNetUnload();
		super.invalidate();
	}

	@Override
	public void validate()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer() && ModLoadFlag.isIC2Load)
			this.euNetLoad();
		super.validate();
	}

	@Override
	public void drawEnergy(double amount)
	{
		this.storage.eu.drawEnergy(amount);
	}

	@Override
	public double getOfferedEnergy()
	{
		return this.storage.eu.getOfferedEnergy();
	}

	@Override
	public int getSourceTier()
	{
		return this.storage.eu.getSourceTier();
	}

	@Override
	public double getDemandedEnergy()
	{
		return this.storage.eu.getDemandedEnergy();
	}

	@Override
	public int getSinkTier()
	{
		return this.storage.eu.getSinkTier();
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage)
	{
		return this.storage.eu.injectEnergy(directionFrom, amount, voltage);
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side)
	{
		return this.canReceive(side);
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side)
	{
		return this.canExtract(side);
	}
	// IC2 End

	@Override
	public boolean canReceive(EnumFacing side)
	{
		return this.sideConfig.canReceive(side);
	}

	@Override
	public boolean canExtract(EnumFacing side)
	{
		return this.sideConfig.canExtract(side);
	}
	
	@Override
	public boolean[] getAllEnergySide()
	{
		return this.sideConfig.getAllEnergySide();
	}

}
