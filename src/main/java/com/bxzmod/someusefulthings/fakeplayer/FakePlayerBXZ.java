package com.bxzmod.someusefulthings.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerBXZ extends FakePlayer
{

	public FakePlayerBXZ(WorldServer world, GameProfile name)
	{
		super(world, name);
	}

	@Override
	public boolean canAttackPlayer(EntityPlayer player)
	{
		return true;
	}
}
