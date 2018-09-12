package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class MobSummonContainer extends AbstractContainer
{
	private EntityPlayer player;
	private MobSummonTileEntity te;

	public MobSummonContainer(EntityPlayer player, MobSummonTileEntity te)
	{
		super(te);
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

	public MobSummonTileEntity getBasicTe()
	{
		return te;
	}

}
