package com.bxzmod.someusefulthings.network;

import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.capability.ITPLocation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class TPLocationSync implements IMessage
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

	public static class ToClientHandler implements IMessageHandler<TPLocationSync, IMessage>
	{

		@Override
		public IMessage onMessage(TPLocationSync message, MessageContext ctx)
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
						if (player.hasCapability(CapabilityLoader.PORTABLE_INVENTORY, null))
						{
							ITPLocation cp = player.getCapability(CapabilityLoader.TP_MENU, null);
							IStorage<ITPLocation> storage = CapabilityLoader.TP_MENU.getStorage();
							storage.readNBT(CapabilityLoader.TP_MENU, cp, null, nbt);
						}

					}

				});
			}
			return null;
		}

	}

	public static class ToServerHandler implements IMessageHandler<TPLocationSync, IMessage>
	{

		@Override
		public IMessage onMessage(TPLocationSync message, MessageContext ctx)
		{
			final MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (ctx.side == Side.SERVER)
			{
				final NBTTagCompound nbt = (NBTTagCompound) message.nbt;
				final String name = message.nbt.getString("name");
				Server.addScheduledTask(new Runnable()
				{

					@Override
					public void run()
					{
						EntityPlayerMP player = Server.getPlayerList().getPlayerByUsername(name);
						if (player != null && player.hasCapability(CapabilityLoader.TP_MENU, null))
						{
							ITPLocation cp = player.getCapability(CapabilityLoader.TP_MENU, null);
							IStorage<ITPLocation> storage = CapabilityLoader.TP_MENU.getStorage();
							storage.readNBT(CapabilityLoader.TP_MENU, cp, null, nbt);
						}

					}

				});
			}

			return null;
		}

	}
}
