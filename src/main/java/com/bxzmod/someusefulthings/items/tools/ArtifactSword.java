package com.bxzmod.someusefulthings.items.tools;

import com.bxzmod.someusefulthings.achievement.AchievementLoader;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.damagesource.Infinity;
import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.throwable.InfinityArrow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ArtifactSword extends ItemSword
{
	private final float attackDamage;

	public ArtifactSword(Item.ToolMaterial material)
	{
		super(material);
		this.setUnlocalizedName("artifactsword");
		this.setRegistryName("artifact_sword");
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
		this.maxStackSize = 1;
		this.setMaxDamage(ItemLoader.LIMITLESS.getMaxUses());
		this.attackDamage = 3.0F * ItemLoader.LIMITLESS.getDamageVsEntity();

	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if (player.world.isRemote)
			return true;
		player.heal(100);
		if (!target.isDead && target.getHealth() > 0)
		{
			target.getCombatTracker().trackDamage(new Infinity(player), target.getHealth(), target.getHealth());
			target.setHealth(0);
			target.onDeath(new EntityDamageSource("infinity", player));
			return true;
		}

		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (!entity.world.isRemote && entity instanceof EntityPlayer)
		{
			EntityPlayer target = (EntityPlayer) entity;
			if (target.capabilities.isCreativeMode && !target.isDead && target.getHealth() > 0)
			{
				target.getCombatTracker().trackDamage(new Infinity(player), target.getHealth(), target.getHealth());
				target.setHealth(0);
				target.onDeath(new EntityDamageSource("infinity", player));
				player.addStat(AchievementLoader.kill_creative, 1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving)
	{
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			// stack.damageItem(2, entityLiving);
			stack.setItemDamage(0);
		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.artifactSword", TextFormatting.GOLD));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ,
				SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote)
		{
			InfinityArrow e = new InfinityArrow(worldIn, playerIn);
			e.setAim(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 4.5F, 0.0F);
			worldIn.spawnEntity(e);
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
