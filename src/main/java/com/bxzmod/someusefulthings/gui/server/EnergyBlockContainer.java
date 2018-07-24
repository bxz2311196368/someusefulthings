package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.EnergyBlockTileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class EnergyBlockContainer extends AbstractContainer
{
	private EnergyBlockTileEntity te;

	public EnergyBlockContainer(EntityPlayer player, EnergyBlockTileEntity tileEntity)
	{
		super(player, tileEntity, 8, 50);
		this.te = tileEntity;

		for (int i = 0; i < this.items.getSlots(); i++)
			this.addSlotToContainer(new SlotItemHandlerHelper(this.items, i, 8 + i * 18, 20));
	}

	public double getTotalEnergy()
	{
		return this.te.getTotalEnergy();
	}
}
