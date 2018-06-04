package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.blocks.itemblock.UniversItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLoader
{
	public static Block removeEnchantment = new RemoveEnchantment();
	public static UniversItemBlock removeEnchantmentItemBlock = new UniversItemBlock(removeEnchantment);
	public static Block copyEnchantment = new CopyEnchantment();
	public static UniversItemBlock copyEnchantmentItemBlock = new UniversItemBlock(copyEnchantment);
	public static Block reinforcementMachine = new ReinforcementMachine();
	public static UniversItemBlock reinforcementMachineItemBlock = new UniversItemBlock(reinforcementMachine);
	public static Block forceEnchTable = new ForceEnchTable();
	public static UniversItemBlock forceEnchTableItemBlock = new UniversItemBlock(forceEnchTable);
	public static Block tank = new TankBlock();
	public static UniversItemBlock tankItemBlock = new UniversItemBlock(tank);
	public static Block eyeGenerator = new EyeGenerator();
	public static UniversItemBlock eyeGeneratorItemBlock = new UniversItemBlock(eyeGenerator);
	public static Block replacementMachine = new ReplacementMachine();
	public static UniversItemBlock replacementMachineItemBlock = new UniversItemBlock(replacementMachine);
	public static Block cobbleStoneMaker = new CobbleStoneMaker();
	public static UniversItemBlock cobbleStoneMakerItemBlock = new UniversItemBlock(cobbleStoneMaker);
	public static Block infiniteWater = new InfiniteWater();
	public static UniversItemBlock infiniteWaterItemBlock = new UniversItemBlock(infiniteWater);
	public static Block craftingTable = new CraftingTable();
	public static UniversItemBlock craftingTableItemBlock = new UniversItemBlock(craftingTable);
	public static Block chunkLoader = new ChunkLoader();
	public static UniversItemBlock chunkLoaderItemBlock = new UniversItemBlock(chunkLoader);
	public static Block lavaPump = new LavaPump();
	public static UniversItemBlock lavaPumpItemBlock = new UniversItemBlock(lavaPump);
	public static Block xpReservoir = new XPReservoir();
	public static UniversItemBlock xpReservoirItemBlock = new UniversItemBlock(xpReservoir);
	public static Block greenHouse = new GreenHouse();
	public static UniversItemBlock greenHouseItemBlock = new UniversItemBlock(greenHouse);
	public static Block fastFurnace = new FastFurnace();
	public static UniversItemBlock fastFurnaceItemBlock = new UniversItemBlock(fastFurnace);
	public static Block mobSummon = new MobSummon();
	public static UniversItemBlock mobSummonItemBlock = new UniversItemBlock(mobSummon);
	public static Block energyBlock = new EnergyBlock();
	public static UniversItemBlock energyItemBlock = new UniversItemBlock(energyBlock);

	public BlockLoader(FMLPreInitializationEvent event)
	{
		registerBlock(removeEnchantment);
		registerItem(removeEnchantmentItemBlock, removeEnchantment);
		registerBlock(copyEnchantment);
		registerItem(copyEnchantmentItemBlock, copyEnchantment);
		registerBlock(reinforcementMachine);
		registerItem(reinforcementMachineItemBlock, reinforcementMachine);
		registerBlock(forceEnchTable);
		registerItem(forceEnchTableItemBlock, forceEnchTable);
		registerBlock(tank);
		registerItem(tankItemBlock, tank);
		registerBlock(eyeGenerator);
		registerItem(eyeGeneratorItemBlock, eyeGenerator);
		registerBlock(replacementMachine);
		registerItem(replacementMachineItemBlock, replacementMachine);
		registerBlock(cobbleStoneMaker);
		registerItem(cobbleStoneMakerItemBlock, cobbleStoneMaker);
		registerBlock(infiniteWater);
		registerItem(infiniteWaterItemBlock, infiniteWater);
		registerBlock(craftingTable);
		registerItem(craftingTableItemBlock, craftingTable);
		registerBlock(chunkLoader);
		registerItem(chunkLoaderItemBlock, chunkLoader);
		registerBlock(lavaPump);
		registerItem(lavaPumpItemBlock, lavaPump);
		registerBlock(xpReservoir);
		registerItem(xpReservoirItemBlock, xpReservoir);
		registerBlock(greenHouse);
		registerItem(greenHouseItemBlock, greenHouse);
		registerBlock(fastFurnace);
		registerItem(fastFurnaceItemBlock, fastFurnace);
		registerBlock(mobSummon);
		registerItem(mobSummonItemBlock, mobSummon);
		registerBlock(energyBlock);
		registerItem(energyItemBlock, energyBlock);
	}

	private static void registerBlock(Block block)
	{
		GameRegistry.register(block);
	}

	private static void registerItem(ItemBlock item, Block block)
	{
		item.setRegistryName(block.getRegistryName());
		GameRegistry.register(item);
	}
}
