package com.bxzmod.someusefulthings.fluid;

import com.bxzmod.someusefulthings.ModInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidRenderLoader
{

	public FluidRenderLoader(FMLPreInitializationEvent event)
	{
		registerRenders();

	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		registerFluidRender((BlockFluidBase) FluidLoaderHelper.fluidwwwwater, "fluid_wwwwwater");
		registerFluidRender((BlockFluidBase) FluidLoaderHelper.fluidxp, "fluid_xp");
	}

	@SideOnly(Side.CLIENT)
	public static void registerFluidRender(BlockFluidBase blockFluid, String blockStateName)
	{
		final String location = ModInfo.MODID + ":" + blockStateName;
		final Item itemFluid = Item.getItemFromBlock(blockFluid);
		ModelLoader.setCustomMeshDefinition(itemFluid, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return new ModelResourceLocation(location, "fluid");
			}
		});
		ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation(location, "fluid");
			}
		});
	}

}
