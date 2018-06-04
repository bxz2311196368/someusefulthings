package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.tileentity.XPReservoirTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class XPReservoirContainer extends Container
{
	private EntityPlayer player;

	private XPReservoirTileEntity te;

	private int xp;

	public XPReservoirContainer(EntityPlayer player, TileEntity te)
	{
		super();
		this.player = player;
		if (te instanceof XPReservoirTileEntity)
			this.te = (XPReservoirTileEntity) te;
		else
			throw new RuntimeException("error xp tile_entity!");
		this.xp = this.te.getXp();
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		this.xp = this.te.getXp();

		for (IContainerListener i : this.listeners)
		{
			i.sendProgressBarUpdate(this, GuiLoader.DATA_X_P, this.xp);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		switch (id)
		{
		case GuiLoader.DATA_X_P:
			this.xp = data;
			break;
		default:
		}
	}

	public int getXp()
	{
		return xp;
	}

	public XPReservoirTileEntity getTe()
	{
		return te;
	}

}
