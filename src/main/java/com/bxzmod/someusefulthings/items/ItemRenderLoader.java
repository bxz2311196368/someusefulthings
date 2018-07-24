package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.ModInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRenderLoader
{

	public ItemRenderLoader(FMLPreInitializationEvent event)
	{
		registerRender(ItemLoader.invinciblering);
		registerRender(ItemLoader.limitlesstool);
		registerRender(ItemLoader.redstoneapple);
		registerRender(ItemLoader.artifactsword);
		registerRender(ItemLoader.compressedDiamondSword);
		registerRender(ItemLoader.garbagebag);
		registerRender(ItemLoader.replacementMachineSetter);
		registerRender(ItemLoader.spawnerChanger);
		registerRender(ItemLoader.portableCrafter);
		registerRender(ItemLoader.multiplePickaxes0);
		registerRender(ItemLoader.multiplePickaxes1);
		registerRender(ItemLoader.multiplePickaxes2);
		registerRender(ItemLoader.infiniteFuel);
		for (int i = 0; i < 16; i++)
		{
			registerRenderWithMeta(ItemLoader.portableInventoryItem, i, "portable_inventory_item");
		}
		registerRender(ItemLoader.wrench);
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Item item)
	{
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(item.getRegistryName(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
	}

	@SideOnly(Side.CLIENT)
	private static void registerRenderWithMeta(Item item, int meta, String name)
	{
		ResourceLocation location = new ResourceLocation(ModInfo.MODID,
				name + '_' + EnumDyeColor.byMetadata(meta).toString());
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(location, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, itemModelResourceLocation);
	}
}
