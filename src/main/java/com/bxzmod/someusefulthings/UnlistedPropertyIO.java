package com.bxzmod.someusefulthings;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyIO implements IUnlistedProperty<EnumIO>
{
	private EnumIO io;

	public UnlistedPropertyIO(EnumIO io)
	{
		this.io = io;
	}

	@Override
	public String getName()
	{
		return this.io.getName();
	}

	@Override
	public boolean isValid(EnumIO value)
	{
		return true;
	}

	@Override
	public Class<EnumIO> getType()
	{
		return EnumIO.class;
	}

	@Override
	public String valueToString(EnumIO value)
	{
		return value.getName();
	}

}
