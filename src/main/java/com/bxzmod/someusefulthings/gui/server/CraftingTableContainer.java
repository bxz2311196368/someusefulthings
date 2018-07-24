package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.InventoryCraftResultTweak;
import com.bxzmod.someusefulthings.SlotCraftingTweak;
import com.bxzmod.someusefulthings.SlotUnpickable;
import com.bxzmod.someusefulthings.gui.InventoryCraftingTweak;
import com.bxzmod.someusefulthings.gui.SlotItemHandlerHelper;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.tileentity.CraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class CraftingTableContainer extends AbstractContainer
{
	public InventoryCraftingTweak craftMatrix;
	public InventoryCraftResult craftResult;
	public IItemHandler output;
	private World worldObj;
	private CraftingTableTileEntity craftingTableTileEntity;

	public CraftingTableContainer(EntityPlayer player, World world, CraftingTableTileEntity te)
	{
		super(player, te, 8, 124);
		this.worldObj = world;
		this.craftingTableTileEntity = te;
		this.craftResult = new InventoryCraftResultTweak(this);
		this.craftMatrix = new InventoryCraftingTweak(this, this.craftingTableTileEntity.getRecipeStacks());
		this.addSlotToContainer(new SlotCraftingTweak(player, this.craftMatrix, this.craftResult, 0, 124, 35, this));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				this.addSlotToContainer(new SlotUnpickable(this.craftMatrix, j + i * 3, 31 + j * 18, 17 + i * 18));
		this.onCraftMatrixChanged(this.craftMatrix);
		for (int i = 0; i < 2; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(
					new SlotItemHandlerHelper(this.craftingTableTileEntity.getiInventory(), j + i * 9, 8 + j * 18,
						84 + i * 18));
	}

	@Nullable
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		Slot slot = this.getSlot(slotId);
		if (slot instanceof SlotUnpickable)
		{
			slot.putStack(Helper.copyStack(player.inventory.getItemStack()));
			return null;
		}
		if (slot instanceof SlotCraftingTweak)
		{
			if (!this.craftingTableTileEntity.canTakeCraftResult())
				return null;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		this.craftResult.setInventorySlotContents(0,
			CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			this.getTe().setRecipe();
		}
		super.onCraftMatrixChanged(inventoryIn);
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
	}

	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public void syncData()
	{
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		message.nbt.setString("Type", "Crafter");
		message.nbt.setInteger("world", this.worldObj.provider.getDimension());
		message.nbt.setString("BlockPos", this.te.getPos().toString());
		message.nbt.setBoolean("Auto", this.craftingTableTileEntity.isAuto());
		message.nbt.setBoolean("Output", this.craftingTableTileEntity.isStayInSlot());
		NetworkLoader.instance.sendToServer(message);
	}

	public CraftingTableTileEntity getTe()
	{
		return this.craftingTableTileEntity;
	}

}
