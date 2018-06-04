package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SpawnerChanger extends Item
{

	public SpawnerChanger()
	{
		super();
		this.setUnlocalizedName("spawnerChanger");
		this.setRegistryName("spawner_changer");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getBlock() == Blocks.MOB_SPAWNER)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityMobSpawner)
			{
				MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
				if (!stack.hasTagCompound())
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("Entityid", "Pig");
					stack.setTagCompound(nbt);
				}
				mobspawnerbaselogic.setEntityName(Helper.getEntityIdFromItem(stack));
				tileentity.markDirty();
				worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

				if (!playerIn.capabilities.isCreativeMode)
				{
					--stack.stackSize;
				}

				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (!stack.hasTagCompound())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Entityid", "Pig");
			stack.setTagCompound(nbt);
		}
		if (target instanceof EntityPlayer)
			return true;
		stack.getTagCompound().setString("Entityid", EntityList.getEntityString(target));
		if (attacker instanceof EntityPlayer && !(attacker instanceof FakePlayer))
			((EntityPlayer) attacker).sendStatusMessage(EntityList
					.createEntityByName(stack.getTagCompound().getString("Entityid"), attacker.getEntityWorld())
					.getDisplayName());
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		if (stack.hasTagCompound())
			tooltip.add(EntityList
					.createEntityByName(stack.getTagCompound().getString("Entityid"), Minecraft.getMinecraft().world)
					.getDisplayName().getFormattedText());

	}

}
