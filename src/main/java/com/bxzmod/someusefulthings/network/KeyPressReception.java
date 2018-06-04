package com.bxzmod.someusefulthings.network;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.keybinding.KeyLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class KeyPressReception implements IMessage
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

	public static class ToServerHandler implements IMessageHandler<KeyPressReception, IMessage>
	{

		@Override
		public IMessage onMessage(KeyPressReception message, MessageContext ctx)
		{
			final MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (ctx.side == Side.SERVER)
			{
				NBTTagCompound nbt_temp = (NBTTagCompound) message.nbt.copy();
				final String name = message.nbt.getString("name");
				final int key = message.nbt.getInteger("key");
				Server.addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						EntityPlayer player = Server.getPlayerList().getPlayerByUsername(name);
						if (player == null)
							return;
						if (key == KeyLoader.key_backpack)
						{
							BlockPos pos = player.getPosition();
							player.openGui(Main.instance, GuiLoader.GUI_P_I, player.world, pos.getX(), pos.getY(),
									pos.getZ());
						}
						if (key == KeyLoader.key_garbage)
						{
							BlockPos pos = player.getPosition();
							player.openGui(Main.instance, GuiLoader.GUI_G_B, player.world, pos.getX(), pos.getY(),
									pos.getZ());
						}
						if (key == KeyLoader.key_crafter)
						{
							BlockPos pos = player.getPosition();
							player.openGui(Main.instance, GuiLoader.GUI_W_B_C, player.world, pos.getX(), pos.getY(),
									pos.getZ());
						}
						if (key == KeyLoader.key_tp)
						{
							BlockPos pos = player.getPosition();
							player.openGui(Main.instance, GuiLoader.GUI_T_P, player.world, pos.getX(), pos.getY(),
									pos.getZ());
						}
					}
				});

			}
			return null;
		}
	}

}
