package com.bxzmod.someusefulthings;

import com.bxzmod.someusefulthings.gui.server.CraftingTableContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotCraftingTweak extends SlotCrafting
{
	public InventoryCrafting craftMatrix;
	public CraftingTableContainer gui;
	public World world;

	public SlotCraftingTweak(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,
		int slotIndex, int xPosition, int yPosition, CraftingTableContainer gui)
	{
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.craftMatrix = craftingInventory;
		this.world = player.world;
		this.gui = gui;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			this.gui.getTe().doCraft(false);
		}
	}
}
