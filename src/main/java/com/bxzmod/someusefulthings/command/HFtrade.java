package com.bxzmod.someusefulthings.command;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.player.IPlayerStats;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class HFtrade extends CommandBase
{

	@Override
	public String getName()
	{
		return "hf_trade";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return new TextComponentTranslation("command.trade.usage", '\n').getFormattedText();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 2)
			throw new WrongUsageException(
					new TextComponentTranslation("command.trade.wrong_1", TextFormatting.RED).getFormattedText() + '\n'
							+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED));
		Entity p = sender.getCommandSenderEntity();
		if (!(p instanceof EntityPlayer) || p instanceof FakePlayer)
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_2", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		EntityPlayer player = (EntityPlayer) p;
		IPlayerHelper hf_helper = HFApi.player;
		IPlayerStats states = hf_helper.getStatsForPlayer(player);
		EntityPlayer player_accept = server.getPlayerList().getPlayerByUsername(args[0]);
		long amount = 0;
		if (args[1].equals("all"))
			amount = states.getGold();
		else if (!args[1].matches("[0-9]+"))
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_3", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		else
			amount = Long.valueOf(args[1]);
		if (amount <= 0)
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_4", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		if (player_accept == null)
			throw new WrongUsageException(
					new TextComponentTranslation("command.trade.wrong_5", TextFormatting.RED, args[0])
							.getFormattedText() + '\n'
							+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED)
									.getFormattedText());
		if (player.getName().equals(player_accept.getName()))
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_6", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		if (states.getGold() < amount)
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_7", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		IPlayerStats states_accept = hf_helper.getStatsForPlayer(player_accept);
		states_accept.setGold(states_accept.getGold() + amount);
		states.setGold(states.getGold() - amount);
		sender.sendMessage(new TextComponentTranslation("command.trade.msg_1", TextFormatting.GREEN, amount, '\n',
				TextFormatting.BLUE, states.getGold(), '\n', TextFormatting.BLUE, states_accept.getGold()));
		player_accept.sendMessage(
				new TextComponentTranslation("command.trade.msg_2", TextFormatting.GREEN, player.getName(), amount));

	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			String[] names = server.getOnlinePlayerNames();
			return CommandBase.getListOfStringsMatchingLastWord(args, names);
		} else if (args.length == 2)
		{
			String[] names = { "all" };
			return CommandBase.getListOfStringsMatchingLastWord(args, names);
		}
		return Collections.<String>emptyList();
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return -1;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

}
