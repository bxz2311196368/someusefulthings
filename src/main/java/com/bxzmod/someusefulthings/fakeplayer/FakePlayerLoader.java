package com.bxzmod.someusefulthings.fakeplayer;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.HashMap;
import java.util.UUID;

public class FakePlayerLoader
{
	private static GameProfile gameProfile;

	public static HashMap<Integer, FakePlayer> listFakePlayer = Maps.newHashMap();

	public FakePlayerLoader(FMLInitializationEvent event)
	{
		gameProfile = new GameProfile(UUID.fromString("A4EEC6D7-FA7B-42F9-90CE-4408A2946494"), "[BXZ]");
	}

	public static FakePlayer getFakePlayer(WorldServer server)
	{
		if (!listFakePlayer.containsKey(server.provider.getDimension()))
			listFakePlayer.put(server.provider.getDimension(), new FakePlayerBXZ(server, gameProfile));
		return listFakePlayer.get(server.provider.getDimension());
	}

}
