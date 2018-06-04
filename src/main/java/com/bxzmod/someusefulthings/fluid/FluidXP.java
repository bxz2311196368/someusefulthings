package com.bxzmod.someusefulthings.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidXP extends Fluid
{

	public FluidXP(String fluidName, ResourceLocation still, ResourceLocation flowing)
	{
		super(fluidName, still, flowing);
		this.setUnlocalizedName("fluidXp");
		this.setDensity(800);
		this.setViscosity(1500);
		this.setLuminosity(10);
		this.setTemperature(300);
	}

	@Override
	public boolean doesVaporize(FluidStack fluidStack)
	{
		return false;
	}

}
