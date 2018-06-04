package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.tileentity.ReplacementMachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import java.util.List;

public class ReplacementMachine extends BaseBlockContainer
{
	public static final PropertyBool WORK = PropertyBool.create("work");

	public ReplacementMachine()
	{
		this.setUnlocalizedName("replacementMachine");
		this.setRegistryName("replacement_machine");
		this.setDefaultState(this.blockState.getBaseState().withProperty(WORK, true));
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
			IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
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
					te.getInventory().removeStackFromSlot(0);
				}
			}

		}
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int a;
		a = meta % 2;
		Boolean work = Boolean.valueOf((a % 2) != 0);
		return this.getDefaultState().withProperty(WORK, work);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int work = state.getValue(WORK).booleanValue() ? 1 : 0;
		return work;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
	{
		IBlockState origin = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
		return origin.withProperty(WORK, true);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { WORK });
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		ReplacementMachineTileEntity te = (ReplacementMachineTileEntity) worldIn.getTileEntity(pos);

		IItemHandler i = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		for (int m = i.getSlots() - 1; m >= 0; --m)
		{
			if (i.getStackInSlot(m) != null)
			{
				Block.spawnAsEntity(worldIn, pos, i.getStackInSlot(m));
				((IItemHandlerModifiable) i).setStackInSlot(m, null);
			}
		}

		i = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		for (int m = i.getSlots() - 1; m >= 0; --m)
		{
			if (i.getStackInSlot(m) != null)
			{
				Block.spawnAsEntity(worldIn, pos, i.getStackInSlot(m));
				((IItemHandlerModifiable) i).setStackInSlot(m, null);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.replacementMachine", TextFormatting.BLUE));
	}

}
