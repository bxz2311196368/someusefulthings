package com.bxzmod.someusefulthings.items.tools;

import com.bxzmod.someusefulthings.blocks.BaseIOBlockContainer;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
				if (!player.isSneaking())
				{
					world.getBlockState(pos).getBlock().rotateBlock(world, pos, side);
					return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
				}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.getItem().equals(this))
		{
			if (!worldIn.isAirBlock(pos))
				if (playerIn.isSneaking())
				{
					IBlockState state = worldIn.getBlockState(pos);
					Block block = state.getBlock();
					if (block instanceof BaseIOBlockContainer)
					{
						TileEntityBase te = (TileEntityBase) worldIn.getTileEntity(pos);
						te.cycleSideIO(facing);
						worldIn.notifyBlockUpdate(pos,state, block.getExtendedState(state, worldIn, pos), 3);
					}
				}
		}
		return EnumActionResult.SUCCESS;
	}

	@Nullable
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
