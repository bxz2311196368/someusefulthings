package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.tileentity.CobbleStoneMakerTiletEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class CobbleStoneMaker extends BaseBlockContainer
{
	public CobbleStoneMaker()
	{
		super("cobble_stone_maker", "cobbleStoneMaker");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new CobbleStoneMakerTiletEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
			if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof CobbleStoneMakerTiletEntity)
				if (worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
						.getStackInSlot(0) != null)
					if (playerIn.isSneaking())
						((CobbleStoneMakerTiletEntity) worldIn.getTileEntity(pos)).change();
					else if (!playerIn.inventory.addItemStackToInventory(worldIn.getTileEntity(pos)
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0)))
						worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
								worldIn.getTileEntity(pos)
										.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
										.getStackInSlot(0)));
		return true;
	}

}
