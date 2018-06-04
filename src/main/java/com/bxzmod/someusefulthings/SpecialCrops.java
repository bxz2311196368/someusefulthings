package com.bxzmod.someusefulthings;

import blusunrize.immersiveengineering.common.IEContent;
import com.blakebr0.mysticalagriculture.items.ModItems;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpecialCrops
{
	public static HashMap<String, SpecialCrops> Crops = Maps.newHashMap();

	public static File configFile;

	private ItemStack seed, crop, s_crop;

	public SpecialCrops(ItemStack seed, ItemStack crop, ItemStack s_crop)
	{
		super();
		this.seed = seed;
		this.crop = crop;
		this.s_crop = s_crop;
	}

	public SpecialCrops(Item seed, Item crop, Item s_crop)
	{
		super();
		this.seed = new ItemStack(seed);
		this.crop = new ItemStack(crop);
		this.s_crop = new ItemStack(s_crop);
	}

	public SpecialCrops(ItemStack itemStack)
	{
		this(itemStack, itemStack, itemStack);
	}

	public static void init()
	{
		Crops.put(new ItemStack(Items.POTATO).serializeNBT().toString(),
				new SpecialCrops(Items.POTATO, Items.POTATO, Items.POISONOUS_POTATO));
		Crops.put(new ItemStack(Blocks.CHORUS_FLOWER).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Blocks.CHORUS_FLOWER), new ItemStack(Items.CHORUS_FRUIT),
						new ItemStack(Items.CHORUS_FRUIT)));
		Crops.put(new ItemStack(Blocks.BROWN_MUSHROOM).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Blocks.BROWN_MUSHROOM)));
		Crops.put(new ItemStack(Blocks.RED_MUSHROOM).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Blocks.RED_MUSHROOM)));
		Crops.put(new ItemStack(Items.NETHER_WART).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Items.NETHER_WART)));
		Crops.put(new ItemStack(Blocks.CACTUS).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Blocks.CACTUS)));
		Crops.put(new ItemStack(Items.REEDS).serializeNBT().toString(), new SpecialCrops(new ItemStack(Items.REEDS)));
		Crops.put(new ItemStack(Items.DYE, 1, 3).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(Items.DYE, 1, 3)));
		if (Loader.isModLoaded("immersiveengineering"))
		{
			ieINIT();
		}
		if (Loader.isModLoaded("mysticalagriculture"))
		{
			mgINIT();
		}
		if (Loader.isModLoaded("actuallyadditions"))
		{
			aaINIT();
		}
	}

	@Optional.Method(modid = "immersiveengineering")
	private static void ieINIT()
	{
		Item seed = IEContent.itemSeeds;
		Crops.put(new ItemStack(seed).serializeNBT().toString(), new SpecialCrops(new ItemStack(seed),
				new ItemStack(IEContent.itemMaterial, 1, 4), new ItemStack(IEContent.itemMaterial, 1, 4)));
	}

	@Optional.Method(modid = "mysticalagriculture")
	private static void mgINIT()
	{
		Crops.put(new ItemStack(ModItems.itemTier1CraftingSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier1CraftingSeed)));
		Crops.put(new ItemStack(ModItems.itemTier2CraftingSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier2CraftingSeed)));
		Crops.put(new ItemStack(ModItems.itemTier3CraftingSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier3CraftingSeed)));
		Crops.put(new ItemStack(ModItems.itemTier4CraftingSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier4CraftingSeed)));
		Crops.put(new ItemStack(ModItems.itemTier5CraftingSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier5CraftingSeed)));
		Crops.put(new ItemStack(ModItems.itemTier1InferiumSeeds).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier1InferiumSeeds),
						new ItemStack(ModItems.itemInferiumEssence), new ItemStack(ModItems.itemInferiumEssence, 0)));
		Crops.put(new ItemStack(ModItems.itemTier2InferiumSeeds).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier2InferiumSeeds),
						new ItemStack(ModItems.itemInferiumEssence), new ItemStack(ModItems.itemInferiumEssence, 8)));
		Crops.put(new ItemStack(ModItems.itemTier3InferiumSeeds).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier3InferiumSeeds),
						new ItemStack(ModItems.itemInferiumEssence), new ItemStack(ModItems.itemInferiumEssence, 16)));
		Crops.put(new ItemStack(ModItems.itemTier4InferiumSeeds).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier4InferiumSeeds),
						new ItemStack(ModItems.itemInferiumEssence), new ItemStack(ModItems.itemInferiumEssence, 24)));
		Crops.put(new ItemStack(ModItems.itemTier5InferiumSeeds).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(ModItems.itemTier5InferiumSeeds),
						new ItemStack(ModItems.itemInferiumEssence), new ItemStack(ModItems.itemInferiumEssence, 32)));
	}

	@Optional.Method(modid = "actuallyadditions")
	private static void aaINIT()
	{
		Crops.put(new ItemStack(InitItems.itemRiceSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(InitItems.itemRiceSeed), new ItemStack(InitItems.itemFoods, 1, 16),
						new ItemStack(InitItems.itemFoods, 1, 16)));
		Crops.put(new ItemStack(InitItems.itemCanolaSeed).serializeNBT().toString(),
				new SpecialCrops(new ItemStack(InitItems.itemCanolaSeed), new ItemStack(InitItems.itemMisc, 1, 13),
						new ItemStack(InitItems.itemMisc, 1, 13)));
	}

	public static void reinit()
	{
		Crops.clear();
		init();
		try
		{
			saveData();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void saveData() throws IOException
	{
		if (!configFile.exists())
			configFile.createNewFile();
		Gson gson = new Gson();
		String json = gson.toJson(toStringMap());
		Files.write(json, configFile, Charsets.UTF_8);
	}

	public static void loadData() throws IOException
	{
		if (configFile.exists())
		{
			Gson gson = new Gson();
			String json = Files.toString(configFile, Charsets.UTF_8);
			HashMap<String, String[]> temp = gson.fromJson(json, new TypeToken<HashMap<String, String[]>>()
			{
			}.getType());
			if (temp != null && !temp.isEmpty())
				loadMapFromString(temp);
			else
				Crops.clear();
		} else
		{
			Crops.clear();
			init();
			saveData();
		}
	}

	public static void clear()
	{
		Crops.clear();
	}

	public static HashMap<String, String[]> toStringMap()
	{
		HashMap<String, String[]> temp = Maps.newHashMap();
		if (!Crops.isEmpty())
		{
			Iterator<Map.Entry<String, SpecialCrops>> it = Crops.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, SpecialCrops> entry = it.next();
				temp.put(entry.getKey(), entry.getValue().toStringArray());
			}
		}
		return temp;
	}

	public static void loadMapFromString(HashMap<String, String[]> temp)
	{
		Crops.clear();
		if (!temp.isEmpty())
		{
			Iterator<Map.Entry<String, String[]>> it = temp.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, String[]> entry = it.next();
				try
				{
					String key = entry.getKey();
					SpecialCrops scrop = fromStringArray(entry.getValue());
					if (scrop != null)
						Crops.put(key, scrop);
					else
						throw new RuntimeException("bad config");
				} catch (Exception e)
				{
					e.printStackTrace();
					Crops.clear();
					init();
				}
			}
		}
	}

	public String[] toStringArray()
	{
		String[] ss = new String[3];
		ss[0] = seed.serializeNBT().toString();
		ss[1] = crop.serializeNBT().toString();
		ss[2] = s_crop.serializeNBT().toString();
		return ss;

	}

	public static SpecialCrops fromStringArray(String[] ss)
	{
		ItemStack seed = null, crop = null, s_crop = null;
		try
		{
			seed = ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson(ss[0]));
			crop = ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson(ss[1]));
			s_crop = ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson(ss[2]));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (seed != null && crop != null && s_crop != null)
			return new SpecialCrops(seed, crop, s_crop);
		return null;
	}

	public ItemStack getSeed()
	{
		return seed;
	}

	public ItemStack getCrop()
	{
		return crop;
	}

	public ItemStack getS_crop()
	{
		return s_crop;
	}
}
