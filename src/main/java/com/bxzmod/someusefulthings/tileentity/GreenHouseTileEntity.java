package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.SpecialCrops;
import com.bxzmod.someusefulthings.asm.BXZFMLLoadingPlugin;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class GreenHouseTileEntity extends TileEntityBase
{
	private boolean work = true;
	private boolean noSeed = false, noS_Crop = false, no_crop = false;
	private boolean isdeobf = BXZFMLLoadingPlugin.deobf;
	private int workType = 0;
	private static Map<BlockCrops, ItemCropSeed> cache = Maps.newHashMap();

	public GreenHouseTileEntity()
	{
		super(new ItemStackHandlerModify(1, 9).setSlotChecker(0,
			stack -> stack.getItem() instanceof ItemSeeds || stack.getItem() instanceof ItemSeedFood
				|| SpecialCrops.Crops.containsKey(Helper.copyStack(stack, 1).serializeNBT().toString())).getInventory(),
			new DefaultSide());
	}

	@Override
	public void work()
	{
		if (!work)
			return;
		ItemStack input = iInventory.getStackInSlot(0);
		if (input != null)
		{
			BlockCrops crops = null;
			BlockStem stem = null;
			String temp = Helper.copyStack(iInventory.getStackInSlot(0), 1).serializeNBT().toString();
			if (SpecialCrops.Crops.containsKey(temp))
			{
				SpecialCrops special_crop = SpecialCrops.Crops.get(temp);
				if (!no_crop)
					this.innerInsert(Helper.copyStack(special_crop.getCrop(), 8), 1, 10, false);
				if (!noS_Crop)
					this.innerInsert(Helper.copyStack(special_crop.getS_crop()), 1, 10, false);
				if (!noSeed)
					this.innerInsert(Helper.copyStack(special_crop.getSeed(), 1), 1, 10, false);
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
	}

	private void output(BlockCrops crops)
	{
		Item crop, seed;
		if (cache.containsKey(crops))
		{
			ItemCropSeed cropSeed = cache.get(crops);
			crop = cropSeed.getCrop();
			seed = cropSeed.getSeed();
		} else
		{
			try
			{
				Method m_getCrop = BlockCrops.class
					.getDeclaredMethod(isdeobf ? "getCrop" : "func_149865_P"), m_getSeed = BlockCrops.class
					.getDeclaredMethod(isdeobf ? "getSeed" : "func_149866_i");
				m_getCrop.setAccessible(true);
				m_getSeed.setAccessible(true);
				crop = (Item) m_getCrop.invoke(crops);
				seed = (Item) m_getSeed.invoke(crops);
				cache.put(crops, new ItemCropSeed(crop, seed));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
				return;
			}
		}
		if (!no_crop)
			this.innerInsert(new ItemStack(crop, 8), 1, 10, false);
		if (!noSeed)
		{
			if (Helper.isSameStackIgnoreAmount(new ItemStack(seed), iInventory.getStackInSlot(0)))
				this.innerInsert(Helper.copyStack(iInventory.getStackInSlot(0), 1), 1, 10, false);
			else
				this.innerInsert(new ItemStack(seed, 1), 1, 10, false);
		}
	}

	private void output(BlockStem stem)
	{
		Block crop = stem.crop;
		if (!no_crop)
			this.innerInsert(new ItemStack(crop, 4), 1, 10, false);
		if (!noSeed)
			this.innerInsert(Helper.copyStack(iInventory.getStackInSlot(0), 1), 1, 10, false);
	}

	public void setDataFromNBT(NBTTagCompound compound)
	{
		super.setDataFromNBT(compound);
		this.work = compound.getBoolean("Work");
		this.workType = compound.getInteger("workType");
		this.setWorkType(this.workType);
	}

	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		super.setNBTFromData(compound);
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

	private static class ItemCropSeed
	{
		private Item crop, seed;

		public ItemCropSeed(Item crop, Item seed)
		{
			this.crop = crop;
			this.seed = seed;
		}

		public Item getCrop()
		{
			return crop;
		}

		public Item getSeed()
		{
			return seed;
		}
	}
}
