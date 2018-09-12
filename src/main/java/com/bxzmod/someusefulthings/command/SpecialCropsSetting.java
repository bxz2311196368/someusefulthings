package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.SpecialCrops;
import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SpecialCropsSetting extends CommandBase
{
	@Override
	public String getName()
	{
		return "bxz_greenHouse";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return new TextComponentTranslation("command.greenHouse.useage", "\n", "\n").getFormattedText();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 1)
			throw new WrongUsageException("command.greenHouse.wrong");
		if (args[0].equalsIgnoreCase("default"))
		{
			SpecialCrops.reinit();
			return;
		}
		if (args[0].equalsIgnoreCase("reload"))
		{
			try
			{
				SpecialCrops.loadData();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return;
		}
		throw new WrongUsageException("command.greenHouse.wrong");
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos)
	{

		if (args.length == 1)
		{
			return ImmutableList.of("default", "reload");
		}
		return Collections.emptyList();
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return -1;
	}
}
