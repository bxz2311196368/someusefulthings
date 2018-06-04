package com.bxzmod.someusefulthings.blocks;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.CopyEnchantmentTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;

public class CopyEnchantment extends BlockContainer
{

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool WORK = PropertyBool.create("work");

	public CopyEnchantment()
	{
		super(Material.GRASS);
		this.setUnlocalizedName("copyEnchantment");
		this.setRegistryName("copy_enchantment");
		this.setHardness(2.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(WORK,
				Boolean.FALSE));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, WORK });
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int a;
		a = (meta >= 8) ? meta - 8 : meta;
		EnumFacing facing = EnumFacing.getHorizontal(a & 3);
		Boolean work = Boolean.valueOf((a & 4) != 0);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(WORK, work);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int facing = state.getValue(FACING).getHorizontalIndex();
		int work = state.getValue(WORK).booleanValue() ? 4 : 0;
		return facing | work;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
	{
		IBlockState origin = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
		return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			int id = GuiLoader.GUI_C_E;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new CopyEnchantmentTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		CopyEnchantmentTileEntity te = (CopyEnchantmentTileEntity) worldIn.getTileEntity(pos);

		IItemHandler i = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

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
		tooltip.add(I18n.format("tooltip.copyEnchantment", TextFormatting.BLUE));
	}
}
