package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.tileentity.ChunkLoaderTileEntity;
import com.bxzmod.someusefulthings.tileentity.LavaPumpTileEntity;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class TicketManager
{
	public static HashMap<ChunkInfo, Ticket> TICKETS = Maps.newHashMap(), PUMP_TICKETS = Maps.newHashMap();

	public static HashMap<Integer, HashSet<BlockPos>> LOADED_LOADERS = Maps.newHashMap(),
			LOADED_PUMP = Maps.newHashMap();

	public static HashMap<String, HashSet<BlockPosWithDim>> WAIT_PLAYER = Maps.newHashMap(),
			WAIT_PLAYER_PUMP = Maps.newHashMap();

	public static File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "BXZ_Force_Chunk_Loader.txt"),
			file_pump = new File(DimensionManager.getCurrentSaveRootDirectory(), "BXZ_Lava_Pump.txt");

	static
	{
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (!file_pump.exists())
		{
			try
			{
				file_pump.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void saveData() throws IOException
	{
		Gson gson = new Gson();
		String json = gson.toJson(LOADED_LOADERS), pump = gson.toJson(LOADED_PUMP);
		Files.write(json.replaceAll("field_177962_a", "x").replaceAll("field_177960_b", "y")
				.replaceAll("field_177961_c", "z"), file, Charsets.UTF_8);
		Files.write(pump.replaceAll("field_177962_a", "x").replaceAll("field_177960_b", "y")
				.replaceAll("field_177961_c", "z"), file_pump, Charsets.UTF_8);
	}

	public static void LoadData() throws IOException
	{
		String json = Files.toString(file, Charsets.UTF_8), pump = Files.toString(file_pump, Charsets.UTF_8);
		Gson gson = new Gson();
		LOADED_LOADERS = gson.fromJson(json.replaceAll("x", "field_177962_a").replaceAll("y", "field_177960_b")
				.replaceAll("z", "field_177961_c"), new TypeToken<HashMap<Integer, HashSet<BlockPos>>>()
				{
				}.getType());
		LOADED_PUMP = gson.fromJson(pump.replaceAll("x", "field_177962_a").replaceAll("y", "field_177960_b")
				.replaceAll("z", "field_177961_c"), new TypeToken<HashMap<Integer, HashSet<BlockPos>>>()
				{
				}.getType());
		if (LOADED_LOADERS == null)
			LOADED_LOADERS = Maps.newHashMap();
		if (LOADED_PUMP == null)
			LOADED_PUMP = Maps.newHashMap();
	}

	public static void init() throws IOException
	{
		LoadData();
		MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();
		HashMap<Integer, HashSet<BlockPos>> toRemove = Maps.newHashMap(), toRemove_pump = Maps.newHashMap();
		if (!LOADED_LOADERS.isEmpty())
		{
			for (int dim : LOADED_LOADERS.keySet())
			{
				try
				{
					if (Server.worldServerForDimension(dim) == null)
						throw new RuntimeException(String.format("Can't find world: %d\nLoad fail", dim));
				} catch (Exception e)
				{
					e.printStackTrace();
					toRemove.put(dim, LOADED_LOADERS.get(dim));
					continue;
				}
				WorldServer world = Server.worldServerForDimension(dim);
				if (!LOADED_LOADERS.get(dim).isEmpty())
					for (BlockPos pos : LOADED_LOADERS.get(dim))
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
						}

					}
			}
			if (!toRemove.isEmpty())
				for (int dim : toRemove.keySet())
				{
					if (!toRemove.get(dim).isEmpty())
						LOADED_LOADERS.get(dim).removeAll(toRemove.get(dim));
					if (LOADED_LOADERS.get(dim).isEmpty())
						LOADED_LOADERS.remove(dim);
				}

		}
		if (!LOADED_PUMP.isEmpty())
		{
			for (int dim : LOADED_PUMP.keySet())
			{
				try
				{
					if (Server.worldServerForDimension(dim) == null)
						throw new RuntimeException(String.format("Can't find world: %d\nLoad fail", dim));
				} catch (Exception e)
				{
					e.printStackTrace();
					toRemove_pump.put(dim, LOADED_PUMP.get(dim));
					continue;
				}
				WorldServer world = Server.worldServerForDimension(dim);
				if (!LOADED_PUMP.get(dim).isEmpty())
					for (BlockPos pos : LOADED_PUMP.get(dim))
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
						}

					}
			}
			if (!toRemove_pump.isEmpty())
				for (int dim : toRemove_pump.keySet())
				{
					if (!toRemove_pump.get(dim).isEmpty())
						LOADED_PUMP.get(dim).removeAll(toRemove_pump.get(dim));
					if (LOADED_PUMP.get(dim).isEmpty())
						LOADED_PUMP.remove(dim);
				}

		}
		saveData();
	}

	public static void clear()
	{
		TICKETS.clear();
		PUMP_TICKETS.clear();
		LOADED_LOADERS.clear();
		LOADED_PUMP.clear();
		WAIT_PLAYER.clear();
		WAIT_PLAYER_PUMP.clear();
	}

	public static class ChunkInfo
	{
		public int dim;
		public ChunkPos pos;

		public ChunkInfo(int dim, ChunkPos pos)
		{
			this.dim = dim;
			this.pos = pos;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + dim;
			result = prime * result + ((pos == null) ? 0 : pos.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChunkInfo other = (ChunkInfo) obj;
			if (dim != other.dim)
				return false;
			if (pos == null)
			{
				if (other.pos != null)
					return false;
			} else if (!pos.equals(other.pos))
				return false;
			return true;
		}

	}

	public static class BlockPosWithDim
	{
		public BlockPos pos;
		public int dim;

		public BlockPosWithDim(BlockPos pos, int dim)
		{
			this.pos = pos;
			this.dim = dim;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + dim;
			result = prime * result + ((pos == null) ? 0 : pos.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BlockPosWithDim other = (BlockPosWithDim) obj;
			if (dim != other.dim)
				return false;
			if (pos == null)
			{
				if (other.pos != null)
					return false;
			} else if (!pos.equals(other.pos))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return Objects.toStringHelper("BlockPosWithDim").add("dim", dim).add("x", pos.getX()).add("y", pos.getY())
					.add("z", pos.getZ()).toString();
		}

	}

}
