package com.bxzmod.someusefulthings.fluid;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidXP extends BlockFluidClassic
{

	public BlockFluidXP()
	{
		super(FluidLoader.fluidXP, Material.WATER);
		this.setUnlocalizedName("fluidXp");
		this.setRegistryName("fluid_xp");
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

}
