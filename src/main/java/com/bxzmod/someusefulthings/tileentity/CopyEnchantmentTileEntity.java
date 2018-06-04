package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
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

public class CopyEnchantmentTileEntity extends TileEntity implements ITickable
{

	protected ItemStackHandlerModify iInventory = new ItemStackHandlerModify(4, new int[] { 0, 1, 2 },
			new int[] { 3 }).setSlotChecker(0, stack -> stack.getItem().equals(Items.ENCHANTED_BOOK))
					.setSlotChecker(1, stack -> stack.getItem().equals(Items.DYE) && stack.getMetadata() == 4)
					.setSlotChecker(2, stack -> stack.getItem().equals(Items.BOOK)).getInventory();

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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		return compound;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			this.iInventory.tryInAndOut(this.world, this.pos);
			ItemStack itemStack_0 = iInventory.getStackInSlot(0);
			ItemStack itemStack_1 = iInventory.removeStackInSlot(1, 16, true);
			ItemStack itemStack_2 = iInventory.removeStackInSlot(2, 1, true);

			if (itemStack_0 != null && itemStack_1 != null && itemStack_2 != null
					&& iInventory.addStackInSlot(3, itemStack_0, true) == null && itemStack_1.stackSize == 16)
			{

				iInventory.removeStackInSlot(1, 16, false);
				iInventory.removeStackInSlot(2, 1, false);
				iInventory.addStackInSlot(3, Helper.copyStack(itemStack_0, 1), false);
				this.markDirty();
			}
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

}
