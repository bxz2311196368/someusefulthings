package com.bxzmod.someusefulthings.blocks.property;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;

public class BlockPropertys
{
	public static UnlistedPropertyIO IO_UP = new UnlistedPropertyIO(EnumFacing.UP.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyIO IO_DOWN = new UnlistedPropertyIO(EnumFacing.DOWN.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyIO IO_NORTH = new UnlistedPropertyIO(EnumFacing.NORTH.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyIO IO_SOUTH = new UnlistedPropertyIO(EnumFacing.SOUTH.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyIO IO_EAST = new UnlistedPropertyIO(EnumFacing.EAST.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyIO IO_WEST = new UnlistedPropertyIO(EnumFacing.WEST.getName(),
		Arrays.asList(EnumIO.values()));
	public static UnlistedPropertyFacing FACING = new UnlistedPropertyFacing("facing");

	public static IUnlistedProperty<EnumIO> getPropertyIOFromFacing(EnumFacing side)
	{
		switch (side)
		{
		case DOWN:
			return IO_DOWN;
		case UP:
			return IO_UP;
		case NORTH:
			return IO_NORTH;
		case EAST:
			return IO_EAST;
		case WEST:
			return IO_WEST;
		case SOUTH:
			return IO_SOUTH;
		default:
			return IO_NORTH;
		}
	}

}
