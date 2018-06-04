package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.tileentity.EyeGeneratorTileEntity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EyeGenerator extends BaseBlockContainer
{
	public static final PropertyBool WORK = PropertyBool.create("work");

	public EyeGenerator()
	{
		this.setUnlocalizedName("eyeGenerator");
		this.setRegistryName("eye_generator");
		this.setDefaultState(this.blockState.getBaseState().withProperty(WORK, false));
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
		return origin.withProperty(WORK, false);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new EyeGeneratorTileEntity();

	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { WORK });
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.eyeGenerator", TextFormatting.GREEN));
	}

}
