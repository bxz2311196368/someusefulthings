package com.bxzmod.someusefulthings.items.tools;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Set;

public class MultiplePickaxes extends ItemTool
{
	protected int type = 0;

	public MultiplePickaxes(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn, int type)
	{
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		this.type = type;
		this.setUnlocalizedName("multiplePickaxes" + this.type);
		this.setRegistryName("multiple_pickaxes" + this.type);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.damageVsEntity = materialIn.getDamageVsEntity();
		this.attackSpeed = attackSpeedIn;
		if (materialIn == ToolMaterial.GOLD)
		{
			this.setHarvestLevel("axe", 3);
			this.setHarvestLevel("shovel", 3);
			this.setHarvestLevel("pickaxe", 3);
			this.setHarvestLevel("hoe", 3);
			this.setHarvestLevel("sword", 3);
		} else
		{
			this.setHarvestLevel("axe", materialIn.getHarvestLevel());
			this.setHarvestLevel("shovel", materialIn.getHarvestLevel());
			this.setHarvestLevel("pickaxe", materialIn.getHarvestLevel());
			this.setHarvestLevel("hoe", materialIn.getHarvestLevel());
			this.setHarvestLevel("sword", materialIn.getHarvestLevel());
		}

	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return (type + 2) * 2;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		int range = this.type + 2;
		if (player.world.isRemote)
		{
			return false;
		}
		if (player instanceof FakePlayer || player.getName().contains("[") || player.getName().contains("]"))
		{
			return false;
		}
		RayTraceResult result = Helper.raytraceFromEntity(player.world, player, true, 10);
		if (result == null || result.sideHit == null)
			return true;
		int side = result.sideHit.getOpposite().getIndex();
		Helper.BreakOtherBlock(itemstack, pos, player, range, range * 2 - 1, side);
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return true;
	}
}
