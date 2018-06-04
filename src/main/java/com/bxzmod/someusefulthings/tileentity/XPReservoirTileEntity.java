package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.fluid.FluidLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class XPReservoirTileEntity extends TileFluidHandler implements ITickable
{
	private int xp = 0;

	public XPReservoirTileEntity()
	{
		this.tank = new FluidTank(new FluidStack(FluidLoader.fluidXP, 0), Integer.MAX_VALUE - 8)
		{

			@Override
			public boolean canFillFluidType(FluidStack fluid)
			{
				return fluid.getFluid().equals(FluidLoader.fluidXP) && fluid.amount >= 20;
			}

			@Override
			public int fillInternal(FluidStack resource, boolean doFill)
			{
				if (resource == null || resource.amount < 20)
				{
					return 0;
				}

				if (!doFill)
				{
					if (fluid == null)
					{
						return Math.min(capacity, resource.amount - resource.amount % 20);
					}

					if (!fluid.isFluidEqual(resource))
					{
						return 0;
					}

					return Math.min(capacity - fluid.amount, resource.amount - resource.amount % 20);
				}

				if (fluid == null)
				{
					fluid = new FluidStack(resource, Math.min(capacity, resource.amount - resource.amount % 20));

					onContentsChanged();

					if (tile != null)
					{
						FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(),
								this, fluid.amount));
					}
					return fluid.amount;
				}

				if (!fluid.isFluidEqual(resource))
				{
					return 0;
				}
				int filled = capacity - fluid.amount;

				if (resource.amount < filled)
				{
					fluid.amount += (resource.amount - resource.amount % 20);
					filled = resource.amount - resource.amount % 20;
				} else
				{
					fluid.amount = capacity;
				}

				onContentsChanged();

				if (tile != null)
				{
					FluidEvent.fireEvent(
							new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, filled));
				}
				return filled;
			}

		};
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if (this.xp * 20 != this.tank.getFluidAmount())
		{
			int amount = Math.max(this.xp, this.tank.getFluidAmount() / 20);
			this.xp = amount;
			this.tank.getFluid().amount = amount * 20;
		}
		fillFluidTo();
	}

	public void fillFluidTo()
	{
		BlockPos pos = this.pos.up();
		TileEntity te = world.getTileEntity(pos);
		FluidStack fluid = this.tank.getFluid();
		int amount = 0;
		if (te != null && fluid != null)
			if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
				amount = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN).fill(fluid,
						false);
		if (amount >= 20)
		{
			if (amount % 20 != 0)
				amount -= amount % 20;
			te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)
					.fill(new FluidStack(fluid, amount), true);
			tank.getFluid().amount -= amount;
			this.xp -= amount;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		this.xp = tag.getInteger("StoredXP");
		tank.getFluid().amount = this.xp * 20;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("StoredXP", this.xp);
		return tag;
	}

	public void setDataFromNBT(NBTTagCompound tag)
	{
		this.xp = tag.getInteger("StoredXP");
		tank.readFromNBT(tag);
		tank.getFluid().amount = this.xp * 20;
	}

	public NBTTagCompound setNBTFromData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		tank.writeToNBT(nbt);
		nbt.setInteger("StoredXP", this.xp);
		return nbt;
	}

	public void setXPFromPlayer(EntityPlayer player, int button)
	{
		player.addExperienceLevel(0);
		int level = 0;
		int xp_take = 0;
		player.experienceTotal = Math.round(player.xpBarCap() * player.experience)
				+ Helper.getTotalXp(player.experienceLevel);
		switch (button)
		{
		case 0:
			level = 1;
			break;
		case 1:
			level = 10;
			break;
		case 2:
			this.xp += player.experienceTotal;
			player.removeExperienceLevel(player.experienceLevel + 1);
			player.experienceTotal = 0;
			tank.getFluid().amount = this.xp * 20;
			return;
		case 3:
			level = -1;
			break;
		case 4:
			level = -10;
			break;
		case 5:
			player.removeExperienceLevel(player.experienceLevel + 1);
			int player_level = Helper.getXpTotalLevel(player.experienceTotal + this.xp);
			player.addExperienceLevel(player_level);
			player.experience = (player.experienceTotal + this.xp - Helper.getTotalXp(player_level))
					/ (float) player.xpBarCap();
			player.experienceTotal = Math.round(player.xpBarCap() * player.experience)
					+ Helper.getTotalXp(player.experienceLevel);
			this.xp = 0;
			tank.getFluid().amount = 0;
			return;
		}
		if (level > 0)
		{
			xp_take = player.experienceTotal - Helper.getTotalXp(player.experienceLevel - level);
			this.xp += xp_take;
			player.experience = .0F;
			player.experienceTotal -= xp_take;
			player.removeExperienceLevel(level);
			player.experienceTotal = Math.round(player.xpBarCap() * player.experience)
					+ Helper.getTotalXp(player.experienceLevel);
			tank.getFluid().amount = this.xp * 20;
		} else
		{
			xp_take = Math.min(Helper.getTotalXp(player.experienceLevel - level) - player.experienceTotal, this.xp);
			this.xp -= xp_take;
			this.modifyPlayerXP(player, xp_take);
			player.experienceTotal = Math.round(player.xpBarCap() * player.experience)
					+ Helper.getTotalXp(player.experienceLevel);
			tank.getFluid().amount = this.xp * 20;
		}
	}

	private void modifyPlayerXP(EntityPlayer player, float amount)
	{
		if (amount < (1 - player.experience) * player.xpBarCap())
		{
			if (amount < 1.0F)
				return;
			player.experience += amount / player.xpBarCap();
		} else
		{
			player.experience = .0F;
			amount -= player.xpBarCap();
			player.addExperienceLevel(1);
			this.modifyPlayerXP(player, amount);
		}
	}

	public int getXp()
	{
		return xp;
	}

	public void setXp(int xp)
	{
		this.xp = xp;
	}

	public BlockPos getBlockPos()
	{
		return getPos();
	}
}
