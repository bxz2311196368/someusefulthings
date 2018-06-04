package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.config.ConfigLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Easyset extends CommandBase
{

	public Easyset()
	{

	}

	@Override
	public String getName()
	{
		return "easyset";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return new TextComponentTranslation("command.easyset.useage", "\n", "\n").getFormattedText();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length > 2)
			throw new WrongUsageException("command.easyset.wrong_1", "\n");
		else if (args.length == 0)
			throw new WrongUsageException("command.easyset.wrong_2", "\n");
		else if (args[0].equalsIgnoreCase("settings"))
		{
			if (server.isServerInOnlineMode())
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_0"));
			else
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_1"));
			if (server.worldServerForDimension(0).getGameRules().getBoolean("keepInventory"))
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_2"));
			else
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_3"));
			if (server.worldServerForDimension(0).getGameRules().getBoolean("mobGriefing"))
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_4"));
			else
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_5"));
		} else if (args.length == 1 && !(args[0].equalsIgnoreCase("settings")))
			throw new WrongUsageException(
					new TextComponentTranslation("command.easyset.wrong_3", "\n").getFormattedText());
		else if (args.length == 2 && args[0].equalsIgnoreCase("onlinemode"))
		{
			if (args[1].equalsIgnoreCase("true"))
			{
				server.setOnlineMode(true);
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_0"));
			}
			if (args[1].equalsIgnoreCase("false"))
			{
				server.setOnlineMode(false);
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_1"));
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("keepInventory"))
		{
			if (args[1].equalsIgnoreCase("true"))
			{
				server.worldServerForDimension(0).getGameRules().setOrCreateGameRule("keepInventory", "true");
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_2"));
			}
			if (args[1].equalsIgnoreCase("false"))
			{
				server.worldServerForDimension(0).getGameRules().setOrCreateGameRule("keepInventory", "false");
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_3"));
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("mobGriefing"))
		{
			if (args[1].equalsIgnoreCase("true"))
			{
				server.worldServerForDimension(0).getGameRules().setOrCreateGameRule("mobGriefing", "true");
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_4"));
			}
			if (args[1].equalsIgnoreCase("false"))
			{
				server.worldServerForDimension(0).getGameRules().setOrCreateGameRule("mobGriefing", "false");
				sender.sendMessage(new TextComponentTranslation("command.easyset.msg_5"));
			}
		} else
			throw new WrongUsageException("command.easyset.wrong_3", "\n");

	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			String[] names = { "settings", "onlinemode", "keepInventory", "mobGriefing" };
			return CommandBase.getListOfStringsMatchingLastWord(args, names);
		} else if (args.length == 2 && (args[0].equalsIgnoreCase("onlinemode")
				|| args[0].equalsIgnoreCase("keepInventory") || args[0].equalsIgnoreCase("mobGriefing")))
		{
			String[] names = { "true", "false" };
			return CommandBase.getListOfStringsMatchingLastWord(args, names);
		}
		return Collections.<String>emptyList();
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		if (ConfigLoader.need_op)
			return 4;
		return -1;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		if (ConfigLoader.need_op)
			return super.checkPermission(server, sender);
		return true;
	}

}
