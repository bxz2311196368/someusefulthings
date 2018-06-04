package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.Helper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class TestCommand extends CommandBase
{

	@Override
	public String getName()
	{
		return "bxz_tp_test";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 1)
			throw new CommandException("error");
		int dim = Integer.valueOf(args[0]);
		Helper.teleportEntity(sender.getCommandSenderEntity(), new BlockPos(0, 64, 0), dim);

	}

}
