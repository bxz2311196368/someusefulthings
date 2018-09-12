package com.bxzmod.someusefulthings.recipes;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

import java.util.HashMap;

public class BXZFuelHandler implements IFuelHandler
{
	private static HashMap<ItemStackData, Integer> Fuel = Maps.newHashMap();

	public static BXZFuelHandler instacnce = new BXZFuelHandler();

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		return Fuel.containsKey(new ItemStackData(fuel, fuel.getMetadata()))
				? Fuel.get(new ItemStackData(fuel, fuel.getMetadata()))
				: 0;
	}

	public static void addFuel(ItemStack stack, int burntime)
	{
		Fuel.put(new ItemStackData(stack, stack.getMetadata()), burntime);
	}

	public static class ItemStackData
	{
		private String item;
		private int meta;

		public ItemStackData(ItemStack item, int meta)
		{
			super();
			this.item = item.getItem().getRegistryName().toString();
			this.meta = meta;
		}

		public String getItem()
		{
			return item;
		}

		public int getMeta()
		{
			return meta;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			result = prime * result + meta;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ItemStackData other = (ItemStackData) obj;
			if (item == null)
			{
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			if (meta != other.meta)
				return false;
			return true;
		}
	}
}
