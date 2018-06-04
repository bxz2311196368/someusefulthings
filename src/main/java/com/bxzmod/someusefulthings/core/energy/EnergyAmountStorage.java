package com.bxzmod.someusefulthings.core.energy;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ModLoadFlag;
import com.bxzmod.someusefulthings.core.energy.side.SideEnergyFE;
import com.bxzmod.someusefulthings.core.energy.side.SideEnergyTesla;
import mekanism.common.capabilities.Capabilities;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;

public class EnergyAmountStorage implements IEnergySide
{
	protected double energyAomunt = 0.0D;
	protected double maxStorage;
	protected double maxReceive;
	protected double maxExtract;
	protected IEnergySide sideConfig;
	public EnergyRFCommon rf;
	public EnergyFECommon fe;
	public EnergyTeslaCommon tesla;
	public EnergyMekanismCommon mek;
	public EnergyEUCommon eu;

	public EnergyAmountStorage(double maxStorage, double maxReceive, double maxExtract, IEnergySide sideConfig)
	{
		this.maxStorage = maxStorage;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.sideConfig = sideConfig;
		this.rf = new EnergyRFCommon(this);
		this.fe = new EnergyFECommon(this);
		if (ModLoadFlag.isTeslaLoad)
			this.tesla = new EnergyTeslaCommon(this);
		if (ModLoadFlag.isMekLoad)
			this.mek = new EnergyMekanismCommon(this);
		if (ModLoadFlag.isIC2Load)
			this.eu = new EnergyEUCommon(this);
	}

	public EnergyAmountStorage(double maxStorage, double maxTransfer, IEnergySide sideConfig)
	{
		this(maxStorage, maxTransfer, maxTransfer, sideConfig);
	}

	public EnergyAmountStorage(double maxStorage, IEnergySide sideConfig)
	{
		this(maxStorage, maxStorage, sideConfig);
	}

	public EnergyAmountStorage(IEnergySide sideConfig)
	{
		this(Double.MAX_VALUE, sideConfig);
	}

	public double getEnergyAomunt()
	{
		return energyAomunt;
	}

	public void setEnergyAomunt(double energyAomunt)
	{
		if (energyAomunt <= 0D)
			this.energyAomunt = 0D;
		else
		{
			if (energyAomunt < this.maxStorage)
				this.energyAomunt = energyAomunt;
			else
				this.energyAomunt = this.maxStorage;
		}
		this.syncAll();
	}

	public double modifyEnergyAomunt(double energyAomunt)
	{
		this.energyAomunt += energyAomunt;
		if (this.energyAomunt < 0D)
			this.energyAomunt = 0D;
		this.syncAll();
		return this.energyAomunt;
	}

	public double receiveEnergy(double maxReceive, boolean simulate)
	{
		double energyReceived = Math.min(this.maxStorage - this.energyAomunt, Math.min(this.maxReceive, maxReceive));
		if (!simulate)
			this.modifyEnergyAomunt(energyReceived);
		return energyReceived;
	}

	public double receiveEnergy(EnumFacing side, double maxReceive, boolean simulate)
	{
		if (this.sideConfig.canReceive(side))
			return this.receiveEnergy(maxReceive, simulate);
		return 0;
	}

	public double extractEnergy(double maxExtract, boolean simulate)
	{
		double energyExtracted = Math.min(this.energyAomunt, Math.min(this.maxExtract, maxExtract));
		if (!simulate)
			this.modifyEnergyAomunt(-energyExtracted);
		return energyExtracted;
	}

	public double extractEnergy(EnumFacing side, double maxExtract, boolean simulate)
	{
		if (this.sideConfig.canExtract(side))
			return this.extractEnergy(maxExtract, simulate);
		return 0;
	}

	public double getMaxStorage()
	{
		return maxStorage;
	}

	public void setMaxStorage(double maxStorage)
	{
		this.maxStorage = maxStorage;
	}

	public EnergyAmountStorage readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey("Energy"))
			return this;
		this.energyAomunt = nbt.getInteger("Energy");
		this.maxExtract = nbt.getDouble("MaxExtract");
		this.maxReceive = nbt.getDouble("MaxReceive");
		this.maxStorage = nbt.getDouble("MaxStorage");
		if (this.energyAomunt < 0)
		{
			this.energyAomunt = 0;
		}
		this.sideConfig.deserializeNBT(nbt.getCompoundTag("sides"));
		this.syncAll();	
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (this.energyAomunt < 0)
		{
			this.energyAomunt = 0;
		}
		nbt.setDouble("Energy", this.energyAomunt);
		nbt.setDouble("MaxExtract", this.maxExtract);
		nbt.setDouble("MaxReceive", this.maxReceive);
		nbt.setDouble("MaxStorage", this.maxStorage);
		nbt.setTag("sides", this.sideConfig.serializeNBT());
		return nbt;
	}

	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (facing == null)
			return false;
		if (ModLoadFlag.isTeslaLoad)
		{
			if (TeslaCapabilities.CAPABILITY_CONSUMER.equals(capability)
					|| TeslaCapabilities.CAPABILITY_PRODUCER.equals(capability)
					|| TeslaCapabilities.CAPABILITY_HOLDER.equals(capability))
				return true;
		}
		if (ModLoadFlag.isMekLoad)
		{
			if (Capabilities.ENERGY_STORAGE_CAPABILITY.equals(capability)
					|| Capabilities.ENERGY_OUTPUTTER_CAPABILITY.equals(capability)
					|| Capabilities.ENERGY_STORAGE_CAPABILITY.equals(capability))
			{
				return true;
			}
		}
		if (CapabilityEnergy.ENERGY.equals(capability))
		{
			return true;
		}
		return false;
	}

	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (facing == null)
			return null;
		if (ModLoadFlag.isTeslaLoad)
		{
			if (TeslaCapabilities.CAPABILITY_CONSUMER.equals(capability)
					|| TeslaCapabilities.CAPABILITY_PRODUCER.equals(capability)
					|| TeslaCapabilities.CAPABILITY_HOLDER.equals(capability))
				return (T) new SideEnergyTesla(this.sideConfig.canReceive(facing), this.tesla);
		}
		if (ModLoadFlag.isMekLoad)
		{
			if (Capabilities.ENERGY_STORAGE_CAPABILITY.equals(capability)
					|| Capabilities.ENERGY_OUTPUTTER_CAPABILITY.equals(capability)
					|| Capabilities.ENERGY_ACCEPTOR_CAPABILITY.equals(capability))
			{
				return (T) this.mek;
			}
		}
		if (CapabilityEnergy.ENERGY.equals(capability))
		{
			return (T) new SideEnergyFE(this.sideConfig.canReceive(facing), this.fe);
		}
		return null;
	}

	private void syncAll()
	{
		this.rf.syncEnergy();
		this.fe.syncEnergy();
		if (ModLoadFlag.isTeslaLoad)
			this.tesla.syncEnergy();
		if (ModLoadFlag.isMekLoad)
			this.mek.syncEnergy();
		if (ModLoadFlag.isIC2Load)
			this.eu.syncEnergy();
	}

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
	public void setSideCanReceive(EnumFacing side, boolean values)
	{
		this.sideConfig.setSideCanReceive(side, values);
	}

	@Override
	public void setAllEnergySide(boolean[] sides)
	{
		this.sideConfig.setAllEnergySide(sides);
	}

	@Override
	public boolean[] getAllEnergySide()
	{
		return this.sideConfig.getAllEnergySide();
	}

	public void transferEnergy(World world, BlockPos pos)
	{
		for (EnumFacing side : EnumFacing.VALUES)
			if (this.canExtract(side) && Helper.getAdjacentTileEntity(world, pos, side) != null)
				this.transferSideEnergy(side, Helper.getAdjacentTileEntity(world, pos, side));
	}

	public void transferSideEnergy(EnumFacing side, TileEntity te)
	{
		this.rf.transferEnergy(side, te);
		this.fe.transferEnergy(side, te);
		if (ModLoadFlag.isTeslaLoad)
			this.tesla.transferEnergy(side, te);
		if (ModLoadFlag.isMekLoad)
			this.mek.transferEnergy(side, te);
	}

	public void chargeItem(IItemHandler inv)
	{
		for (int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null)
			{
				this.rf.chargeItem(stack);
				this.fe.chargeItem(stack);
				if (ModLoadFlag.isTeslaLoad)
					this.tesla.chargeItem(stack);
				if (ModLoadFlag.isMekLoad)
					this.mek.chargeItem(stack);
				if (ModLoadFlag.isIC2Load)
					this.eu.chargeItem(stack);
			}
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		
	}

}
