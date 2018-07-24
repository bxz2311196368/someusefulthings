package com.bxzmod.someusefulthings.events;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.TicketManager;
import com.bxzmod.someusefulthings.achievement.AchievementLoader;
import com.bxzmod.someusefulthings.capability.*;
import com.bxzmod.someusefulthings.fluid.FluidLoaderHelper;
import com.bxzmod.someusefulthings.items.InvincibleRing;
import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.keybinding.KeyLoader;
import com.bxzmod.someusefulthings.network.DataInteraction;
import com.bxzmod.someusefulthings.network.GarbageBagSetting;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.network.TPLocationSync;
import com.bxzmod.someusefulthings.tileentity.ChunkLoaderTileEntity;
import com.bxzmod.someusefulthings.tileentity.LavaPumpTileEntity;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EventLoader
{
	Item ring = ItemLoader.invinciblering;

	public EventLoader(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onGetHurt(LivingHurtEvent event)
	{

		if (!(event.getEntity() instanceof EntityPlayer))
			return;
		else
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() == ring)
					|| (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() == ring))
				event.setCanceled(true);
		}

	}

	@SubscribeEvent
	public void onAttacked(LivingAttackEvent event)
	{
		if (!(event.getEntity() instanceof EntityPlayer))
			return;
		else
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() == ring)
					|| (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() == ring))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;
		else
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() == ring)
					|| (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() == ring))
			{
				event.setCanceled(true);
				player.setHealth(player.getMaxHealth());
			}
		}
	}

	@SubscribeEvent
	public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent.Entity event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			ICapabilitySerializable<NBTTagCompound> provider_backpack = new PortableInventory.ProviderPlayer();
			ICapabilitySerializable<NBTTagCompound> provider_garbag = new GarbagBagCP.ProviderPlayer();
			ICapabilitySerializable<NBTTagCompound> provider_tpList = new TPLocation.ProviderPlayer();
			event.addCapability(new ResourceLocation(ModInfo.MODID + ":" + "portable_inventory"), provider_backpack);
			event.addCapability(new ResourceLocation(ModInfo.MODID + ":" + "garbag_bag"), provider_garbag);
			event.addCapability(new ResourceLocation(ModInfo.MODID + ":" + "tp_pos"), provider_tpList);
		}
	}

	@SubscribeEvent
	public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
	{
		EntityPlayer player = event.getEntityPlayer();
		player.getCapability(CapabilityLoader.PORTABLE_INVENTORY, null).deserializeNBT(
				event.getOriginal().getCapability(CapabilityLoader.PORTABLE_INVENTORY, null).serializeNBT());
		player.getCapability(CapabilityLoader.GARBAGBAG, null)
				.deserializeNBT(event.getOriginal().getCapability(CapabilityLoader.GARBAGBAG, null).serializeNBT());
		player.getCapability(CapabilityLoader.TP_MENU, null)
				.deserializeNBT(event.getOriginal().getCapability(CapabilityLoader.TP_MENU, null).serializeNBT());
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			if (player.hasCapability(CapabilityLoader.PORTABLE_INVENTORY, null))
			{
				DataInteraction message_backpack = new DataInteraction();
				IPortableInventory cp_backpack = player.getCapability(CapabilityLoader.PORTABLE_INVENTORY, null);
				IStorage<IPortableInventory> storage_backpack = CapabilityLoader.PORTABLE_INVENTORY.getStorage();
				message_backpack.nbt = new NBTTagCompound();
				message_backpack.nbt = (NBTTagCompound) storage_backpack.writeNBT(CapabilityLoader.PORTABLE_INVENTORY,
						cp_backpack, null);
				NetworkLoader.instance.sendTo(message_backpack, player);
			}
			if (player.hasCapability(CapabilityLoader.GARBAGBAG, null))
			{
				GarbageBagSetting message_garbag = new GarbageBagSetting();
				IGarbagBag cp_garbag = player.getCapability(CapabilityLoader.GARBAGBAG, null);
				IStorage<IGarbagBag> storage_garbag = CapabilityLoader.GARBAGBAG.getStorage();
				message_garbag.nbt = new NBTTagCompound();
				message_garbag.nbt = (NBTTagCompound) storage_garbag.writeNBT(CapabilityLoader.GARBAGBAG, cp_garbag,
						null);
				NetworkLoader.instance.sendTo(message_garbag, player);
			}
			if (player.hasCapability(CapabilityLoader.TP_MENU, null))
			{
				TPLocationSync message_tp = new TPLocationSync();
				ITPLocation cp_tp = player.getCapability(CapabilityLoader.TP_MENU, null);
				IStorage<ITPLocation> storage_tp = CapabilityLoader.TP_MENU.getStorage();
				message_tp.nbt = new NBTTagCompound();
				message_tp.nbt = (NBTTagCompound) storage_tp.writeNBT(CapabilityLoader.TP_MENU, cp_tp, null);
				NetworkLoader.instance.sendTo(message_tp, player);
			}
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() == ring)
					|| (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() == ring))
			{
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)
						.removeModifier(InvincibleRing.max_health);
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)
						.applyModifier(InvincibleRing.max_health);
				player.setHealth(player.getHealth() * 0.99F);
				player.capabilities.allowFlying = true;
			}
		}
	}

	@SubscribeEvent
	public void onItemPickUp(EntityItemPickupEvent evt)
	{
		ArrayList<ItemStack> item = new ArrayList<ItemStack>();
		final EntityPlayer player = evt.getEntityPlayer();
		final ItemStack pickedStack = evt.getItem().getEntityItem();
		if (player == null || pickedStack == null || !player.hasCapability(CapabilityLoader.GARBAGBAG, null)
				|| !player.getCapability(CapabilityLoader.GARBAGBAG, null).isEnable())
			return;
		item = (ArrayList<ItemStack>) Helper
				.helpGetItemList(player.getCapability(CapabilityLoader.GARBAGBAG, null).getStacks()).clone();
		if (item.isEmpty())
			return;
		for (ItemStack i : item)
		{
			if (i.getItem() == pickedStack.getItem())
			{
				evt.setCanceled(true);
				evt.getItem().setDead();
				break;
			}
		}
	}

	@SubscribeEvent
	public void onCreateFluidSource(CreateFluidSourceEvent event)
	{
		Block block = event.getState().getBlock();
		if (block == FluidLoaderHelper.fluidwwwwater)
			event.setResult(Result.ALLOW);
	}

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event)
	{
		Item item = event.crafting.getItem();
		EntityPlayer player = event.player;
		if (player instanceof FakePlayer || player.getName().contains("["))
			return;
		if (item.equals(ItemLoader.portableInventoryItem))
		{
			player.addStat(AchievementLoader.build_backpack);
		}
		if (item.equals(ItemLoader.garbagebag))
		{
			player.addStat(AchievementLoader.build_garbagebag);
		}
		if (item.equals(ItemLoader.limitlesstool))
		{
			player.addStat(AchievementLoader.build_tool);
		}
		if (item.equals(ItemLoader.invinciblering))
		{
			player.addStat(AchievementLoader.build_ring);
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		HashMap<Integer, HashSet<BlockPos>> toRemove = Maps.newHashMap(), toRemove_pump = Maps.newHashMap();
		if (TicketManager.WAIT_PLAYER.containsKey(event.player.getName()))
		{
			if (!TicketManager.WAIT_PLAYER.get(event.player.getName()).isEmpty())

				for (TicketManager.BlockPosWithDim o : TicketManager.WAIT_PLAYER.get(event.player.getName()))
				{
					BlockPos pos = o.pos;
					int dim = o.dim;
					WorldServer world = server.worldServerForDimension(dim);
					if (!world.isBlockLoaded(pos))
						world.getChunkProvider().loadChunk(pos.getX() >> 4, pos.getZ() >> 4);
					TileEntity te = world.getTileEntity(pos);
					if (te == null || !(te instanceof ChunkLoaderTileEntity))
					{
						if (!toRemove.containsKey(dim))
							toRemove.put(dim, Sets.newHashSet());
						toRemove.get(dim).add(pos);
						continue;
					}
					((ChunkLoaderTileEntity) te).setNeedUpdate(true);
				}
			TicketManager.WAIT_PLAYER.remove(event.player.getName());
		}
		if (!toRemove.isEmpty())
			for (int dim : toRemove.keySet())
			{
				if (!toRemove.get(dim).isEmpty())
					TicketManager.LOADED_LOADERS.get(dim).removeAll(toRemove.get(dim));
				if (TicketManager.LOADED_LOADERS.get(dim).isEmpty())
					TicketManager.LOADED_LOADERS.remove(dim);
			}
		if (TicketManager.WAIT_PLAYER_PUMP.containsKey(event.player.getName()))
		{
			if (!TicketManager.WAIT_PLAYER_PUMP.get(event.player.getName()).isEmpty())

				for (TicketManager.BlockPosWithDim o : TicketManager.WAIT_PLAYER_PUMP.get(event.player.getName()))
				{
					BlockPos pos = o.pos;
					int dim = o.dim;
					WorldServer world = server.worldServerForDimension(dim);
					if (!world.isBlockLoaded(pos))
						world.getChunkProvider().loadChunk(pos.getX() >> 4, pos.getZ() >> 4);
					TileEntity te = world.getTileEntity(pos);
					if (te == null || !(te instanceof LavaPumpTileEntity))
					{
						if (!toRemove_pump.containsKey(dim))
							toRemove_pump.put(dim, Sets.newHashSet());
						toRemove_pump.get(dim).add(pos);
						continue;
					}
					((LavaPumpTileEntity) te).setNeedUpdate(true);
				}
			TicketManager.WAIT_PLAYER_PUMP.remove(event.player.getName());
		}
		if (!toRemove_pump.isEmpty())
			for (int dim : toRemove_pump.keySet())
			{
				if (!toRemove_pump.get(dim).isEmpty())
					TicketManager.LOADED_PUMP.get(dim).removeAll(toRemove_pump.get(dim));
				if (TicketManager.LOADED_PUMP.get(dim).isEmpty())
					TicketManager.LOADED_PUMP.remove(dim);
			}
		MobSummonTileEntity.syncData((EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public void onPlayerLogOut(PlayerLoggedOutEvent event)
	{
		MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();
		HashMap<Integer, HashSet<BlockPos>> toRemove = Maps.newHashMap(), toRemove_pump = Maps.newHashMap();
		if (!TicketManager.LOADED_LOADERS.isEmpty())
		{
			for (int dim : TicketManager.LOADED_LOADERS.keySet())
			{
				try
				{
					if (Server.worldServerForDimension(dim) == null)
						throw new RuntimeException(String.format("Can't find world: %d\nLoad fail", dim));
				} catch (Exception e)
				{
					e.printStackTrace();
					toRemove.put(dim, TicketManager.LOADED_LOADERS.get(dim));
					continue;
				}
				WorldServer world = Server.worldServerForDimension(dim);
				if (!TicketManager.LOADED_LOADERS.get(dim).isEmpty())
					for (BlockPos pos : TicketManager.LOADED_LOADERS.get(dim))
					{
						if (!world.isBlockLoaded(pos))
							world.getChunkProvider().loadChunk(pos.getX() >> 4, pos.getZ() >> 4);
						TileEntity te = world.getTileEntity(pos);
						if (te == null || !(te instanceof ChunkLoaderTileEntity))
						{
							if (!toRemove.containsKey(dim))
								toRemove.put(dim, Sets.newHashSet());
							toRemove.get(dim).add(pos);
							continue;
						} else
						{
							((ChunkLoaderTileEntity) te).onPlayerLogOut(event.player.getName());
						}

					}
			}
			if (!toRemove.isEmpty())
				for (int dim : toRemove.keySet())
				{
					if (!toRemove.get(dim).isEmpty())
						TicketManager.LOADED_LOADERS.get(dim).removeAll(toRemove.get(dim));
					if (TicketManager.LOADED_LOADERS.get(dim).isEmpty())
						TicketManager.LOADED_LOADERS.remove(dim);
				}

		}
		if (!TicketManager.LOADED_PUMP.isEmpty())
		{
			for (int dim : TicketManager.LOADED_PUMP.keySet())
			{
				try
				{
					if (Server.worldServerForDimension(dim) == null)
						throw new RuntimeException(String.format("Can't find world: %d\nLoad fail", dim));
				} catch (Exception e)
				{
					e.printStackTrace();
					toRemove_pump.put(dim, TicketManager.LOADED_PUMP.get(dim));
					continue;
				}
				WorldServer world = Server.worldServerForDimension(dim);
				if (!TicketManager.LOADED_PUMP.get(dim).isEmpty())
					for (BlockPos pos : TicketManager.LOADED_PUMP.get(dim))
					{
						if (!world.isBlockLoaded(pos))
							world.getChunkProvider().loadChunk(pos.getX() >> 4, pos.getZ() >> 4);
						TileEntity te = world.getTileEntity(pos);
						if (te == null || !(te instanceof LavaPumpTileEntity))
						{
							if (!toRemove_pump.containsKey(dim))
								toRemove_pump.put(dim, Sets.newHashSet());
							toRemove_pump.get(dim).add(pos);
							continue;
						} else
						{
							((LavaPumpTileEntity) te).onPlayerLogOut(event.player.getName());
						}

					}
			}
			if (!toRemove_pump.isEmpty())
				for (int dim : toRemove_pump.keySet())
				{
					if (!toRemove_pump.get(dim).isEmpty())
						TicketManager.LOADED_PUMP.get(dim).removeAll(toRemove_pump.get(dim));
					if (TicketManager.LOADED_PUMP.get(dim).isEmpty())
						TicketManager.LOADED_PUMP.remove(dim);
				}

		}
	}

	public void onTick(ServerTickEvent event)
	{
		if (MobSummonTileEntity.FirstStart)
			return;
		MobSummonTileEntity.checkSync();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(TickEvent.ClientTickEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (KeyLoader.openBackpack.isKeyDown())
		{
			Helper.sendKey(player, KeyLoader.key_backpack);
		}
		if (KeyLoader.openGarbageBag.isKeyDown())
		{
			Helper.sendKey(player, KeyLoader.key_garbage);
		}
		if (KeyLoader.openCrafter.isKeyDown())
		{
			Helper.sendKey(player, KeyLoader.key_crafter);
		}
		if (KeyLoader.openTp.isKeyDown())
		{
			Helper.sendKey(player, KeyLoader.key_tp);
		}
	}
}
