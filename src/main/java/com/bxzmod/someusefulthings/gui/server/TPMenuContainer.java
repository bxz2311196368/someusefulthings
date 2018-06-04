package com.bxzmod.someusefulthings.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class TPMenuContainer extends Container
{
	public EntityPlayer player;

	public TPMenuContainer(EntityPlayer player)
	{
		super();
		this.player = player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

}
