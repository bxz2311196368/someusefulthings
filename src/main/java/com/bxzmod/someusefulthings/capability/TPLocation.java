package com.bxzmod.someusefulthings.capability;

import com.bxzmod.someusefulthings.Helper;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.ArrayList;

public class TPLocation
{
	public static class Storage implements Capability.IStorage<ITPLocation>
	{

		@Override
		public NBTBase writeNBT(Capability<ITPLocation> capability, ITPLocation instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ITPLocation> capability, ITPLocation instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}

	}

	public static class Implementation implements ITPLocation
	{
		private ArrayList<BlockPosWithDimAndName> list_pos = Lists.newArrayList();

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("tp_pos", this.toNBT());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.toList((NBTTagList) nbt.getTag("tp_pos"));
		}

		@Override
		public BlockPosWithDimAndName getPos(int index)
		{
			return list_pos.get(index);
		}

		@Override
		public void addPos(BlockPosWithDimAndName pos)
		{
			list_pos.add(pos);
		}

		@Override
		public void delPos(int index)
		{
			list_pos.remove(index);
		}

		@Override
		public void rename(int index, String name)
		{
			list_pos.get(index).setName(name);
		}

		@Override
		public String getName(int index)
		{
			if (index >= list_pos.size())
				return "";
			else
				return list_pos.get(index).getName();
		}

		public int getPosAmount()
		{
			return list_pos.size();
		}

		private NBTTagList toNBT()
		{
			NBTTagList nbt = new NBTTagList();
			if (!list_pos.isEmpty())
				for (BlockPosWithDimAndName o : list_pos)
				{
					NBTTagCompound temp = new NBTTagCompound();
					temp.setString("name", o.getName());
					temp.setInteger("dim", o.dim);
					temp.setString("pos", o.pos.toString());
					nbt.appendTag(temp);
				}
			return nbt;
		}

		private void toList(NBTTagList nbt)
		{
			list_pos.clear();
			if (nbt.tagCount() == 0)
				return;
			for (int i = 0; i < nbt.tagCount(); i++)
			{
				NBTTagCompound temp = nbt.getCompoundTagAt(i);
				list_pos.add(new BlockPosWithDimAndName(Helper.getBlockPosFromstring(temp.getString("pos")),
						temp.getInteger("dim"), temp.getString("name")));
			}
		}

	}

	public static class ProviderPlayer implements ICapabilitySerializable<NBTTagCompound>
	{
		private ITPLocation tpLocation = new Implementation();
		private IStorage<ITPLocation> storage = CapabilityLoader.TP_MENU.getStorage();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return CapabilityLoader.TP_MENU.equals(capability);
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if (CapabilityLoader.TP_MENU.equals(capability))
			{
				T result = (T) tpLocation;
				return result;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound compound = (NBTTagCompound) storage.writeNBT(CapabilityLoader.TP_MENU, tpLocation, null);
			return compound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound compound)
		{
			storage.readNBT(CapabilityLoader.TP_MENU, tpLocation, null, compound);
		}

	}

}
