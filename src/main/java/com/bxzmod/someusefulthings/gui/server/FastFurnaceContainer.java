package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.FastFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class FastFurnaceContainer extends AbstractContainer
{
	FastFurnaceTileEntity te;

	public FastFurnaceContainer(EntityPlayer player, FastFurnaceTileEntity te)
	{
		super(player, te, 8, 51);
		this.addSlotToContainer(new SlotItemHandlerHelper(this.items, 0, 44, 20));
		this.addSlotToContainer(new SlotItemHandlerHelper(this.items, 1, 116, 20));
	}

}
