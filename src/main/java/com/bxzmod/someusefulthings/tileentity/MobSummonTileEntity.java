package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MobSummonTileEntity extends TileEntity implements ITickable
{
	private static List<String> Entitys = EntityList.getEntityNameList();
	private static Set<String> RemovedEntity = Sets.newHashSet();
	public static HashMap<String, String> EntityDisplayName = Maps.newHashMap();
	public static Set<String> PlayerNeedSync = Sets.newHashSet();
	public static boolean FirstStart = true, FirstClinetStart = true;

	static
	{
		Entitys.removeAll(RemovedEntity);
	}

	private String selectEntity = "";

	private int tick = 1;

	public static void testEntityList()
	{
		if (!FirstStart)
			return;
		else
			FirstStart = false;
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "BXZ_Entity_Blacklist.txt");
		Gson gson = new Gson();
		try
		{
			if (!file.exists())
				file.createNewFile();
			else
			{
				String json = Files.toString(file, Charsets.UTF_8);
				ArrayList<String> temp = gson.fromJson(json, new TypeToken<ArrayList<String>>()
				{
				}.getType());
				if (temp == null || temp.isEmpty())
					throw new Exception("");
				else
				{
					Entitys.removeAll(temp);
					RemovedEntity.addAll(temp);
				}
			}
		} catch (Exception e)
		{
			Helper.LOGGER.info("read file \"BXZ_Entity_Blacklist.txt\" fail!");
		}
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		World world0 = server.worldServerForDimension(0);
		world0.getChunkProvider().provideChunk(0, 0);
		for (String entityname : Entitys)
		{
			Entity entity = EntityList.createEntityByName(entityname, world0);
			try
			{
				if (entity == null || !(entity instanceof EntityLivingBase))
				{
					RemovedEntity.add(entityname);
					continue;
				}
				entity.setPositionAndRotation(0, 0, 0, 0, 0);
				entity.onUpdate();
			} catch (Exception e)
			{
				RemovedEntity.add(entityname);
			}
			EntityDisplayName.put(entityname, entity.getDisplayName().getFormattedText());
		}
		Entitys.removeAll(RemovedEntity);
		try
		{
			String json = gson.toJson(RemovedEntity);
			Files.write(json, file, Charsets.UTF_8);
		} catch (Exception e)
		{
			Helper.LOGGER.info("write file \"BXZ_Entity_Blacklist.txt\" fail!");
		}
	}

	@Override
	public void update()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if (tick++ % 100 != 0)
			return;
		if (this.selectEntity == null || this.selectEntity.isEmpty())
			return;
		try
		{
			Entity entity = EntityList.createEntityByName(selectEntity, getWorld());
			if (entity == null || !(entity instanceof EntityLivingBase))
			{
				Entitys.remove(this.selectEntity);
				RemovedEntity.add(this.selectEntity);
				this.selectEntity = "";
				return;
			}
			entity.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 0, 0);
			entity.onUpdate();
			world.spawnEntity(entity);
		} catch (Exception e)
		{
			Entitys.remove(this.selectEntity);
			RemovedEntity.add(this.selectEntity);
			this.selectEntity = "";
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		this.selectEntity = compound.getString("selectEntity");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setString("selectEntity", this.selectEntity);
		return compound;
	}

	public static void syncData(EntityPlayerMP player)
	{
		if (FirstStart)
			PlayerNeedSync.add(player.getName());
		else
			syncTo(player);
	}

	private static void syncTo(EntityPlayerMP player)
	{
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		message.nbt.setString("Type", "MS_Sync");
		message.nbt.setTag("Data", dataToNBT());
		NetworkLoader.instance.sendTo(message, player);
	}

	public static void checkSync()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (!PlayerNeedSync.isEmpty())
			for (String name : PlayerNeedSync)
				syncTo(server.getPlayerList().getPlayerByUsername(name));
		PlayerNeedSync.clear();
	}

	private static NBTTagCompound dataToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		Gson gson = new Gson();
		nbt.setString("entitys", gson.toJson(Entitys));
		return nbt;
	}

	public static void NBTToData(NBTTagCompound nbt)
	{
		Gson gson = new Gson();
		Entitys = gson.fromJson(nbt.getString("entitys"), new TypeToken<ArrayList<String>>()
		{
		}.getType());
	}

	public void setClinetWorldEntityNames()
	{
		if (FirstClinetStart)
		{
			FirstClinetStart = false;
			EntityDisplayName.clear();
			for (String e : Entitys)
			{
				Entity entity = EntityList.createEntityByName(e, world);
				if (entity != null)
					EntityDisplayName.put(e, entity.getDisplayName().getFormattedText());
			}

		}
	}

	public String getSelectEntity()
	{
		return selectEntity;
	}

	public void setSelectEntity(String selectEntity)
	{
		this.selectEntity = selectEntity;
	}

	public static List<String> getEntitys()
	{
		return Entitys;
	}

}
