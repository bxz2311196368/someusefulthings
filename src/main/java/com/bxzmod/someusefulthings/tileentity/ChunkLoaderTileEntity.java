package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.TicketManager;
import com.bxzmod.someusefulthings.TicketManager.BlockPosWithDim;
import com.bxzmod.someusefulthings.TicketManager.ChunkInfo;
import com.bxzmod.someusefulthings.blocks.BlockLoader;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashSet;

public class ChunkLoaderTileEntity extends TileEntity implements ITickable
{
	private String player;

	private Ticket ticket;

	private boolean needUpdate = false, canLoad = true;

	private int n = 0, time = 60;

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		player = compound.getString("Player");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setString("Player", player);
		return compound;
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if (world.isRemote)
			return;
		unforceLoad();
	}

	@Override
	public void validate()
	{
		super.validate();
		if (world.isRemote)
			return;
		forceLoad();
	}

	private void forceLoad()
	{
		if (player == null)
		{
			this.needUpdate = true;
			return;
		} else
			this.needUpdate = false;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		EntityPlayer playerBind = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayerByUsername(player);
		if (playerBind == null)
		{
			if (TicketManager.WAIT_PLAYER.isEmpty() || !TicketManager.WAIT_PLAYER.containsKey(player))
				TicketManager.WAIT_PLAYER.put(player, Sets.newHashSet());
			TicketManager.WAIT_PLAYER.get(player).add(new BlockPosWithDim(pos, world.provider.getDimension()));
			return;
		}
		ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		ChunkInfo info = new ChunkInfo(world.provider.getDimension(), chunk);
		if (TicketManager.TICKETS.containsKey(info))
		{
			this.canLoad = false;
			return;
		}
		if (ticket == null)
			ticket = ForgeChunkManager.requestPlayerTicket(Main.instance, player, world, Type.ENTITY);
		ticket.bindEntity(playerBind);
		HashSet<ChunkPos> chunks = this.getChunks(chunk);
		TicketManager.TICKETS.put(info, ticket);
		if (TicketManager.LOADED_LOADERS.isEmpty()
				|| !TicketManager.LOADED_LOADERS.containsKey(world.provider.getDimension()))
			TicketManager.LOADED_LOADERS.put(world.provider.getDimension(), Sets.newHashSet());
		TicketManager.LOADED_LOADERS.get(world.provider.getDimension()).add(getPos());
		for (ChunkPos p : chunks)
			ForgeChunkManager.forceChunk(ticket, p);

	}

	private void unforceLoad()
	{
		if (world.getBlockState(pos).getBlock() != BlockLoader.chunkLoader)
			TicketManager.LOADED_LOADERS.get(world.provider.getDimension()).remove(getPos());
		if (ticket == null)
			return;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if (player == null)
			throw new RuntimeException("Can't find player");
		ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		HashSet<ChunkPos> chunks = this.getChunks(chunk);
		for (ChunkPos p : chunks)
			ForgeChunkManager.unforceChunk(ticket, p);
		ForgeChunkManager.releaseTicket(ticket);
		TicketManager.TICKETS.remove(new ChunkInfo(world.provider.getDimension(), chunk));
	}

	public void onPlayerLogOut(String player)
	{
		if (this.player.equals(player))
		{
			ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
			HashSet<ChunkPos> chunks = this.getChunks(chunk);
			for (ChunkPos p : chunks)
				ForgeChunkManager.unforceChunk(ticket, p);
			ForgeChunkManager.releaseTicket(ticket);
			TicketManager.TICKETS.remove(new ChunkInfo(world.provider.getDimension(), chunk));
			this.ticket = null;
		}
	}

	private HashSet<ChunkPos> getChunks(ChunkPos chunk)
	{
		HashSet<ChunkPos> chunks = Sets.newHashSet();
		for (int i = -2; i <= 2; i++)
			for (int j = -2; j <= 2; j++)
			{
				ChunkPos temp = new ChunkPos(chunk.chunkXPos + i, chunk.chunkZPos + j);
				chunks.add(temp);
			}
		return chunks;
	}

	public String getPlayer()
	{
		return player;
	}

	public void setPlayer(String player)
	{
		this.player = player;
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;

		if (canLoad)
		{
			if (needUpdate)
				this.forceLoad();
		} else
		{
			if (n++ > 5 && world.getMinecraftServer().getTickCounter() % 20 == 0)
			{
				PlayerList players = world.getMinecraftServer().getPlayerList();
				for (EntityPlayerMP player : players.getPlayers())
					player.sendMessage(new TextComponentTranslation("error.chunkloader", TextFormatting.RED,
							world.provider.getDimension(), pos.toString(), time--));
				if (time <= 0)
					world.setBlockToAir(pos);
			}
		}

	}

	public void setNeedUpdate(boolean needUpdate)
	{
		this.needUpdate = needUpdate;
	}

}
