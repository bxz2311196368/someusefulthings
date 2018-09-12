package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class BXZGuiTest extends CommandBase
{
	@Override
	public String getName()
	{
		return "bxz_gui_test";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity entity = sender.getCommandSenderEntity();
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			player.openGui(Main.instance, GuiLoader.GUI_TEST, sender.getEntityWorld(), (int) player.posX,
					(int) player.posY, (int) player.posZ);
		}
	}
}
