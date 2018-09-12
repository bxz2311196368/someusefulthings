package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.AbstractContainer;
import net.minecraft.util.ResourceLocation;

public class ReinforcementMachineGuiContainer extends BaseGuiContainer
{

	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/ReinforcementMachine.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public ReinforcementMachineGuiContainer(AbstractContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "reinforcementMachine", TEXTURE, 176, 133);
	}

}
