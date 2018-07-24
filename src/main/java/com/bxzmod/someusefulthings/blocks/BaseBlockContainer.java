package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.blocks.property.BlockPropertys;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BaseBlockContainer extends BlockContainer
{
	public BaseBlockContainer(String registryName, String unlocalizedName)
	{
		super(Material.GRASS);
		this.setHardness(2.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
		this.setLightLevel(1);
		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
		ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.025F);
		if (te instanceof TileEntityBase)
		{
			ItemStack drop = new ItemStack(state.getBlock());
			drop.setTagCompound(((TileEntityBase) te).setNBTFromData(new NBTTagCompound()));
			spawnAsEntity(worldIn, pos, drop);
		} else
			super.harvestBlock(worldIn, player, pos, state, te, stack);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
		ItemStack stack)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(stack.hasTagCompound() && te instanceof TileEntityBase)
		{
			((TileEntityBase) te).setDataFromNBT(stack.getTagCompound());
		}
	}
}
