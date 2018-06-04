package com.bxzmod.someusefulthings.creativetabs;

import com.bxzmod.someusefulthings.items.ItemLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabsSomeUsefulThings extends CreativeTabs
{

	public CreativeTabsSomeUsefulThings(String label)
	{
		super(label);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return ItemLoader.invinciblering;
	}

}
