package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.ReinforcementMachineTileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class ReinforcementMachineContainer extends AbstractContainer
{

	public ReinforcementMachineContainer(EntityPlayer player, ReinforcementMachineTileEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 0, 26, 20));

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 1, 62, 20));

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 2, 134, 20));
	}

}