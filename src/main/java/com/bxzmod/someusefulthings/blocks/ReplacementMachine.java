package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.tileentity.ReplacementMachineTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ReplacementMachine extends BaseIOBlockContainer
{

	public ReplacementMachine()
	{
		super("replacement_machine", "replacementMachine");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new ReplacementMachineTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		if (!worldIn.isRemote)
		{
			ReplacementMachineTileEntity te = (ReplacementMachineTileEntity) worldIn.getTileEntity(pos);
			ItemStackHandlerModify inv = te.getiInventory();
			if (playerIn.isSneaking())
			{
				if (heldItem == null)
				{
					te.clearData();
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_12", TextFormatting.RED));
				}

			} else
			{
				if (heldItem != null && heldItem.stackSize > 0)
				{
					if (heldItem.getItem() instanceof ItemBlock && !heldItem.equals(inv.insertItem(0, heldItem, true)))
					{
						playerIn.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(0, heldItem, false));
					}
				} else if (inv.getStackInSlot(0) != null && inv.getStackInSlot(0).stackSize > 0)
				{
					playerIn.setHeldItem(EnumHand.MAIN_HAND, inv.getStackInSlot(0));
					inv.removeStackFromSlot(0);
				}
			}

		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.replacementMachine", TextFormatting.BLUE));
	}

}
