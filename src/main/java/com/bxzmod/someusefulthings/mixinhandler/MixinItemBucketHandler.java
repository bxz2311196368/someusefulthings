package com.bxzmod.someusefulthings.mixinhandler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBucket.class)
public abstract class MixinItemBucketHandler extends Item
{
	@Inject(method = "onItemRightClick", at = @At(value = "RETURN", ordinal = 9), cancellable = true)
	protected void onItemBucketFullRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> ci)
	{
		if (stack.stackSize == 1)
			ci.setReturnValue(new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET)));
		else
		{
			stack.stackSize--;
			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET)))
				player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ,
						new ItemStack(Items.BUCKET)));
			ci.setReturnValue(new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack));
		}
	}
}
