package com.bxzmod.someusefulthings.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

public class KeyLoader
{

	public static KeyBinding openBackpack;
	public static KeyBinding openGarbageBag;
	public static KeyBinding openCrafter;
	public static KeyBinding openTp;

	public static final int key_backpack = Keyboard.KEY_H;
	public static final int key_garbage = Keyboard.KEY_N;
	public static final int key_crafter = Keyboard.KEY_COMMA;
	public static final int key_tp = Keyboard.KEY_X;

	public KeyLoader(FMLInitializationEvent event)
	{
		openBackpack = new KeyBinding("key.bxz.openBackpack", key_backpack, "key.bxz.group");
		openGarbageBag = new KeyBinding("key.bxz.openGarbageBag", key_garbage, "key.bxz.group");
		openCrafter = new KeyBinding("key.bxz.openCrafter", key_crafter, "key.bxz.group");
		openTp = new KeyBinding("key.bxz.openTp", key_tp, "key.bxz.group");

		ClientRegistry.registerKeyBinding(openBackpack);
		ClientRegistry.registerKeyBinding(openGarbageBag);
		ClientRegistry.registerKeyBinding(openCrafter);
		ClientRegistry.registerKeyBinding(openTp);
	}

}
