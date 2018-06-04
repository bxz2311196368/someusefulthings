package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.gui.server.ContainerWorkbenchTweak;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCraftingTweak extends GuiCrafting
{

	public GuiCraftingTweak(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition)
	{
		super(playerInv, worldIn, blockPosition);
		this.inventorySlots = new ContainerWorkbenchTweak(playerInv, worldIn, blockPosition);
	}

}
