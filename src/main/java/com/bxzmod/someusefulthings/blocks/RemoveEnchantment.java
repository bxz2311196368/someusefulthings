package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.RemoveEnchantmentTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RemoveEnchantment extends BaseIOBlockContainer
{

	public RemoveEnchantment()
	{
		super("remove_enchantment", "removeEnchantment");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			int id = GuiLoader.GUI_R_E;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new RemoveEnchantmentTileEntity();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.removeEnchantment", TextFormatting.BLUE));
	}
}
