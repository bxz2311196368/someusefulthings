package com.bxzmod.someusefulthings.blocks.property;

import net.minecraft.util.IStringSerializable;

public enum EnumIO implements IStringSerializable
{
	INPUT("input"), OUTPUT("output"), ALL("all"), NOT("not");

	private String name;

	EnumIO(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public static EnumIO getIOByName(String name)
	{
		for (EnumIO io : values())
			if (name.equalsIgnoreCase(io.name))
				return io;
		return INPUT;
	}

	public static boolean isEnumIO(String name)
	{
		for (EnumIO io : values())
			if (io.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}

	public EnumIO next()
	{
		return this.ordinal() >= 3 ? INPUT : values()[this.ordinal() + 1];
	}

	public boolean canInput()
	{
		return this.equals(INPUT) || this.equals(ALL);
	}

	public boolean canOutput()
	{
		return this.equals(OUTPUT) || this.equals(ALL);
	}

}
