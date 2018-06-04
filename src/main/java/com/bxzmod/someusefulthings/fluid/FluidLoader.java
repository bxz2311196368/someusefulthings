package com.bxzmod.someusefulthings.fluid;

import com.bxzmod.someusefulthings.Info;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FluidLoader
{
	public static final ResourceLocation wwwwater_still = new ResourceLocation(
			Info.MODID + ":" + "fluid/wwwwater_still");
	public static final ResourceLocation wwwwater_flowing = new ResourceLocation(
			Info.MODID + ":" + "fluid/wwwwater_flow");
	public static final ResourceLocation xp_still = new ResourceLocation(Info.MODID + ":" + "fluid/xp_still");
	public static final ResourceLocation xp_flowing = new ResourceLocation(Info.MODID + ":" + "fluid/xp_flow");

	public static Fluid fluidWwwwwater;

	public static Fluid fluidXP;

	public FluidLoader(FMLPreInitializationEvent event)
	{
		fluidWwwwwater = registerFluid(new FluidWwwwater("fluid_wwwwwater", wwwwater_still, wwwwater_flowing), event);
		fluidXP = registerFluid(new FluidXP("xpjuice", xp_still, xp_flowing), event);
		if (fluidXP == null)
			throw new RuntimeException("null fluid");
	}

	public static Fluid registerFluid(Fluid fluid, FMLPreInitializationEvent event)
	{
		if (FluidRegistry.isFluidRegistered(fluid))
		{
			fluid = FluidRegistry.getFluid(fluid.getName());
			event.getModLog().info("Found fluid {}, the registration is canceled. ", fluid.getName());
		} else
		{
			FluidRegistry.registerFluid(fluid);
		}
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}
}
