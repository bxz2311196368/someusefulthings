package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.XPReservoirTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class XPReservoir extends BaseBlockContainer
{

	protected XPReservoir()
	{
		this.setUnlocalizedName("xpReservoir");
		this.setRegistryName("xp_reservoir");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new XPReservoirTileEntity();
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.025F);
		ItemStack itemstack = this.getSilkTouchDrop(state);
		if (te != null && te instanceof XPReservoirTileEntity)
			itemstack.setTagCompound(((XPReservoirTileEntity) te).setNBTFromData());
		spawnAsEntity(worldIn, pos, itemstack);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && stack.getTagCompound() != null && te instanceof XPReservoirTileEntity)
			((XPReservoirTileEntity) te).setDataFromNBT(stack.getTagCompound());
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && !(playerIn instanceof FakePlayer))
		{
			int id = GuiLoader.GUI_X_P;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}
