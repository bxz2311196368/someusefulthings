package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.CopyEnchantmentTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class CopyEnchantmentContainer extends AbstractContainer
{
	protected Slot enchbook;
	protected Slot Lapis;
	protected Slot book;
	protected Slot copy;

	public CopyEnchantmentContainer(EntityPlayer player, CopyEnchantmentTileEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.enchbook = this.addSlotToContainer(new SlotItemHandlerHelper(items, 0, 17, 20));

		this.Lapis = this.addSlotToContainer(new SlotItemHandlerHelper(items, 1, 53, 20));

		this.book = this.addSlotToContainer(new SlotItemHandlerHelper(items, 2, 89, 20));

		this.copy = this.addSlotToContainer(new SlotItemHandlerHelper(items, 3, 143, 20));
	}

}
