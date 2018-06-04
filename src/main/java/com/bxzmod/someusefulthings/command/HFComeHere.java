package com.bxzmod.someusefulthings.command;

import com.bxzmod.someusefulthings.Helper;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;

public class HFComeHere extends CommandBase
{

	@Override
	public String getName()
	{
		return "hf_come_here";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity p = sender.getCommandSenderEntity();
		if (!(p instanceof EntityPlayer) || p instanceof FakePlayer)
			throw new WrongUsageException(new TextComponentTranslation("command.trade.wrong_2", TextFormatting.RED)
					.getFormattedText() + '\n'
					+ new TextComponentTranslation("command.trade.wrong_0", TextFormatting.RED).getFormattedText());
		EntityPlayer player = (EntityPlayer) p;
		ArrayList<EntityNPC> npcs = (ArrayList<EntityNPC>) sender.getEntityWorld()
				.getEntitiesWithinAABB(EntityNPC.class, new AxisAlignedBB(player.getPosition()).expand(64, 64, 64));
		if (npcs != null && !npcs.isEmpty())
		{
			for (EntityNPC entity : npcs)
			{
				Helper.tpNPC(entity, player);
			}
			sender.sendMessage(new TextComponentString("success!"));
		} else
		{
			sender.sendMessage(new TextComponentString("none npc!"));
		}
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
