package com.bxzmod.someusefulthings.fluid;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidWwwwater extends BlockFluidClassic
{

	public BlockFluidWwwwater()
	{
		super(FluidLoader.fluidWwwwwater, Material.WATER);
		this.setUnlocalizedName("fluidWwwwwater");
		this.setRegistryName("fluid_wwwwwater");
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

}
