package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.*;
import com.bxzmod.someusefulthings.asm.BXZFMLLoadingPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GreenHouseTileEntity extends TileEntity implements ITickable
{
	private boolean work = true;

	ItemStackHandlerModify inv = new ItemStackHandlerModify(1, 9)
			.setSlotChecker(0,
					stack -> stack.getItem() instanceof ItemSeeds || stack.getItem() instanceof ItemSeedFood
							|| SpecialCrops.Crops.containsKey(Helper.copyStack(stack, 1).serializeNBT().toString()))
			.getInventory();

	private boolean noSeed = false, noS_Crop = false, no_crop = false;
	private boolean isdeobf = BXZFMLLoadingPlugin.deobf;
	private int workType = 0;

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if (!work)
			return;
		ItemStack input = inv.getStackInSlot(0);
		if (input != null)
		{
			BlockCrops crops = null;
			BlockStem stem = null;
			String temp = Helper.copyStack(inv.getStackInSlot(0), 1).serializeNBT().toString();
			if (SpecialCrops.Crops.containsKey(temp))
			{
				SpecialCrops special_crop = SpecialCrops.Crops.get(temp);
				if (!no_crop)
					Helper.mergeItemStack(Helper.copyStack(special_crop.getCrop(), 8), 0, 9, false, this.inv);
				if (!noS_Crop)
					Helper.mergeItemStack(Helper.copyStack(special_crop.getS_crop()), 0, 9, false, this.inv);
				if (!noSeed)
					Helper.mergeItemStack(Helper.copyStack(special_crop.getSeed(), 1), 0, 9, false, this.inv);
			} else
			{
				if (input.getItem() instanceof ItemSeeds)
				{
					ItemSeeds seeds = (ItemSeeds) input.getItem();
					if (seeds.crops instanceof BlockCrops)
					{
						crops = (BlockCrops) seeds.crops;

					}
					if (seeds.crops instanceof BlockStem)
					{
						stem = (BlockStem) seeds.crops;
					}
				} else if (input.getItem() instanceof ItemSeedFood)
				{
					ItemSeedFood seeds = (ItemSeedFood) input.getItem();
					if (seeds.crops instanceof BlockCrops)
					{
						crops = (BlockCrops) seeds.crops;

					}
					if (seeds.crops instanceof BlockStem)
					{
						stem = (BlockStem) seeds.crops;
					}
				} else
					return;
				if (crops != null)
					this.output(crops);
				if (stem != null)
					this.output(stem);
			}
		}
		this.inv.tryInAndOut(world, pos);
		this.markDirty();
	}

	private void output(BlockCrops crops)
	{
		try
		{
			Method m_getCrop = BlockCrops.class.getDeclaredMethod(isdeobf ? "getCrop" : "func_149865_P"),
					m_getSeed = BlockCrops.class.getDeclaredMethod(isdeobf ? "getSeed" : "func_149866_i");
			m_getCrop.setAccessible(true);
			m_getSeed.setAccessible(true);
			Item crop = (Item) m_getCrop.invoke(crops), seed = (Item) m_getSeed.invoke(crops);
			if (!no_crop)
				Helper.mergeItemStack(new ItemStack(crop, 8), 0, 9, false, this.inv);
			if (!noSeed)
			{
				if (Helper.isSameStackIgnoreAmount(new ItemStack(seed), inv.getStackInSlot(0)))
					Helper.mergeItemStack(Helper.copyStack(inv.getStackInSlot(0), 1), 0, 9, false, this.inv);
				else
					Helper.mergeItemStack(new ItemStack(seed, 1), 0, 9, false, this.inv);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			e.printStackTrace();
			return;
		}
	}

	private void output(BlockStem stem)
	{
		Block crop = stem.crop;
		if (!no_crop)
			Helper.mergeItemStack(new ItemStack(crop, 4), 0, 9, false, this.inv);
		if (!noSeed)
			Helper.mergeItemStack(Helper.copyStack(inv.getStackInSlot(0), 1), 0, 9, false, this.inv);
	}

	private IBlockState getSoilState(Block blockIn)
	{
		if (blockIn == null)
			return Blocks.AIR.getDefaultState();
		if (blockIn == Blocks.FARMLAND)
			return blockIn.getDefaultState().withProperty(BlockFarmland.MOISTURE, Integer.valueOf(7));
		return blockIn.getDefaultState();
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
			return (T) this.inv;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.setDataFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		return this.setNBTFromData(super.writeToNBT(compound));
	}

	public void setDataFromNBT(NBTTagCompound compound)
	{
		this.inv.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.work = compound.getBoolean("Work");
		this.workType = compound.getInteger("workType");
		this.setWorkType(this.workType);
	}

	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		compound.setTag("Inventory", inv.serializeNBT());
		compound.setBoolean("Work", this.work);
		compound.setInteger("workType", this.workType);
		return compound;
	}

	public void cycleSets(EntityPlayer player)
	{
		if (player == null)
			return;
		workType = workType >= 4 ? 0 : workType + 1;
		this.setWorkType(this.workType);
		player.sendMessage(new TextComponentTranslation("greenHouse.msg." + this.workType));
		this.markDirty();
	}

	private void setWorkType(int workType)
	{
		switch (workType)
		{
		case 0:
			noSeed = false;
			no_crop = false;
			noS_Crop = false;
			break;
		case 1:
			noSeed = true;
			no_crop = false;
			noS_Crop = false;
			break;
		case 2:
			noSeed = true;
			no_crop = false;
			noS_Crop = true;
			break;
		case 3:
			noSeed = true;
			no_crop = true;
			noS_Crop = false;
			break;
		case 4:
			noSeed = false;
			no_crop = true;
			noS_Crop = true;
		}
	}

	public void setWork(boolean work)
	{
		this.work = work;
	}

	public ItemStackHandlerModify getInv()
	{
		return inv;
	}
}
