package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class FastFurnaceTileEntity extends TileEntityBase
{
	public FastFurnaceTileEntity()
	{
		super(new ItemStackHandlerModify(1, 1)
			.setSlotChecker(0, stack -> FurnaceRecipes.instance().getSmeltingResult(stack) != null || (
				stack.getItem().equals(Items.IRON_INGOT) && OreDictionary.doesOreNameExist("ingotSteel")))
			.getInventory(), new DefaultSide());
	}

	@Override
	public void work()
	{
		ItemStack input = this.iInventory.getStackInSlot(0), output = this.iInventory.getStackInSlot(1);
		if (input != null && input.stackSize > 0)
		{
			ItemStack smeltItem;
			if (input.getItem().equals(Items.IRON_INGOT) && OreDictionary.doesOreNameExist("ingotSteel"))
			{
				smeltItem = OreDictionary.getOres("ingotSteel").get(0);
			} else
			{
				smeltItem = FurnaceRecipes.instance().getSmeltingResult(input);
			}
			if (smeltItem != null)
			{
				if (output != null)
				{
					if (Helper.isSameStackIgnoreAmount(output, smeltItem))
					{
						int temp = Math.min(input.stackSize,
								(output.getMaxStackSize() - output.stackSize) / smeltItem.stackSize);
						input.stackSize -= temp;
						output.stackSize += temp * smeltItem.stackSize;
					}
				} else
				{
					int temp = Math.min(input.stackSize, smeltItem.getMaxStackSize() / smeltItem.stackSize);
					input.stackSize -= temp;
					iInventory.setStackInSlot(1, Helper.copyStack(smeltItem, temp * smeltItem.stackSize));
				}
				if (input != null && input.stackSize <= 0)
					this.iInventory.removeStackFromSlot(0);
			}
		}
		this.markDirty();
	}

}
