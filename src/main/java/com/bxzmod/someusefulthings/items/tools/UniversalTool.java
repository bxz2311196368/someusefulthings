package com.bxzmod.someusefulthings.items.tools;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.items.ItemLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class UniversalTool extends ItemTool
{
	private final Set<Block> effectiveBlocks;
	protected float efficiencyOnProperMaterial;
	EnumFacing facing = EnumFacing.NORTH;

	public UniversalTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn)
	{
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		this.setUnlocalizedName("limitlessTool");
		this.setRegistryName("limitless_tool");
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
		this.toolMaterial = materialIn;
		this.effectiveBlocks = effectiveBlocksIn;
		this.maxStackSize = 1;
		this.setMaxDamage(materialIn.getMaxUses());
		this.efficiencyOnProperMaterial = materialIn.getEfficiencyOnProperMaterial();
		this.damageVsEntity = attackDamageIn * materialIn.getDamageVsEntity();
		this.attackSpeed = attackSpeedIn;
		this.setHarvestLevel("axe", 99);
		this.setHarvestLevel("shovel", 99);
		this.setHarvestLevel("pickaxe", 99);
		this.setHarvestLevel("hoe", 99);
		this.setHarvestLevel("sword", 99);

	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		int side = 0;
		// Get a Facing by it's index (0-5). The order is D-U-N-S-W-E.
		// E=x+, U=y+, S=z+
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
		facing = result.sideHit.getOpposite();
		side = facing.getIndex();
		if (itemstack.getTagCompound().hasKey("dig_parameter"))
		{
			int a = ((NBTTagCompound) itemstack.getTagCompound().getTag("dig_parameter")).getInteger("dig_range");
			int b = ((NBTTagCompound) itemstack.getTagCompound().getTag("dig_parameter")).getInteger("dig_depth");
			if ((a <= 0 || a > 5) || (b <= 0 || b > 9))
			{
				itemstack.getTagCompound().removeTag("dig_parameter");
				itemstack.getTagCompound().setTag("dig_parameter", dig);
				a = 1;
				b = 1;
			}
			if ((a != 1 || b != 1) && this.isNotSafe(pos, player, a, b, side))
			{
				player.sendMessage(new TextComponentTranslation("limitlessTool.safePrompt"));
				return true;
			}
			switch (a)
			{
			case 2:
			case 3:
			case 4:
			case 5:
				Helper.BreakOtherBlock(itemstack, pos, player, a, b, side);
				break;
			case 1:
				if (b == 1)
				{
					this.HelpBlockStartBreak(itemstack, pos, player);
				} else
					Helper.BreakOtherBlock(itemstack, pos, player, a, b, side);
				break;
			default:
				this.HelpBlockStartBreak(itemstack, pos, player);
			}
		} else
		{
			itemstack.getTagCompound().setTag("dig_parameter", dig);
			this.HelpBlockStartBreak(itemstack, pos, player);
		}

		return false;
	}

	@SuppressWarnings("incomplete-switch")
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
		{
			return EnumActionResult.FAIL;
		} else
		{
			int hook = ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos);
			if (hook != 0)
				return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
			{
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
				{
					this.setBlock(stack, playerIn, worldIn, pos,
							Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, Integer.valueOf(7)));
					return EnumActionResult.SUCCESS;
				}

				if (block == Blocks.DIRT)
				{
					switch ((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT))
					{
					case DIRT:
						this.setBlock(stack, playerIn, worldIn, pos, Blocks.FARMLAND.getDefaultState()
								.withProperty(BlockFarmland.MOISTURE, Integer.valueOf(7)));
						return EnumActionResult.SUCCESS;
					case COARSE_DIRT:
						this.setBlock(stack, playerIn, worldIn, pos,
								Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
						return EnumActionResult.SUCCESS;
					}
				}
			}

			return EnumActionResult.PASS;
		}
	}

	protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		if (!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, state, 11);
			this.setDamage(stack, 0);
		}
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity,
			EnumHand hand)
	{
		if (entity.world.isRemote)
		{
			return false;
		}
		if (entity instanceof IShearable)
		{
			IShearable target = (IShearable) entity;
			BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
			if (target.isShearable(itemstack, entity.world, pos))
			{
				java.util.List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos,
						EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

				Random rand = new Random();
				for (ItemStack stack : drops)
				{
					EntityItem ent = entity.entityDropItem(stack, 1.0F);
					ent.motionY += rand.nextFloat() * 0.05F;
					ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				}
				this.setDamage(itemstack, 0);
			}
			return true;
		}
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Block block = state.getBlock();
		if (block != null)
		{
			float blockHardness = block.blockHardness;
			return blockHardness == 0.0F ? 20.0F : blockHardness * 10.0F;
		}
		return 30.0F;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		attacker.heal(100);
		this.setDamage(itemStack, 0);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving)
	{
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
			this.setDamage(stack, 0);
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("tooltip.limitlessTool", TextFormatting.YELLOW));
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dig_parameter"))
		{
			NBTTagCompound dig = stack.getTagCompound().getCompoundTag("dig_parameter");
			int r = dig.getInteger("dig_range"), d = dig.getInteger("dig_depth");
			if (r == 1 && d == 1)
				tooltip.add(I18n.format("limitlessTool.toolmsg_0", TextFormatting.YELLOW));
			else
				tooltip.add(I18n.format("limitlessTool.toolmsg_1", TextFormatting.BLUE, r, d, r));
		}
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		ItemStack limitlesstoolwithnbt = new ItemStack(itemIn);
		ItemStack limitlesstoolwithnbt1 = new ItemStack(itemIn);
		limitlesstoolwithnbt.addEnchantment(Enchantment.getEnchantmentByID(21), 10);
		limitlesstoolwithnbt.addEnchantment(Enchantment.getEnchantmentByID(35), 10);
		limitlesstoolwithnbt.getTagCompound().setTag("dig_parameter", dig);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.getEnchantmentByID(21), 10);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
		limitlesstoolwithnbt1.getTagCompound().setTag("dig_parameter", dig);
		subItems.add(limitlesstoolwithnbt);
		subItems.add(limitlesstoolwithnbt1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if (itemStackIn.getItem() == ItemLoader.limitlesstool && playerIn.isSneaking())
		{
			BlockPos pos = playerIn.getPosition();
			int id = GuiLoader.GUI_T_S;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	public boolean HelpBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode)
			return false;

		BlockPos playerPos = player.getPosition();
		World world = player.world;
		IBlockState state = world.getBlockState(pos);
		Block block = world.getBlockState(pos).getBlock();
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMobSpawner)
			Helper.harvestMobSpawner(block, world, player, pos, te, state);
		if (block instanceof IShearable)
		{
			if (block.canSilkHarvest(world, pos, world.getBlockState(pos), player)
					&& EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0)
				return false;
			IShearable target = (IShearable) block;
			if (target.isShearable(itemstack, world, pos))
			{
				List<ItemStack> drops = target.onSheared(itemstack, world, pos,
						EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
				Random rand = new Random();

				for (ItemStack stack : drops)
				{
					EntityItem entityitem = new EntityItem(world, (double) playerPos.getX(), (double) playerPos.getY(),
							(double) playerPos.getZ(), stack);
					entityitem.setDefaultPickupDelay();
					world.spawnEntity(entityitem);
				}
				this.setDamage(itemstack, 0);
				player.addStat(StatList.getBlockStats(block));
			}
			return true;
		}
		return false;
	}

	public boolean isNotSafe(BlockPos pos, EntityPlayer player, int range, int depth, int side)
	{
		BlockPos nextpos = pos;
		int y_range = range == 1 ? 1 : range * 2 - 2, digrange = depth;
		switch (side)
		{
		case 0:
			for (int x = -range + 1; x < range; x++)
				for (int y = 0; y < digrange; y++)
					for (int z = -range + 1; z < range; z++)
					{
						nextpos = pos.add(x, -y, z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		case 1:
			for (int x = -range + 1; x < range; x++)
				for (int y = 0; y < digrange; y++)
					for (int z = -range + 1; z < range; z++)
					{
						nextpos = pos.add(x, y, z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		case 2:
			for (int x = -range + 1; x < range; x++)
				for (int y = range == 1 ? 0 : -1; y < y_range; y++)
					for (int z = 0; z < digrange; z++)
					{
						nextpos = pos.add(x, y, -z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		case 3:
			for (int x = -range + 1; x < range; x++)
				for (int y = range == 1 ? 0 : -1; y < y_range; y++)
					for (int z = 0; z < digrange; z++)
					{
						nextpos = pos.add(x, y, z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		case 4:
			for (int x = 0; x < digrange; x++)
				for (int y = range == 1 ? 0 : -1; y < y_range; y++)
					for (int z = -range + 1; z < range; z++)
					{
						nextpos = pos.add(-x, y, z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		case 5:
			for (int x = 0; x < digrange; x++)
				for (int y = range == 1 ? 0 : -1; y < y_range; y++)
					for (int z = -range + 1; z < range; z++)
					{
						nextpos = pos.add(x, y, z);
						if (x == 0 && y == 0 && z == 0)
							continue;
						if (!player.world.isAirBlock(nextpos) && player.world.getTileEntity(nextpos) != null)
							return true;
					}
			break;
		default:
			return false;
		}
		return false;
	}

}
