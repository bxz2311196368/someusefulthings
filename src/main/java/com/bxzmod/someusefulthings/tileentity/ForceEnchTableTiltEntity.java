package com.bxzmod.someusefulthings.tileentity;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;

public class ForceEnchTableTiltEntity extends TileEntityBase
{

	public ForceEnchTableTiltEntity()
	{
		super(new ItemStackHandlerModify(2, 1)
				.setSlotChecker(0, stack -> stack.isItemEnchantable() || stack.isItemEnchanted())
				.setSlotChecker(1,
						stack -> stack.getItem().equals(Items.ENCHANTED_BOOK) && stack.hasTagCompound()
								&& stack.getTagCompound().hasKey("StoredEnchantments")
								&& !stack.getTagCompound().getTag("StoredEnchantments").hasNoTags())
				.getInventory());
	}

	@Override
	public void work()
	{
		this.iInventory.tryInAndOut(this.world, this.pos);
		ItemStack itemStack_0 = iInventory.removeStackInSlot(0, 1, true);
		ItemStack itemStack_1 = iInventory.removeStackInSlot(1, 1, true);
		NBTTagList itemEnch, temp_item, bookEnch, temp_book;

		if (itemStack_0 != null && itemStack_1 != null && iInventory.addStackInSlot(2, itemStack_0, true) == null)
		{
			bookEnch = (NBTTagList) itemStack_1.getTagCompound().getTag("StoredEnchantments").copy();
			if (itemStack_0.isItemEnchanted())
			{
				itemEnch = itemStack_0.getEnchantmentTagList().copy();
				temp_item = itemEnch.copy();
				int i = itemEnch.tagCount(), j = bookEnch.tagCount();
				for (int m = 0; m < j; m++)
				{
					NBTTagCompound ench_book = bookEnch.getCompoundTagAt(m);
					short book_id = ench_book.getShort("id"), book_lvl = ench_book.getShort("lvl");
					boolean add_lvl = false;
					for (int n = 0; n < i; n++)
					{

						NBTTagCompound ench_item = temp_item.getCompoundTagAt(n);
						short item_id = ench_item.getShort("id"), item_lvl = ench_item.getShort("lvl");
						if (item_id == book_id)
						{
							ench_item.setShort("lvl",
									book_lvl > item_lvl ? book_lvl : book_lvl == item_lvl ? ++book_lvl : item_lvl);
							add_lvl = true;
							break;
						}
					}
					if (!add_lvl)
					{
						temp_item.appendTag(ench_book);
					}
				}
			} else
				temp_item = bookEnch;
			NBTTagCompound nbtench = new NBTTagCompound();
			nbtench.setTag("ench", temp_item);
			itemStack_0.setTagCompound(nbtench);
			iInventory.addStackInSlot(2, itemStack_0, false);
			iInventory.removeStackInSlot(0, 1, false);
			iInventory.removeStackInSlot(1, 1, false);
		}

	}
}
