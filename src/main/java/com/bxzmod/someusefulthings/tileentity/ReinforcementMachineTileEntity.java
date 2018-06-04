package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.blocks.ReinforcementMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ReinforcementMachineTileEntity extends TileEntity implements ITickable
{

	private static final int workTotalTime = 20;

	public ReinforcementMachineTileEntity()
	{

	}

	protected int workTime = 0;

	protected ItemStackHandlerMe iInventory = new ItemStackHandlerMe(3);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			T result = (T) iInventory;
			return result;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.iInventory.deserializeNBT(compound.getCompoundTag("iInventory"));
		this.workTime = compound.getInteger("WorkTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		compound.setInteger("WorkTime", this.workTime);
		return compound;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			ItemStack itemStack_0 = iInventory.extractItemMe(0, 1, true);
			ItemStack itemStack_1 = iInventory.extractItemMe(1, 1, true);
			ItemStack itemStack_2 = new ItemStack(Items.DIAMOND_BOOTS);
			IBlockState state = this.world.getBlockState(pos);

			if (itemStack_0 != null && itemStack_1 != null && iInventory.insertItemMe(2, itemStack_2, true) == null)
			{
				this.world.setBlockState(pos, state.withProperty(ReinforcementMachine.WORK, Boolean.TRUE));
				if (++this.workTime >= workTotalTime)
				{
					this.workTime = 0;
					if (itemStack_0.hasTagCompound())
					{
						if (itemStack_0.getTagCompound().getByte("Unbreakable") != (byte) 1)
						{
							itemStack_0.getTagCompound().setByte("Unbreakable", (byte) 1);
							iInventory.extractItemMe(1, 1, false);
							itemStack_2 = iInventory.extractItemMe(0, 1, false);
							iInventory.insertItemMe(2, itemStack_2, false);
							this.markDirty();
						}

					} else
					{
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setByte("Unbreakable", (byte) 1);
						itemStack_0.setTagCompound(nbt);
						iInventory.extractItemMe(1, 1, false);
						itemStack_2 = iInventory.extractItemMe(0, 1, false);
						iInventory.insertItemMe(2, itemStack_2, false);
						this.markDirty();
					}
				}
			} else
			{
				this.world.setBlockState(pos, state.withProperty(ReinforcementMachine.WORK, Boolean.FALSE));
				this.workTime = 0;
			}
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	public int getWorkTime()
	{
		return this.workTime;
	}

	public int getTotalWorkTime()
	{
		return this.workTotalTime;
	}

	public class ItemStackHandlerMe extends ItemStackHandler
	{

		public ItemStackHandlerMe()
		{
			super();
		}

		public ItemStackHandlerMe(int size)
		{
			super(size);
		}

		public ItemStackHandlerMe(ItemStack[] stacks)
		{
			super(stacks);

		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (slot == 0 && !(stack.getItem().isDamageable()
					&& !(stack.hasTagCompound() && stack.getTagCompound().getByte("Unbreakable") == (byte) 1)))
				return stack;
			if (slot == 1 && stack.getItem() != Items.NETHER_STAR)
				return stack;
			if (slot == 2)
				return stack;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			if (slot == 2)
				return super.extractItem(slot, amount, simulate);
			return null;
		}

		public ItemStack insertItemMe(int slot, ItemStack stack, boolean simulate)
		{
			return super.insertItem(slot, stack, simulate);
		}

		public ItemStack extractItemMe(int slot, int amount, boolean simulate)
		{
			return super.extractItem(slot, amount, simulate);
		}
	}

}
