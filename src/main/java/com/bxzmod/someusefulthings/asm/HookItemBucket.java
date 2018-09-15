package com.bxzmod.someusefulthings.asm;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public class HookItemBucket
{
	public static ActionResult bucketFix(ItemStack itemStackIn, EntityPlayer playerIn)
	{
		if (itemStackIn.stackSize == 1)
			return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET));
		else
		{
			itemStackIn.stackSize--;
			if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET)))
				playerIn.world.spawnEntity(new EntityItem(playerIn.world, playerIn.posX, playerIn.posY, playerIn.posZ,
						new ItemStack(Items.BUCKET)));
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
}
