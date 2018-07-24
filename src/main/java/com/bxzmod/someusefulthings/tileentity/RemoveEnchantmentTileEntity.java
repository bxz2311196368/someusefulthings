package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class RemoveEnchantmentTileEntity extends TileEntityBase
{

	public RemoveEnchantmentTileEntity()
	{
		super(new ItemStackHandlerModify(2, 2).setSlotChecker(0, stack -> stack.isItemEnchanted())
			.setSlotChecker(1, stack -> stack.getItem().equals(Items.BOOK)).getInventory(), new DefaultSide());
	}

	@Override
	public void work()
	{
		if (!this.world.isRemote)
		{
			ItemStack itemStack_0 = iInventory.removeStackInSlot(0, 1, true);
			ItemStack itemStack_1 = iInventory.removeStackInSlot(1, 1, true);
			ItemStack itemStack_2 = new ItemStack(Items.ENCHANTED_BOOK);
			ItemStack itemStack_3 = new ItemStack(Items.DIAMOND_BOOTS);
			IBlockState state = this.world.getBlockState(pos);

			if (itemStack_0 != null && itemStack_1 != null && iInventory.addStackInSlot(2, itemStack_2, true) == null
				&& iInventory.addStackInSlot(3, itemStack_3, true) == null)
			{
				if (itemStack_0.isItemEnchanted())
				{
					NBTTagList list = itemStack_0.getEnchantmentTagList();
					if (list.tagCount() > 1)
					{
						NBTTagCompound ench = list.getCompoundTagAt(0);
						list.removeTag(0);
						NBTTagList list1 = new NBTTagList();
						list1.appendTag(ench);
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setTag("StoredEnchantments", list1);
						itemStack_2.setTagCompound(nbt);
						int amount = itemStack_1.stackSize;
						if (amount >= 1)
							iInventory.removeStackInSlot(1, 1, false);
						iInventory.addStackInSlot(2, itemStack_2, false);
						this.markDirty();
					} else
					{
						if (list.tagCount() == 1)
						{
							NBTTagCompound ench = list.getCompoundTagAt(0);
							list.removeTag(0);
							NBTTagList list1 = new NBTTagList();
							list1.appendTag(ench);
							NBTTagCompound nbt = new NBTTagCompound();
							nbt.setTag("StoredEnchantments", list1);
							itemStack_2.setTagCompound(nbt);
							int amount = itemStack_1.stackSize;
							if (amount >= 1)
								iInventory.removeStackInSlot(1, 1, false);
							iInventory.addStackInSlot(2, itemStack_2, false);
						}
						itemStack_0.getTagCompound().removeTag("ench");
						if (itemStack_0.getTagCompound().hasNoTags())
						{
							itemStack_0.setTagCompound((NBTTagCompound) null);
						}
						itemStack_3 = iInventory.removeStackInSlot(0, 1, false);
						iInventory.addStackInSlot(3, itemStack_3, false);
						this.markDirty();
					}
				}
			}
		}
	}

}
