package com.bxzmod.someusefulthings.network;

import com.bxzmod.someusefulthings.ModInfo;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkLoader
{
	public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);

	private static int nextID = 0;

	public NetworkLoader(FMLPreInitializationEvent event)
	{
		registerMessage(DataInteraction.ToClientHandler.class, DataInteraction.class, Side.CLIENT);
		registerMessage(DataInteraction.ToServerHandler.class, DataInteraction.class, Side.SERVER);
		registerMessage(ToolSettingSync.ToServerSetting.class, ToolSettingSync.class, Side.SERVER);
		registerMessage(GarbageBagSetting.ToServerSetting.class, GarbageBagSetting.class, Side.SERVER);
		registerMessage(GarbageBagSetting.ToClientSetting.class, GarbageBagSetting.class, Side.CLIENT);
		registerMessage(KeyPressReception.ToServerHandler.class, KeyPressReception.class, Side.SERVER);
		registerMessage(MultiSignSync.ToClientHandler.class, MultiSignSync.class, Side.CLIENT);
		registerMessage(MultiSignSync.ToServerHandler.class, MultiSignSync.class, Side.SERVER);
		registerMessage(TPLocationSync.ToClientHandler.class, TPLocationSync.class, Side.CLIENT);
		registerMessage(TPLocationSync.ToServerHandler.class, TPLocationSync.class, Side.SERVER);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
			Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		instance.registerMessage(messageHandler, requestMessageType, nextID++, side);
	}
}
