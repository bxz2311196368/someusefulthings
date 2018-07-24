package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.tileentity.ChunkLoaderTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ChunkLoader extends BaseBlockContainer
{

	public ChunkLoader()
	{
		super("chunk_loader", "chunkLoader");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new ChunkLoaderTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity te = worldIn.getTileEntity(pos);
		if (placer != null && placer instanceof EntityPlayer && te != null && te instanceof ChunkLoaderTileEntity)
			((ChunkLoaderTileEntity) te).setPlayer(placer.getName());
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return player.getName().equals(((ChunkLoaderTileEntity) world.getTileEntity(pos)).getPlayer());
	}

}
