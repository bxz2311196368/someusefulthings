package com.bxzmod.someusefulthings.network;

import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.capability.IGarbagBag;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GarbageBagSetting implements IMessage
{

	public NBTTagCompound nbt;

	@Override
	public void fromBytes(ByteBuf buf)
	{
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbt);
	}

	public static class ToServerSetting implements IMessageHandler<GarbageBagSetting, IMessage>
	{
		private static final Logger LOGGER = LogManager.getLogger();

		@Override
		public IMessage onMessage(GarbageBagSetting message, MessageContext ctx)
		{
			final MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (ctx.side == Side.SERVER)
			{
				final String name = message.nbt.getString("name");
				final NBTTagCompound nbt = message.nbt;
				Server.addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						EntityPlayer player = Server.getPlayerList().getPlayerByUsername(name);
						if (player != null && player.hasCapability(CapabilityLoader.GARBAGBAG, null))
						{
							IGarbagBag cp = player.getCapability(CapabilityLoader.GARBAGBAG, null);
							IStorage<IGarbagBag> storage = CapabilityLoader.GARBAGBAG.getStorage();
							storage.readNBT(CapabilityLoader.GARBAGBAG, cp, null, nbt);
						}
					}
				});
			}
			return null;
		}

	}

	public static class ToClientSetting implements IMessageHandler<GarbageBagSetting, IMessage>
	{

		@Override
		public IMessage onMessage(GarbageBagSetting message, MessageContext ctx)
		{
			if (ctx.side == Side.CLIENT)
			{
				final NBTTagCompound nbt = (NBTTagCompound) message.nbt;
				Minecraft.getMinecraft().addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						EntityPlayer player = Minecraft.getMinecraft().player;
						if (player.hasCapability(CapabilityLoader.GARBAGBAG, null))
						{
							IGarbagBag cp = player.getCapability(CapabilityLoader.GARBAGBAG, null);
							IStorage<IGarbagBag> storage = CapabilityLoader.GARBAGBAG.getStorage();
							storage.readNBT(CapabilityLoader.GARBAGBAG, cp, null, nbt);
						}
					}

				});
			}
			return null;
		}

	}
}
