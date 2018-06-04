package com.bxzmod.someusefulthings.capability;

import com.bxzmod.someusefulthings.TicketManager.BlockPosWithDim;
import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITPLocation extends INBTSerializable<NBTTagCompound>
{
	public BlockPosWithDimAndName getPos(int index);

	public void addPos(BlockPosWithDimAndName pos);

	public void delPos(int index);

	public void rename(int index, String name);

	public String getName(int index);

	public int getPosAmount();

	public static class BlockPosWithDimAndName extends BlockPosWithDim
	{
		public String name;

		public BlockPosWithDimAndName(BlockPos pos, int dim, String name)
		{
			super(pos, dim);
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			BlockPosWithDimAndName other = (BlockPosWithDimAndName) obj;
			if (name == null)
			{
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return Objects.toStringHelper("BlockPosWithDimAndName").add("dim", dim).add("x", pos.getX())
					.add("y", pos.getY()).add("z", pos.getZ()).add("name", name).toString();
		}

	}
}
