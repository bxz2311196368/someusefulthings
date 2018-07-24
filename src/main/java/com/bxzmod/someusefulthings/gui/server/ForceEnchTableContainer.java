package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.ForceEnchTableTiltEntity;
import net.minecraft.entity.player.EntityPlayer;

public class ForceEnchTableContainer extends AbstractContainer
{
	protected SlotItemHandlerHelper itemToEnch;
	protected SlotItemHandlerHelper enchBook;
	protected SlotItemHandlerHelper itemEnched;

	public ForceEnchTableContainer(EntityPlayer player, ForceEnchTableTiltEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.addSlotToContainer(this.itemToEnch = new SlotItemHandlerHelper(items, 0, 26, 20));

		this.addSlotToContainer(this.enchBook = new SlotItemHandlerHelper(items, 1, 62, 20));

		this.addSlotToContainer(this.itemEnched = new SlotItemHandlerHelper(items, 2, 134, 20));

	}

}
