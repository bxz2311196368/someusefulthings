package com.bxzmod.someusefulthings.recipes;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.blocks.BlockLoader;
import com.bxzmod.someusefulthings.config.ConfigLoader;
import com.bxzmod.someusefulthings.fluid.FluidLoader;
import com.bxzmod.someusefulthings.items.ItemLoader;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftingLoader
{
	public final static ItemStack limitlesstoolwithnbt = new ItemStack(ItemLoader.limitlesstool);
	public final static ItemStack limitlesstoolwithnbt1 = new ItemStack(ItemLoader.limitlesstool);

	static
	{
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		limitlesstoolwithnbt.addEnchantment(Enchantment.getEnchantmentByID(21), 10);
		limitlesstoolwithnbt.addEnchantment(Enchantment.getEnchantmentByID(35), 10);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.getEnchantmentByID(21), 10);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
		limitlesstoolwithnbt1.getTagCompound().setTag("dig_parameter", dig);
		limitlesstoolwithnbt1.getTagCompound().setTag("dig_parameter", dig);
	}

	public CraftingLoader(FMLInitializationEvent event)
	{
		registerRecipeClass();
		registerRecipe();
		registerSmelting();
		registerFuel();
	}

	private static void registerRecipeClass()
	{
		RecipeSorter.register(ModInfo.MODID + ":shaped_ore_no_return", ShapedOreRecipeNoReturn.class,
			RecipeSorter.Category.SHAPED, "after:forge:shapedore");
	}

	private static void registerRecipe()
	{
		if (ConfigLoader.invincible_ring)
			GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.invinciblering), " # ", "#*#", " # ", '#', Items.NETHER_STAR,
					'*', Item.getItemFromBlock(Blocks.DRAGON_EGG));
		if (ConfigLoader.redstone_apple)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.redstoneapple), "###", "#*#", "###", '#',
					"dustRedstone", '*', Items.APPLE));
		if (ConfigLoader.limitless_tool)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(limitlesstoolwithnbt, "PAS", "WDH", " # ", 'P', Items.DIAMOND_PICKAXE,
					'A', Items.DIAMOND_AXE, 'S', Items.DIAMOND_SHOVEL, 'W', Items.DIAMOND_SWORD, 'D', "blockDiamond",
					'H', Items.DIAMOND_HOE, '#', Items.SHEARS));
			GameRegistry.addRecipe(new ShapedOreRecipe(limitlesstoolwithnbt1, "SAP", "WDH", " # ", 'P', Items.DIAMOND_PICKAXE,
					'A', Items.DIAMOND_AXE, 'S', Items.DIAMOND_SHOVEL, 'W', Items.DIAMOND_SWORD, 'D', "blockDiamond",
					'H', Items.DIAMOND_HOE, '#', Items.SHEARS));
		}
		if (ConfigLoader.artifact_sword)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.artifactsword), "###", "#*#", "###", '#', Items.NETHER_STAR,
					'*', ItemLoader.compressedDiamondSword);
			GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.compressedDiamondSword), "###", "###", "###", '#',
					Items.DIAMOND_SWORD);
		}
		if (ConfigLoader.remove_enchantment)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.removeEnchantmentItemBlock), " # ",
					"#*#", " # ", '#', "gemDiamond", '*', Item.getItemFromBlock(Blocks.ENCHANTING_TABLE)));
		if (ConfigLoader.reinforcement_machine)
			GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.reinforcementMachineItemBlock), " # ", "#*#", " # ",
					'#', Items.NETHER_STAR, '*', Item.getItemFromBlock(Blocks.ENCHANTING_TABLE));
		if (ConfigLoader.copy_enchantment)
			GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.copyEnchantmentItemBlock), "###", "#*#", "###", '#',
					Items.ENCHANTED_BOOK, '*', Item.getItemFromBlock(Blocks.ENCHANTING_TABLE));
		if (ConfigLoader.portable_inventory_item)
			for (int i = 0; i < 16; i++)
			{
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.portableInventoryItem, 1, i), "WDW",
						"LCL", "WDW", 'W', "wool", 'D', "dye" + toUpperCaseFirstOne(EnumDyeColor.byMetadata(i).toString()),
						'C', "chest", 'L', "leather"));
			}
		if (ConfigLoader.bucket_wwwwater)
			GameRegistry.addShapelessRecipe(
					UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket,
							FluidLoader.fluidWwwwwater),
					new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.BUCKET));
		GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.forceEnchTableItemBlock), "###", "###", "###", '#',
				Item.getItemFromBlock(Blocks.ENCHANTING_TABLE));
		GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.tankItemBlock), "###", "#*#", "###", '#', Item.getItemFromBlock(Blocks.GLASS),
				'*', Items.BUCKET);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.eyeGenerator), "#*#", "#&#", "#*#", '#',
				"blockRedstone", '*', "dustRedstone", '&', Items.ENDER_EYE));
		GameRegistry.addRecipe(new ShapedOreRecipe((new ItemStack(BlockLoader.replacementMachine)), "&#&", "#*#", "&#&",
				'*', "blockRedstone", '#', Items.STONE_PICKAXE, '&', "dustRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.replacementMachineSetter), " * ", "*#*",
				" * ", '#', Items.PAPER, '*', "dustRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.cobbleStoneMaker), "#$#", "*$&", "#$#", '#',
				"cobblestone", '*', Items.LAVA_BUCKET, '&', Items.WATER_BUCKET, '$', "stone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.infiniteWater), "###", "#*#", "###", '#',
				"cobblestone", '*', Items.WATER_BUCKET));
		//TODO 试图修复自动合成
		/*GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(BlockLoader.craftingTable), "a#a", "#*#", "a#a", '#', "cobblestone",
						'*', Item.getItemFromBlock(Blocks.CRAFTING_TABLE), 'a', "plankWood"));*/
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemLoader.portableCrafter), new ItemStack(Blocks.CRAFTING_TABLE),
				"plankWood", "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.xpReservoir), "a#a", "#*#", "a#a", '#',
				"gemDiamond", '*', Item.getItemFromBlock(BlockLoader.tank), 'a', "ingotGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.chunkLoader), "a#a", "#*#", "a#a", '#',
				"gemDiamond", '*', Item.getItemFromBlock(Blocks.ANVIL), 'a', "ingotGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.lavaPump), "a#a", "#*#", "a#a", '#', Items.LAVA_BUCKET,
				'*', Item.getItemFromBlock(Blocks.IRON_BLOCK), 'a', Items.BUCKET));
		for (int i = 0; i < 16; i++)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemLoader.garbagebag),
					new ItemStack(ItemLoader.portableInventoryItem, 1, i), "cobblestone"));
		if (OreDictionary.doesOreNameExist("listAllseed"))
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.greenHouse), "###", "#*#", "###", '#',
					"listAllseed", '*', Item.getItemFromBlock(Blocks.DIRT)));
		else
			GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.greenHouse), "###", "#*#", "###", '#', Items.WHEAT_SEEDS,
					'*', Item.getItemFromBlock(Blocks.DIRT));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.multiplePickaxes0), "###", "#*#", "###", '*',
				Items.STONE_PICKAXE, '#', "cobblestone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.multiplePickaxes1), "###", "#*#", "###", '*',
				Items.IRON_PICKAXE, '#', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.multiplePickaxes2), "###", "#*#", "###", '*',
				Items.GOLDEN_PICKAXE, '#', "ingotGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.infiniteFuel), "###", "#*#", "###", '*',
				"blockCoal", '#', "gemDiamond"));
		GameRegistry.addRecipe(new ShapedOreRecipeNoReturn(new ItemStack(BlockLoader.fastFurnace), "###", "#*#", "###",
				'*', ItemLoader.infiniteFuel, '#', "blockCoal"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.mobSummon), "###", "#*#", "###", '*',
				"blockDiamond", '#', Blocks.MOB_SPAWNER));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.DRAGON_EGG), " # ", "#*#", " # ", '*', Items.EGG,
				'#', Items.NETHER_STAR));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLoader.wrench), "s s", "sis", " s ", 's', "stickWood", 'i', "ingotIron"));
	}

	private static void registerSmelting()
	{
		if (OreDictionary.doesOreNameExist("ingotSteel"))
		{
			if (FurnaceRecipes.instance().getSmeltingResult(new ItemStack(Items.IRON_INGOT)) == null)
				GameRegistry.addSmelting(OreDictionary.getOres("ingotIron").get(0),
						OreDictionary.getOres("ingotSteel").get(0), 0.5F);
		}

		if (OreDictionary.doesOreNameExist("blockSteel"))
			GameRegistry.addSmelting(OreDictionary.getOres("blockIron").get(0),
					OreDictionary.getOres("blockSteel").get(0), 0.5F);

	}

	private static void registerFuel()
	{
		GameRegistry.registerFuelHandler(BXZFuelHandler.instacnce);
		BXZFuelHandler.addFuel(new ItemStack(ItemLoader.infiniteFuel), 10000);
	}

	public static String toUpperCaseFirstOne(String s)
	{
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static class ShapedOreRecipeNoReturn extends ShapedOreRecipe
	{
		public ShapedOreRecipeNoReturn(Block result, Object... recipe)
		{
			super(result, recipe);
		}

		public ShapedOreRecipeNoReturn(Item result, Object... recipe)
		{
			super(result, recipe);
		}

		public ShapedOreRecipeNoReturn(ItemStack result, Object... recipe)
		{
			super(result, recipe);
		}

		@Override
		public ItemStack[] getRemainingItems(InventoryCrafting inv)
		{
			return new ItemStack[9];
		}
	}

}
