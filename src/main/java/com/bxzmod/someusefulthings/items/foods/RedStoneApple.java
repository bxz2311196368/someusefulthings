package com.bxzmod.someusefulthings.items.foods;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class RedStoneApple extends Item
{

	public RedStoneApple(int amount, float saturation, boolean isWolfFood)
	{
		super();
		this.setUnlocalizedName("redStoneApple");
		this.setRegistryName("redstone_apple");
		this.setMaxDamage(0);
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		--stack.stackSize;
		if (!worldIn.isRemote)
		{
			playerIn.getFoodStats().addStats(20, 100);
			playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 4, true, true));
			playerIn.addExperience(10);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		--itemStackIn.stackSize;
		if (!worldIn.isRemote)
		{
			playerIn.getFoodStats().addStats(20, 100);
			playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 6000, 4, true, true));
			playerIn.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 4, true, true));
			playerIn.addExperience(10);
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.redStoneApple", TextFormatting.BLUE));
		tooltip.add(I18n.format("tooltip.redStoneApple_1", TextFormatting.BLUE));
		tooltip.add(I18n.format("tooltip.redStoneApple_2", TextFormatting.RED));
	}

}
