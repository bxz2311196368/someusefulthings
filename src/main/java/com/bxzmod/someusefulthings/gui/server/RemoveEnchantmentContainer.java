package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.tileentity.RemoveEnchantmentTileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class RemoveEnchantmentContainer extends AbstractContainer
{

	public RemoveEnchantmentContainer(EntityPlayer player, RemoveEnchantmentTileEntity tileEntity)
	{
		super(player, tileEntity, 8, 51);

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 0, 17, 20));

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 1, 53, 20));

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 2, 107, 20));

		this.addSlotToContainer(new SlotItemHandlerHelper(items, 3, 143, 20));

	}

}
