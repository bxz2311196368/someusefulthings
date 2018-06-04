package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.item.Item;

public class CompressedDiamondSword extends Item
{

	public CompressedDiamondSword()
	{
		super();
		this.setUnlocalizedName("compressedDiamondSword");
		this.setRegistryName("compressed_diamond_sword");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

}
