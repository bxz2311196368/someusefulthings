package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.SpecialCrops;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class BXZDebug extends CommandBase
{
	@Override
	public String getName()
	{
		return "bxz_debug";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 2)
			throw new CommandException("bad args");
		if (args[0].equalsIgnoreCase("GreenHouse") && args[1].equalsIgnoreCase("reinit"))
			SpecialCrops.reinit();
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos)
	{
		if (args.length == 1)
			return CommandBase.getListOfStringsMatchingLastWord(args, new String[] { "GreenHouse" });
		if (args.length == 2)
			return CommandBase.getListOfStringsMatchingLastWord(args, new String[] { "reinit" });
		return super.getTabCompletions(server, sender, args, pos);
	}
}
