package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Info;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityLoader
{

	public TileEntityLoader(FMLPreInitializationEvent event)
	{
		registerTileEntity(RemoveEnchantmentTileEntity.class, "RemoveEnchantment");
		registerTileEntity(CopyEnchantmentTileEntity.class, "CopyEnchantment");
		registerTileEntity(ReinforcementMachineTileEntity.class, "ReinforcementMachine");
		registerTileEntity(ForceEnchTableTiltEntity.class, "ForceEnchTable");
		registerTileEntity(TankTileEntity.class, "Tank");
		registerTileEntity(EyeGeneratorTileEntity.class, "EyeGenerator");
		registerTileEntity(ReplacementMachineTileEntity.class, "ReplacementMachine");
		registerTileEntity(CobbleStoneMakerTiletEntity.class, "CobbleStoneMaker");
		registerTileEntity(InfiniteWaterTileEntity.class, "InfiniteWater");
		registerTileEntity(CraftingTableTileEntity.class, "CraftingTable");
		registerTileEntity(ChunkLoaderTileEntity.class, "ChunkLoader");
		registerTileEntity(LavaPumpTileEntity.class, "LavaPump");
		registerTileEntity(XPReservoirTileEntity.class, "XPReservoir");
		registerTileEntity(GreenHouseTileEntity.class, "GreenHouse");
		registerTileEntity(FastFurnaceTileEntity.class, "FastFurnace");
		registerTileEntity(MobSummonTileEntity.class, "MobSummon");
		registerTileEntity(EnergyBlockTileEntity.class, "EnergyBlock");
	}

	public void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id)
	{
		GameRegistry.registerTileEntity(tileEntityClass, Info.MODID + ":" + id);
	}

}
