package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ForceEnchTableContainer extends AbstractContainer
{
	protected SlotItemHandlerHelper itemToEnch;
	protected SlotItemHandlerHelper enchBook;
	protected SlotItemHandlerHelper itemEnched;

	public ForceEnchTableContainer(EntityPlayer player, TileEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.addSlotToContainer(this.itemToEnch = new SlotItemHandlerHelper(items, 0, 26, 20));

		this.addSlotToContainer(this.enchBook = new SlotItemHandlerHelper(items, 1, 62, 20));

		this.addSlotToContainer(this.itemEnched = new SlotItemHandlerHelper(items, 2, 134, 20));

	}

}
