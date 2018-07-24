package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.IConfigSide;
import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.EnergyBlockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EnergyBlock extends BaseIOBlockContainer
{

	public EnergyBlock()
	{
		super("energy_block", "energyBlock");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new EnergyBlockTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
		EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking())
		{
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof IConfigSide)
			{
				IConfigSide config = (IConfigSide) te;
				config.cycleSideIO(side);
				worldIn.notifyBlockUpdate(pos, state, this.getExtendedState(state, worldIn, pos), 2);
			}
		} else if (!worldIn.isRemote)
		{
			playerIn.openGui(Main.instance, GuiLoader.GUI_E_B, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}
