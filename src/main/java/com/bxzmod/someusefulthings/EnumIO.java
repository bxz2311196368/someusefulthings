package com.bxzmod.someusefulthings;

import net.minecraft.util.IStringSerializable;

public enum EnumIO implements IStringSerializable
{
	INPUT("input"), OUTPUT("output"), ALL("all"), NOT("not");

	private String name;

	private EnumIO(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

}
