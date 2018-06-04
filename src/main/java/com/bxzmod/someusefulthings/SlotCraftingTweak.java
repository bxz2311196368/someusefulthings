package com.bxzmod.someusefulthings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class SlotCraftingTweak extends SlotCrafting
{
	public InventoryCrafting craftMatrix;

	public World world;

	public SlotCraftingTweak(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,
			int slotIndex, int xPosition, int yPosition)
	{
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.craftMatrix = craftingInventory;
		this.world = player.world;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
	{
		if (Helper.isSameStackIgnoreAmount(
				CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.world), stack))
			super.onPickupFromSlot(playerIn, stack);
		else
			this.onSlotChanged();
	}

}
