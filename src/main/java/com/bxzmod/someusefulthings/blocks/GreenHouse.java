package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.GreenHouseTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GreenHouse extends BaseBlockContainer
{

	public GreenHouse()
	{
		this.setUnlocalizedName("greenHouse");
		this.setRegistryName("green_house");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new GreenHouseTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		if (!worldIn.isRemote && pos.getY() < 10)
		{
			placer.sendMessage(new TextComponentTranslation("error.greenHouse.tooLow", TextFormatting.RED));
			((GreenHouseTileEntity) worldIn.getTileEntity(pos)).setWork(false);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (playerIn.isSneaking())
			{
				GreenHouseTileEntity te = (GreenHouseTileEntity) worldIn.getTileEntity(pos);
				te.cycleSets(playerIn);
			} else
			{
				int id = GuiLoader.GUI_G_H;
				playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		GreenHouseTileEntity te = (GreenHouseTileEntity) worldIn.getTileEntity(pos);
		for (int i = 0; i < te.getInv().getSizeInventory(); i++)
			if (te.getInv().getStackInSlot(i) != null)
				spawnAsEntity(worldIn, pos, te.getInv().getStackInSlot(i));
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
}
