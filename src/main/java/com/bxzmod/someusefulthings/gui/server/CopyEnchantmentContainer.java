package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class CopyEnchantmentContainer extends AbstractContainer
{
	protected Slot enchbook;
	protected Slot Lapis;
	protected Slot book;
	protected Slot copy;

	public CopyEnchantmentContainer(EntityPlayer player, TileEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.addSlotToContainer(this.enchbook = new SlotItemHandlerHelper(items, 0, 17, 20));

		this.addSlotToContainer(this.Lapis = new SlotItemHandlerHelper(items, 1, 53, 20));

		this.addSlotToContainer(this.book = new SlotItemHandlerHelper(items, 2, 89, 20));

		this.addSlotToContainer(this.copy = new SlotItemHandlerHelper(items, 3, 143, 20));
	}

}
