package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.FastFurnaceTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FastFurnace extends BaseIOBlockContainer
{

	public FastFurnace()
	{
		super("fast_furnace", "fastFurnace");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new FastFurnaceTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			int id = GuiLoader.GUI_F_F;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
