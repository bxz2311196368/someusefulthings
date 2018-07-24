package com.bxzmod.someusefulthings.blocks.property;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyFacing implements IUnlistedProperty<EnumFacing>
{
	String name;

	public UnlistedPropertyFacing(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean isValid(EnumFacing value)
	{
		return value.getHorizontalIndex() > -1;
	}

	@Override
	public Class<EnumFacing> getType()
	{
		return EnumFacing.class;
	}

	@Override
	public String valueToString(EnumFacing value)
	{
		return value.getName();
	}
}
