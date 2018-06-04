package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Info;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRenderLoader
{
	@SideOnly(Side.CLIENT)
	public BlockRenderLoader(FMLPreInitializationEvent event)
	{
		/*
		 * no use
		 * only split blockstate file will use it
		registerRender(BlockLoader.removeEnchantment,BlockLoader.removeEnchantmentItemBlock);
		registerBlockStateMapper(BlockLoader.removeEnchantment, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.copyEnchantment, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.reinforcementMachine, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.forceEnchTable, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.tank, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.eyeGenerator, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.replacementMachine, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.cobbleStoneMaker, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.infiniteWater, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.craftingTable, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.chunkLoader, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.lavaPump, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.xpReservoir, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.greenHouse, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.fastFurnace, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.mobSummon, new StateMap.Builder().build());
		registerBlockStateMapper(BlockLoader.energyBlock, new StateMap.Builder().build());
		*/

		registerItemBlockRender(BlockLoader.removeEnchantmentItemBlock, 0, "remove_enchantment", "inventory");
		registerItemBlockRender(BlockLoader.copyEnchantmentItemBlock, 0, "copy_enchantment", "inventory");
		registerItemBlockRender(BlockLoader.reinforcementMachineItemBlock, 0, "reinforcement_machine", "inventory");
		registerItemBlockRender(BlockLoader.forceEnchTableItemBlock, 0, "force_ench_table", "inventory");
		registerItemBlockRender(BlockLoader.tankItemBlock, 0, "tank", "inventory");
		registerItemBlockRender(BlockLoader.eyeGeneratorItemBlock, 0, "eye_generator", "inventory");
		registerItemBlockRender(BlockLoader.replacementMachineItemBlock, 0, "replacement_machine", "inventory");
		registerItemBlockRender(BlockLoader.cobbleStoneMakerItemBlock, 0, "cobble_stone_maker", "inventory");
		registerItemBlockRender(BlockLoader.infiniteWaterItemBlock, 0, "infinite_water", "inventory");
		registerItemBlockRender(BlockLoader.craftingTableItemBlock, 0, "crafting_table", "inventory");
		registerItemBlockRender(BlockLoader.chunkLoaderItemBlock, 0, "chunk_loader", "inventory");
		registerItemBlockRender(BlockLoader.lavaPumpItemBlock, 0, "lava_pump", "inventory");
		registerItemBlockRender(BlockLoader.xpReservoirItemBlock, 0, "xp_reservoir", "inventory");
		registerItemBlockRender(BlockLoader.greenHouseItemBlock, 0, "green_house", "inventory");
		registerItemBlockRender(BlockLoader.fastFurnaceItemBlock, 0, "fast_furnace", "inventory");
		registerItemBlockRender(BlockLoader.mobSummonItemBlock, 0, "mob_summon", "inventory");
		registerItemBlockRender(BlockLoader.energyItemBlock, 0, "energy_block", "inventory");

	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Block block, ItemBlock item)
	{
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(block.getRegistryName(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
	}

	/*
	 * variantIn mean witch the item render with in blockstate; if type has more
	 * than one, use xxx=yyyy
	 */
	@SideOnly(Side.CLIENT)
	private static void registerItemBlockRender(ItemBlock item, int meta, String name, String variantIn)
	{
		ResourceLocation location = new ResourceLocation(Info.MODID, name);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, variantIn));
	}

	@SideOnly(Side.CLIENT)
	private static void registerBlockStateMapper(Block block, IStateMapper mapper)
	{
		ModelLoader.setCustomStateMapper(block, mapper);
	}

}
