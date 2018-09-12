package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.item.Item;

public class InfiniteFuel extends Item
{

	public InfiniteFuel()
	{
		super();
		this.setUnlocalizedName("infiniteFuel");
		this.setRegistryName("infinite_fuel");
		this.setMaxDamage(0);
		this.setMaxStackSize(64);
		this.setContainerItem(this);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

}
