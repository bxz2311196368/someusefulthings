package com.bxzmod.someusefulthings.blocks;

import javax.annotation.Nullable;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobSummon extends BaseBlockContainer
{

	public MobSummon()
	{
		this.setUnlocalizedName("mobSummon");
		this.setRegistryName("mob_summon");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new MobSummonTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking())
		{
			MobSummonTileEntity te = (MobSummonTileEntity) worldIn.getTileEntity(pos);
			te.setSelectEntity("");
			return false;
		}
		if (!worldIn.isRemote)
		{
			int id = GuiLoader.GUI_M_S;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
