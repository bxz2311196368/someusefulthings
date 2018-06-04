package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MobSummonContainer extends Container
{
	private EntityPlayer player;
	private MobSummonTileEntity te;

	public MobSummonContainer(EntityPlayer player, MobSummonTileEntity te)
	{
		super();
		this.player = player;
		this.te = te;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	public EntityPlayer getPlayer()
	{
		return player;
	}

	public MobSummonTileEntity getTe()
	{
		return te;
	}

}
