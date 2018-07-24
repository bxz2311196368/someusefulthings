package com.bxzmod.someusefulthings.blocks.property;

import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;

public class UnlistedPropertyIO implements IUnlistedProperty<EnumIO>
{
	private String name;
	private Collection<EnumIO> values;

	public UnlistedPropertyIO(String name, Collection<EnumIO> values)
	{
		this.name = name;
		this.values = values;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean isValid(EnumIO value)
	{
		return this.values.contains(value);
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
