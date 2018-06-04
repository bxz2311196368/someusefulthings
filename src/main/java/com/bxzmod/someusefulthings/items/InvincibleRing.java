package com.bxzmod.someusefulthings.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;

import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InvincibleRing extends Item implements IBauble
{
	// private static final Logger LOGGER = LogManager.getLogger();

	public static AttributeModifier max_health = new AttributeModifier(
			UUID.fromString("de1edbb8-d37c-4424-83f6-f7a22b23bb86"), "invinciblering", 20.0D, 0);

	public InvincibleRing()
	{
		super();
		MinecraftForge.EVENT_BUS.register(this);
		this.setUnlocalizedName("invinciblering");
		this.setRegistryName("invincible_ring");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);

	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase e)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			((EntityPlayer) e).capabilities.allowFlying = true;
			return;
		}

		EntityPlayer player = (EntityPlayer) e;
		// Helper.LOGGER.info(player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
		if (player.isBurning())
			player.extinguish();
		player.getEntityAttribute(SharedMonsterAttributes.LUCK).setBaseValue(1024.0D);
		if (itemStack.getItemDamage() == 0 && player.ticksExisted % 20 == 0)
		{
			player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1200, 0, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 3, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 5, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 3, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 0, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 1200, 0, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0, true, true));
			player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 1200, 0, true, true));
			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		}
		player.capabilities.allowFlying = true;
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		// LOGGER.info(effects.size());
		if (effects.size() > 0)
		{
			ArrayList<PotionEffect> bad = new ArrayList<PotionEffect>();
			for (Object effect : effects)
			{
				if (effect instanceof PotionEffect)
				{
					PotionEffect potion = (PotionEffect) effect;
					if (potion.getPotion().isBadEffect())
						bad.add(potion);
				}
			}
			if (bad.size() > 0)
			{
				for (PotionEffect potion : bad)
				{
					player.removePotionEffect(potion.getPotion());
				}
			}
		}
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		((EntityPlayer) player).capabilities.allowFlying = true;
		player.setAir(300);
		player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1200, 0, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 3, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 5, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 3, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 0, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 1200, 0, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0, true, true));
		player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 1200, 0, true, true));
		// player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
		// + 20.0D);
		player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).applyModifier(max_health);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		((EntityPlayer) player).capabilities.allowFlying = false;
		player.removePotionEffect(MobEffects.HASTE);
		player.removePotionEffect(MobEffects.SPEED);
		player.removePotionEffect(MobEffects.REGENERATION);
		player.removePotionEffect(MobEffects.RESISTANCE);
		player.removePotionEffect(MobEffects.FIRE_RESISTANCE);
		player.removePotionEffect(MobEffects.WATER_BREATHING);
		player.removePotionEffect(MobEffects.NIGHT_VISION);
		player.removePotionEffect(MobEffects.SATURATION);
		if (player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 20.0D)
		{
			// player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
			// - 20.0D);
		}
		player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).removeModifier(max_health);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote)
		{
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++)
				if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, stack, player))
				{
					baubles.setStackInSlot(i, stack.copy());
					if (!player.capabilities.isCreativeMode)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
					onEquipped(stack, player);
					break;
				}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.invincibleRing", TextFormatting.RED));
	}

}
