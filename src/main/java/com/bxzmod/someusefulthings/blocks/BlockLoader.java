package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.blocks.itemblock.UniverseItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLoader
{
	public static Block removeEnchantment = new RemoveEnchantment();
	public static UniverseItemBlock removeEnchantmentItemBlock = new UniverseItemBlock(removeEnchantment);
	public static Block copyEnchantment = new CopyEnchantment();
	public static UniverseItemBlock copyEnchantmentItemBlock = new UniverseItemBlock(copyEnchantment);
	public static Block reinforcementMachine = new ReinforcementMachine();
	public static UniverseItemBlock reinforcementMachineItemBlock = new UniverseItemBlock(reinforcementMachine);
	public static Block forceEnchTable = new ForceEnchTable();
	public static UniverseItemBlock forceEnchTableItemBlock = new UniverseItemBlock(forceEnchTable);
	public static Block tank = new TankBlock();
	public static UniverseItemBlock tankItemBlock = new UniverseItemBlock(tank);
	public static Block eyeGenerator = new EyeGenerator();
	public static UniverseItemBlock eyeGeneratorItemBlock = new UniverseItemBlock(eyeGenerator);
	public static Block replacementMachine = new ReplacementMachine();
	public static UniverseItemBlock replacementMachineItemBlock = new UniverseItemBlock(replacementMachine);
	public static Block cobbleStoneMaker = new CobbleStoneMaker();
	public static UniverseItemBlock cobbleStoneMakerItemBlock = new UniverseItemBlock(cobbleStoneMaker);
	public static Block infiniteWater = new InfiniteWater();
	public static UniverseItemBlock infiniteWaterItemBlock = new UniverseItemBlock(infiniteWater);
	public static Block craftingTable = new CraftingTable();
	public static UniverseItemBlock craftingTableItemBlock = new UniverseItemBlock(craftingTable);
	public static Block chunkLoader = new ChunkLoader();
	public static UniverseItemBlock chunkLoaderItemBlock = new UniverseItemBlock(chunkLoader);
	public static Block lavaPump = new LavaPump();
	public static UniverseItemBlock lavaPumpItemBlock = new UniverseItemBlock(lavaPump);
	public static Block xpReservoir = new XPReservoir();
	public static UniverseItemBlock xpReservoirItemBlock = new UniverseItemBlock(xpReservoir);
	public static Block greenHouse = new GreenHouse();
	public static UniverseItemBlock greenHouseItemBlock = new UniverseItemBlock(greenHouse);
	public static Block fastFurnace = new FastFurnace();
	public static UniverseItemBlock fastFurnaceItemBlock = new UniverseItemBlock(fastFurnace);
	public static Block mobSummon = new MobSummon();
	public static UniverseItemBlock mobSummonItemBlock = new UniverseItemBlock(mobSummon);
	public static Block energyBlock = new EnergyBlock();
	public static UniverseItemBlock energyItemBlock = new UniverseItemBlock(energyBlock);

	public BlockLoader(FMLPreInitializationEvent event)
	{
		registerBlock(removeEnchantment);
		registerItem(removeEnchantmentItemBlock);
		registerBlock(copyEnchantment);
		registerItem(copyEnchantmentItemBlock);
		registerBlock(reinforcementMachine);
		registerItem(reinforcementMachineItemBlock);
		registerBlock(forceEnchTable);
		registerItem(forceEnchTableItemBlock);
		registerBlock(tank);
		registerItem(tankItemBlock);
		registerBlock(eyeGenerator);
		registerItem(eyeGeneratorItemBlock);
		registerBlock(replacementMachine);
		registerItem(replacementMachineItemBlock);
		registerBlock(cobbleStoneMaker);
		registerItem(cobbleStoneMakerItemBlock);
		registerBlock(infiniteWater);
		registerItem(infiniteWaterItemBlock);
		registerBlock(craftingTable);
		registerItem(craftingTableItemBlock);
		registerBlock(chunkLoader);
		registerItem(chunkLoaderItemBlock);
		registerBlock(lavaPump);
		registerItem(lavaPumpItemBlock);
		registerBlock(xpReservoir);
		registerItem(xpReservoirItemBlock);
		registerBlock(greenHouse);
		registerItem(greenHouseItemBlock);
		registerBlock(fastFurnace);
		registerItem(fastFurnaceItemBlock);
		registerBlock(mobSummon);
		registerItem(mobSummonItemBlock);
		registerBlock(energyBlock);
		registerItem(energyItemBlock);
	}

	private static void registerBlock(Block block)
	{
		GameRegistry.register(block);
	}

	private static void registerItem(ItemBlock item)
	{
		GameRegistry.register(item);
	}
}
