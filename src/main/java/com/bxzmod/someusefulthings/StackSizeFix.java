package com.bxzmod.someusefulthings;

import net.minecraft.init.Items;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.Loader;
import sonar.calculator.mod.Calculator;

public class StackSizeFix
{

	public StackSizeFix()
	{
		Items.EGG.maxStackSize = 64;
		Items.ENDER_PEARL.maxStackSize = 64;
		Items.BUCKET.maxStackSize = 64;
		Items.POTIONITEM.maxStackSize = 64;
		ForgeModContainer.getInstance().universalBucket.maxStackSize = 64;
		Items.LAVA_BUCKET.maxStackSize = 64;
		Items.MILK_BUCKET.maxStackSize = 64;
		Items.WATER_BUCKET.maxStackSize = 64;
		Items.LINGERING_POTION.maxStackSize = 64;
		Items.SPLASH_POTION.maxStackSize = 64;
		Items.ENCHANTED_BOOK.maxStackSize = 64;

		if (Loader.isModLoaded("calculator"))
		{
			Calculator.circuitBoard.maxStackSize = 64;
			Calculator.circuitDamaged.maxStackSize = 64;
			Calculator.circuitDirty.maxStackSize = 64;
		}

	}

}
