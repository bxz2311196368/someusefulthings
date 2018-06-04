package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.EnumIO;
import com.bxzmod.someusefulthings.UnlistedPropertyIO;
import com.bxzmod.someusefulthings.core.energy.IEnergySide;
import com.bxzmod.someusefulthings.tileentity.EnergyBlockTileEntity;
import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class EnergyBlock extends BaseBlockContainer
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.values()));
	public static final PropertyEnum<EnumIO> UP = PropertyEnum.create("up", EnumIO.class);
	public static final PropertyEnum<EnumIO> DOWN = PropertyEnum.create("down", EnumIO.class);
	public static final PropertyEnum<EnumIO> NORTH = PropertyEnum.create("north", EnumIO.class);
	public static final PropertyEnum<EnumIO> SOUTH = PropertyEnum.create("south", EnumIO.class);
	public static final PropertyEnum<EnumIO> EAST = PropertyEnum.create("east", EnumIO.class);
	public static final PropertyEnum<EnumIO> WEST = PropertyEnum.create("west", EnumIO.class);

	public EnergyBlock()
	{
		super();
		this.setRegistryName("energy_block");
		this.setUnlocalizedName("energyBlock");
		this.setDefaultState(this.createBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new EnergyBlockTileEntity();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int a;
		a = meta % 3;
		EnumFacing facing = EnumFacing.getHorizontal(a);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int facing = state.getValue(FACING).getIndex();
		return facing;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
	{
		IBlockState origin = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
		return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
//		return new ExtendedBlockState(this, new IProperty[] { FACING, NORTH, SOUTH, WEST, EAST, UP, DOWN },
//				new IUnlistedProperty[] {});
		return new BlockStateContainer(this, FACING, NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

//	@Override
//	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
//	{
//		TileEntity te = world.getTileEntity(pos);
//		//IExtendedBlockState s = (IExtendedBlockState) state;
//		//state.withProperty(FACING, state.getValue(FACING));
//		if (te instanceof IEnergySide)
//		{
//			IEnergySide config = (IEnergySide) te;
//			System.out.println(Booleans.asList(config.getAllEnergySide()).toString());
//			for(EnumFacing side:EnumFacing.VALUES)
//				state.withProperty(this.getProperty(side), config.canReceive(side) ? EnumIO.INPUT : EnumIO.OUTPUT);
//		}
//		return super.getExtendedState(state, world, pos);
//	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IEnergySide)
		{
			IEnergySide config = (IEnergySide) te;
			//System.out.println(Booleans.asList(config.getAllEnergySide()).toString());
			for(EnumFacing side:EnumFacing.VALUES)
				state.withProperty(this.getProperty(side), config.canReceive(side) ? EnumIO.INPUT : EnumIO.OUTPUT);
		}
		return state;
	}

	public PropertyEnum<EnumIO> getProperty(EnumFacing from)
	{
		switch (from)
		{
		case UP:
			return this.UP;
		case DOWN:
			return this.DOWN;
		case NORTH:
			return this.NORTH;
		case SOUTH:
			return this.SOUTH;
		case WEST:
			return this.WEST;
		case EAST:
			return this.EAST;
		default:
			return null;
		}

	}

}
