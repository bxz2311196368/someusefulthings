package com.bxzmod.someusefulthings;

import net.minecraftforge.fml.common.Loader;

public class ModLoadFlag
{
	public static boolean isIC2Load = false;
	public static boolean isMekLoad = false;
	public static boolean isTeslaLoad = false;

	public ModLoadFlag()
	{
		isIC2Load = Loader.isModLoaded("IC2");
		isMekLoad = Loader.isModLoaded("Mekanism");
		isTeslaLoad = Loader.isModLoaded("tesla");
	}

}
