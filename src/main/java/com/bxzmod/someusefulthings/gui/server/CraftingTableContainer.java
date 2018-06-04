package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerTweak;
import com.bxzmod.someusefulthings.SlotCraftingTweak;
import com.bxzmod.someusefulthings.gui.InventoryCraftingTweak;
import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.tileentity.CraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class CraftingTableContainer extends Container
{
	public InventoryCraftingTweak craftMatrix;
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	public IItemHandler output;

	private World worldObj;

	private CraftingTableTileEntity te;

	private boolean isTable = true;

	BlockPos pos;

	public CraftingTableContainer(EntityPlayer player, World world, BlockPos pos)
	{
		this.worldObj = world;
		this.pos = pos;
		TileEntity t = world.getTileEntity(pos);
		if (t != null && t instanceof CraftingTableTileEntity)
			this.te = (CraftingTableTileEntity) t;
		else
			isTable = false;
		if (isTable)
		{
			this.craftMatrix = new InventoryCraftingTweak(this, this.te.getSideInventory().getStacksInput());
			this.output = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		} else
		{
			if (player.getHeldItemMainhand() != null
					&& player.getHeldItemMainhand().getItem().equals(ItemLoader.portableCrafter))
				this.craftMatrix = new InventoryCraftingTweak(this,
						((ItemStackHandlerTweak) player.getHeldItemMainhand()
								.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getStacks());
		}
		if (craftMatrix == null)
			throw new RuntimeException();

		if (isTable)
		{
			this.addSlotToContainer(new SlotCraftingTweak(player, this.craftMatrix, this.craftResult, 0, 102, 35));

			for (int i = 0; i < 3; ++i)
			{
				for (int j = 0; j < 3; ++j)
				{
					this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 8 + j * 18, 17 + i * 18)
					{

						@Override
						public boolean isItemValid(ItemStack stack)
						{
							boolean flag = CraftingTableContainer.this.isItemValidInCrafting(stack,
									this.getSlotIndex());
							return flag;
						}

					});
				}
			}
		} else
		{
			this.addSlotToContainer(new SlotCraftingTweak(player, this.craftMatrix, this.craftResult, 0, 124, 35));

			for (int i = 0; i < 3; ++i)
			{
				for (int j = 0; j < 3; ++j)
				{
					this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
				}
			}
		}

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			if (!isTable && player.inventory.currentItem == l)
			{
				this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142)
				{

					@Override
					public boolean canTakeStack(EntityPlayer playerIn)
					{
						return false;
					}

				});
			} else
				this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
		}

		if (isTable)
			for (int i = 0; i < 2; ++i)
				for (int j = 0; j < 2; ++j)
					this.addSlotToContainer(new SlotItemHandler(output, i * 2 + j, 134 + j * 18, 35 + i * 18));

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	private boolean isItemValidInCrafting(ItemStack stack, int index)
	{
		if (!this.te.isLockRecipe())
			return true;
		else
		{
			if (this.te.getLockedRecipeCache() != null)
			{
				ItemStackHandlerTweak cache = this.te.getLockedRecipeCache();
				boolean flag = cache.getStackInSlot(index) != null
						&& Helper.isSameStackIgnoreAmount(cache.getStackInSlot(index), stack);
				return flag;
			}
		}
		return false;
	}

	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		this.craftResult.setInventorySlotContents(0,
				CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
		super.onCraftMatrixChanged(inventoryIn);
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		syncData();
	}

	public void syncData()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient() && isTable)
		{
			MultiSignSync message = new MultiSignSync();
			message.nbt = new NBTTagCompound();
			message.nbt.setInteger("world", worldObj.provider.getDimension());
			message.nbt.setString("BlockPos", pos.toString());
			message.nbt.setString("Type", "Crafter");
			message.nbt.setBoolean("Button", true);
			message.nbt.setBoolean("Auto", isAuto());
			message.nbt.setBoolean("Output", te.isStayInSlot());
			message.nbt.setBoolean("Lock", te.isLockRecipe());
			NetworkLoader.instance.sendToServer(message);
		}
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37)
			{
				if (!this.mergeItemStack(itemstack1, 1, 10, false) && !this.mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			} else if (index >= 37 && index < 46)
			{
				if (!this.mergeItemStack(itemstack1, 1, 10, false) && !this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}

	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public boolean isTable()
	{
		return isTable;
	}

	public CraftingTableTileEntity getTe()
	{
		return te;
	}

	public boolean isAuto()
	{
		return te.isAuto();
	}

	public void setAuto(boolean isAuto)
	{
		te.setAuto(isAuto);
	}

}
