package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ReinforcementMachineTileEntity extends TileEntityBase
{

	public ReinforcementMachineTileEntity()
	{
		super(new ItemStackHandlerModify(2, 1).setSlotChecker(0,
			stack -> stack.getItem().isDamageable() && !(stack.hasTagCompound() && stack.getTagCompound()
				.hasKey("Unbreakable"))).setSlotChecker(1, stack -> stack.getItem().equals(Items.NETHER_STAR))
			.getInventory(), new DefaultSide());
	}

	@Override
	public void work()
	{
		if (!this.world.isRemote)
		{
			ItemStack itemStack_0 = iInventory.removeStackInSlot(0, 1, true);
			ItemStack itemStack_1 = iInventory.removeStackInSlot(1, 1, true);
			ItemStack itemStack_2 = new ItemStack(Items.DIAMOND_BOOTS);
			IBlockState state = this.world.getBlockState(pos);

			if (itemStack_0 != null && itemStack_1 != null && iInventory.addStackInSlot(2, itemStack_2, true) == null)
			{
				if (itemStack_0.hasTagCompound())
				{
					if (itemStack_0.getTagCompound().getByte("Unbreakable") != (byte) 1)
					{
						itemStack_0.getTagCompound().setByte("Unbreakable", (byte) 1);
						iInventory.removeStackInSlot(1, 1, false);
						itemStack_2 = iInventory.removeStackInSlot(0, 1, false);
						iInventory.addStackInSlot(2, itemStack_2, false);
						this.markDirty();
					}

				} else
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setByte("Unbreakable", (byte) 1);
					itemStack_0.setTagCompound(nbt);
					iInventory.removeStackInSlot(1, 1, false);
					itemStack_2 = iInventory.removeStackInSlot(0, 1, false);
					iInventory.addStackInSlot(2, itemStack_2, false);
					this.markDirty();
				}
			}
		}
	}

}
