package com.bxzmod.someusefulthings.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidWwwwater extends Fluid
{

	public FluidWwwwater(String fluidName, ResourceLocation still, ResourceLocation flowing)
	{
		super(fluidName, still, flowing);
		this.setUnlocalizedName("fluidWwwwwater");
		this.setDensity(1000);
		this.setViscosity(1000);
		this.setLuminosity(0);
		this.setTemperature(300);

	}

	@Override
	public boolean doesVaporize(FluidStack fluidStack)
	{
		return false;
	}

}
