package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.tileentity.LavaPumpTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class LavaPump extends BaseBlockContainer
{

	protected LavaPump()
	{
		this.setUnlocalizedName("lavaPump");
		this.setRegistryName("lava_pump");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new LavaPumpTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote)
		{
			if (!(placer instanceof EntityPlayer) || placer instanceof FakePlayer)
				worldIn.setBlockToAir(pos);
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && te instanceof LavaPumpTileEntity)
			{
				((LavaPumpTileEntity) te).setPlayer(placer.getName());
				((LavaPumpTileEntity) te).setFirstSet(true);
				if (stack.hasTagCompound())
					((LavaPumpTileEntity) te).getTank().readFromNBT(stack.getTagCompound());
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (!worldIn.isRemote)
		{
			((LavaPumpTileEntity) te).showWork(playerIn);
		}
		return true;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.025F);
		ItemStack itemstack = this.getSilkTouchDrop(state);
		if (te != null && te instanceof LavaPumpTileEntity)
			itemstack.setTagCompound(((LavaPumpTileEntity) te).getTank().writeToNBT(new NBTTagCompound()));
		spawnAsEntity(worldIn, pos, itemstack);
	}

}
