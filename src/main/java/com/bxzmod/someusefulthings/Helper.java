package com.bxzmod.someusefulthings;

import cofh.api.energy.IEnergyContainerItem;
import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.network.KeyPressReception;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import ic2.api.item.ElectricItem;
import mekanism.common.capabilities.Capabilities;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Helper
{

	public static final Logger LOGGER = LogManager.getLogger();

	public Helper()
	{

	}

	public static ArrayList<ItemStack> helpGetItemList(ItemStack[] itemStacks)
	{
		ArrayList<ItemStack> item = new ArrayList<ItemStack>();
		for (ItemStack i : itemStacks)
		{
			if (i != null)
				item.add(i);
		}
		return item;
	}

	public static boolean hasItemStack(EntityPlayer player, Item item)
	{
		for (int i = 0; i < player.inventory.mainInventory.length; i++)
			if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == item)
				return true;
		return false;
	}

	public static ItemStack getItemStackInPlayer(EntityPlayer player, Item item)
	{
		for (int i = 0; i < player.inventory.mainInventory.length; i++)
			if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == item)
				return player.inventory.getStackInSlot(i);
		return null;
	}

	public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range)
	{
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (player instanceof EntityPlayer)
			d1 += ((EntityPlayer) player).eyeHeight;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		Vec3d vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.rayTraceBlocks(vec3, vec31, par3, true, false);
	}

	public static void BreakOtherBlock(ItemStack itemstack, BlockPos pos, EntityPlayer player, int range, int depth,
			int side)
	{
		BlockPos nextpos = pos;
		BlockPos playerPos = player.getPosition();
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
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
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
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
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
						;
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
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
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
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
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
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
						SimulatePlayerBreakBlock(itemstack, nextpos, playerPos, player);
					}
			break;
		default:
			return;
		}
	}

	public static void SimulatePlayerBreakBlock(ItemStack stack, BlockPos pos, BlockPos playerPos, EntityPlayer player)
	{
		World world = player.world;
		if (!world.isBlockLoaded(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block blk = state.getBlock();

		if (!world.isRemote && blk != null && !blk.isAir(state, world, pos)
				&& state.getPlayerRelativeBlockHardness(player, world, pos) > 0)
		{
			if (!blk.canHarvestBlock(player.world, pos, player))
				return;

			int exp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(),
					(EntityPlayerMP) player, pos);
			if (exp == -1)
				return;

			if (!player.capabilities.isCreativeMode)
			{
				TileEntity tile = world.getTileEntity(pos);
				IBlockState localState = world.getBlockState(pos);
				blk.onBlockHarvested(world, pos, localState, player);

				if (blk.removedByPlayer(state, world, pos, player, true))
				{
					blk.onBlockDestroyedByPlayer(world, pos, state);
					if (tile != null && tile instanceof TileEntityMobSpawner)
						harvestMobSpawner(blk, world, player, pos, tile, state);
					else
						harvestBlock(blk, world, player, pos, playerPos, state, tile, stack);
				}
			} else
				world.setBlockToAir(pos);

			if (exp > 0)
				blk.dropXpOnBlockBreak(world, playerPos, exp);
		}
	}

	public static void harvestMobSpawner(Block blk, World worldIn, EntityPlayer player, BlockPos pos, TileEntity te,
			IBlockState state)
	{
		if (te != null && !(te instanceof TileEntityMobSpawner))
			return;
		NBTTagCompound nbt = new NBTTagCompound();
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) te;
		Item i = Item.getItemFromBlock(blk);
		ItemStack s = new ItemStack(i);
		ItemStack s1 = new ItemStack(ItemLoader.spawnerChanger);
		MobSpawnerBaseLogic mobspawnerbaselogic = spawner.getSpawnerBaseLogic();
		nbt.setString("Entityid",
				((NBTTagCompound) mobspawnerbaselogic.writeToNBT(new NBTTagCompound()).getTag("SpawnData"))
						.getString("id"));
		s1.setTagCompound(nbt);
		blk.onBlockDestroyedByPlayer(worldIn, pos, state);
		blk.spawnAsEntity(worldIn, player.getPosition(), s);
		EntityItem entityitem = new EntityItem(worldIn, player.getPosition().getX(), player.getPosition().getY(),
				player.getPosition().getZ(), s1);
		entityitem.setNoPickupDelay();
		worldIn.spawnEntity(entityitem);

	}

	public static void harvestBlock(Block blk, World worldIn, EntityPlayer player, BlockPos pos, BlockPos playerPos,
			IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(blk));
		player.addExhaustion(0.025F);

		if (blk.canSilkHarvest(worldIn, pos, state, player)
				&& EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
		{
			java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
			ItemStack itemstack = getSilkTouchDrop(state, blk);

			if (itemstack != null)
			{
				items.add(itemstack);
			}

			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true,
					player);
			for (ItemStack item : items)
			{
				blk.spawnAsEntity(worldIn, playerPos, item);
			}
		} else
		{
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			dropBlockAsItemWithChance(player, blk, worldIn, pos, playerPos, state, 1.0F, i);
		}
	}

	public static void dropBlockAsItemWithChance(EntityPlayer player, Block blk, World worldIn, BlockPos pos,
			BlockPos playerPos, IBlockState state, float chance, int fortune)
	{
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
		{
			java.util.List<ItemStack> items = blk.getDrops(worldIn, pos, state, fortune);
			chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune,
					chance, false, player);

			for (ItemStack item : items)
			{
				if (worldIn.rand.nextFloat() <= chance)
				{
					blk.spawnAsEntity(worldIn, playerPos, item);
				}
			}
		}
	}

	@Nullable
	public static ItemStack getSilkTouchDrop(IBlockState state, Block blk)
	{
		Item item = Item.getItemFromBlock(blk);

		if (item == null)
		{
			return null;
		} else
		{
			int i = 0;

			if (item.getHasSubtypes())
			{
				i = blk.getMetaFromState(state);
			}

			if (blk instanceof BlockRotatedPillar || blk instanceof BlockLeaves)
				i &= 3;

			return new ItemStack(item, 1, i);
		}
	}

	@Nullable
	public static String getEntityIdFromItem(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null)
		{
			return null;
		}
		return nbttagcompound.hasKey("Entityid", 8) ? nbttagcompound.getString("Entityid") : null;
	}

	public static BlockPos getBlockPosFromstring(String s)
	{
		return new BlockPos(Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(s, "x="), ",")),
				Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(s, "y="), ",")),
				Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(s, "z="), "}")));
	}

	public static void tpNPC(Entity npc, EntityPlayer player)
	{
		npc.setLocationAndAngles(player.posX, player.posY, player.posZ, npc.rotationYaw, npc.rotationPitch);
	}

	public static ItemStack copyStack(ItemStack stack)
	{
		if (stack == null)
			return null;
		ItemStack copyStack = new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage());
		if (stack.hasTagCompound())
			copyStack.setTagCompound(stack.getTagCompound());
		return copyStack;
	}

	public static ItemStack copyStack(ItemStack stack, int amount)
	{
		if (stack == null)
			return null;
		ItemStack copyStack = new ItemStack(stack.getItem(), amount, stack.getItemDamage());
		if (stack.hasTagCompound())
			copyStack.setTagCompound(stack.getTagCompound());
		return copyStack;
	}

	public static ItemStack mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection,
			IItemHandler handler)
	{
		return mergeItemStack(stack, startIndex, endIndex, reverseDirection, handler, false);
	}

	public static ItemStack mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection,
			IItemHandler handler, boolean simulate)
	{
		if (stack == null || stack.stackSize <= 0)
			return null;
		boolean flag = false;
		int i = startIndex, size = stack.stackSize;

		if (reverseDirection)
		{
			i = endIndex - 1;
		}

		while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
		{
			ItemStack temp = handler.insertItem(i, copyStack(stack), simulate);
			if (temp != null)
			{
				if (stack.stackSize != temp.stackSize)
				{
					if (!simulate)
						stack.stackSize = temp.stackSize;
					else
						size = temp.stackSize;
					flag = true;
				}
			} else
			{
				if (!simulate)
					stack.stackSize = 0;
				else
					size = 0;
				flag = true;
			}

			if (reverseDirection)
			{
				--i;
			} else
			{
				++i;
			}
		}
		return simulate ? (size > 0 ? copyStack(stack, size) : null) : (stack.stackSize > 0 ? stack : null);
	}

	public static boolean mergeInventory(IItemHandler invSend, IItemHandler invReceive, int receiveSlot)
	{
		for (int i = 0; i < invSend.getSlots(); i++)
		{
			ItemStack stack = invSend.extractItem(i, 64, true), insert;
			if (stack != null)
			{
				insert = invReceive.insertItem(receiveSlot, stack, false);
				int inserted = insert == null ? 64 : stack.stackSize - insert.stackSize;
				if (inserted != 0)
					invSend.extractItem(i, inserted, false);
			}
		}
		return false;
	}

	public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
	{
		return stackB.getItem() == stackA.getItem()
				&& (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata())
				&& ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static void sendKey(EntityPlayer player, int key)
	{
		KeyPressReception message = new KeyPressReception();
		message.nbt = new NBTTagCompound();
		message.nbt.setString("name", player.getName());
		message.nbt.setInteger("key", key);
		NetworkLoader.instance.sendToServer(message);
	}

	public static ItemStack[] loadStacksFromNBT(int size, NBTTagList list)
	{
		ItemStack[] stacks = new ItemStack[size];
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound itemTags = list.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < stacks.length)
			{
				stacks[slot] = ItemStack.loadItemStackFromNBT(itemTags);
			}
		}
		return stacks;
	}

	public static NBTTagList writeStacksToNBT(ItemStack[] stacks)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < stacks.length; i++)
		{
			if (stacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				stacks[i].writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}
		return list;
	}

	public static ItemStack creatItemStackWithNBT(Object o, int amount, int meta, NBTTagCompound nbt)
	{
		ItemStack stack;
		if (o instanceof Item)
			stack = new ItemStack((Item) o, amount, meta);
		else if (o instanceof Block)
			stack = new ItemStack((Block) o, amount, meta);
		else if (o instanceof ItemStack)
			stack = new ItemStack(((ItemStack) o).getItem(), amount, meta);
		else
			return null;
		stack.setTagCompound(nbt);
		return stack;
	}

	public static boolean isSameStackIgnoreAmount(ItemStack stack1, ItemStack stack2)
	{
		if (stack1 == null || stack2 == null)
			return false;
		if (stack1.isItemEqual(stack2))
			return true;
		if (stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata())
			if (stack1.hasTagCompound() && stack2.hasTagCompound())
			{
				if (stack1.getTagCompound().equals(stack2.getTagCompound()))
					return true;
			} else if (!stack1.hasTagCompound() && !stack2.hasTagCompound())
				return true;
		return false;
	}

	public static ChunkPos getChunkPosFromString(String s)
	{
		return new ChunkPos(Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(s, "["), ",")),
				Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(s, ", "), "]")));
	}

	public static void teleportEntity(Entity entity, BlockPos pos, int targetDim)
	{
		EntityPlayerMP player = null;

		if (entity instanceof EntityPlayerMP)
		{
			player = (EntityPlayerMP) entity;
		}

		if (player == null)
			return;
		int from = entity.dimension;
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		WorldServer toDim = server.worldServerForDimension(targetDim);
		toDim.getChunkProvider().provideChunk(pos.getX() >> 4, pos.getZ() >> 4);
		if (from != targetDim)
		{
			WorldServer fromDim = server.worldServerForDimension(from);
			Teleporter teleporter = new Teleporter(toDim)
			{
				@Override
				public void placeInPortal(Entity entityIn, float rotationYaw)
				{
					int x = MathHelper.floor(entity.posX);
					int y = MathHelper.floor(entity.posY) - 1;
					int z = MathHelper.floor(entity.posZ);
					entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
					entity.motionX = 0D;
					entity.motionY = 0D;
					entity.motionZ = 0D;
					entity.fallDistance = 0F;
				}

				@Override
				public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
				{
					return true;
				}

				@Override
				public boolean makePortal(Entity entityIn)
				{
					return true;
				}

				@Override
				public void removeStalePortalLocations(long worldTime)
				{

				}

			};

			server.getPlayerList().transferPlayerToDimension(player, targetDim, teleporter);
			if (from == 1 && entity.isEntityAlive())
			{
				toDim.spawnEntity(entity);
				toDim.updateEntityWithOptionalForce(entity, false);
			}
		}

		player.connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, player.rotationYaw,
				player.rotationPitch);
		player.addExperienceLevel(0);
		entity.fallDistance = 0;

	}

	public static int getXpBarCap(int level)
	{
		return level >= 30 ? 112 + (level - 30) * 9 : (level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2);
	}

	public static int getXpTotalLevel(int xp)
	{
		int level = 0;
		for (; xp >= 0; level++)
			xp -= getXpBarCap(level);
		return level - 1;
	}

	public static int getTotalXp(int level)
	{
		if (level >= 21863)
		{
			return Integer.MAX_VALUE;
		}
		int xp = 0;
		for (; level > 0; level--)
			xp += getXpBarCap(level - 1);

		return xp;
	}

	public static int getXpTotalBetween(int start, int end)
	{
		int xp = 0, level = end;
		for (; level > start; level--)
			xp += getXpBarCap(level - 1);
		return xp;
	}

	public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, EnumFacing dir)
	{
		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}

	public static boolean isEnergyStack(ItemStack stack)
	{
		if (ModLoadFlag.isTeslaLoad)
			if (stack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null))
				return true;
		if (ModLoadFlag.isIC2Load)
			if (ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, 14, true, true) > 0.0)
				return true;
		if (ModLoadFlag.isMekLoad)
			if (stack.hasCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, null))
				return true;
		if (stack.getItem() instanceof IEnergyContainerItem)
			return true;
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
			return true;
		return false;
	}

	public static boolean isFullEnergyStack(ItemStack stack)
	{
		if (ModLoadFlag.isTeslaLoad)
			if (stack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null))
				if (stack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null).givePower(1, true) == 0)
					return true;
		if (ModLoadFlag.isIC2Load)
			if (ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, 14, true, true) == 0.0
					&& ElectricItem.manager.getCharge(stack) > 0.0)
				return true;
		if (ModLoadFlag.isMekLoad)
			if (stack.hasCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, null))
				if (stack.getCapability(Capabilities.ENERGY_ACCEPTOR_CAPABILITY, null).acceptEnergy(null, 1D,
						true) == 0.0D)
					return true;
		if (stack.getItem() instanceof IEnergyContainerItem)
			if (((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack, 1, true) == 0)
				return true;
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
			if (stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(1, true) == 0)
				return true;
		return false;
	}

}
