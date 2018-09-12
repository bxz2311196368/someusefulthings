package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.blocks.model.BXZModelLoader;
import com.bxzmod.someusefulthings.blocks.property.BlockPropertys;
import com.bxzmod.someusefulthings.tileentity.TileEntityBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class BaseIOBlockContainer extends BaseBlockContainer
{

	public BaseIOBlockContainer(String registryName, String unlocalizedName)
	{
		super(registryName, unlocalizedName);
		this.setCustomModel(registryName);
		this.setDefaultState(this.blockState.getBaseState());
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {},
			new IUnlistedProperty[] { BlockPropertys.IO_NORTH, BlockPropertys.IO_SOUTH, BlockPropertys.IO_WEST,
				BlockPropertys.IO_EAST, BlockPropertys.IO_UP, BlockPropertys.IO_DOWN, BlockPropertys.FACING });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (state instanceof IExtendedBlockState && te instanceof TileEntityBase)
		{
			IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
			TileEntityBase config = (TileEntityBase) te;
			for (EnumFacing side : EnumFacing.values())
				extendedBlockState = extendedBlockState
					.withProperty(BlockPropertys.getPropertyIOFromFacing(side), config.getSideIO(side));
			extendedBlockState = extendedBlockState.withProperty(BlockPropertys.FACING, config.getFacing());
			return extendedBlockState;
		}
		return state;
	}

	protected void setCustomModel(String name)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			BXZModelLoader.customModelBlock.add(name);
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		IExtendedBlockState state = (IExtendedBlockState) world.getBlockState(pos);
		TileEntity te = world.getTileEntity(pos);
		EnumFacing facing = EnumFacing.NORTH;
		if (te instanceof TileEntityBase)
			facing = ((TileEntityBase) te).rotateY();
		world.notifyBlockUpdate(pos, state, state.withProperty(BlockPropertys.FACING, facing), 3);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
		ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityBase && state instanceof IExtendedBlockState)
		{
			EnumFacing facing = placer.getHorizontalFacing().getOpposite();
			((TileEntityBase) te).setFacing(facing);
			worldIn.notifyBlockUpdate(pos, state,
				((IExtendedBlockState) state).withProperty(BlockPropertys.FACING, facing), 3);
		}
	}
}
