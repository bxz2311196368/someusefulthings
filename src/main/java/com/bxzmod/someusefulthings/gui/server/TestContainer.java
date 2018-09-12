package com.bxzmod.someusefulthings.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class TestContainer extends Container
{

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
