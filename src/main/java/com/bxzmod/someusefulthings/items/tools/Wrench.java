package com.bxzmod.someusefulthings.items.tools;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Wrench extends Item
{
	public Wrench()
	{
		this.setUnlocalizedName("wrench");
		this.setRegistryName("wrench");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
		EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (stack.getItem().equals(this))
		{
			if (!world.isAirBlock(pos))
			{
				world.getBlockState(pos).getBlock().rotateBlock(world, pos, side);
				return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}
}
