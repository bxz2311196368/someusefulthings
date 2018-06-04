package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class FastFurnaceTileEntity extends TileEntity implements ITickable
{
	private ItemStackHandlerModify iInventory = new ItemStackHandlerModify(1, 1)
			.setSlotChecker(0, stack -> FurnaceRecipes.instance().getSmeltingResult(stack) != null
					|| (stack.getItem().equals(Items.IRON_INGOT) && OreDictionary.doesOreNameExist("ingotSteel")))
			.getInventory();

	@Override
	public void update()
	{
		if (this.world.isRemote)
			return;
		ItemStack input = this.iInventory.getStackInSlot(0), output = this.iInventory.getStackInSlot(1);
		if (input != null && input.stackSize > 0)
		{
			ItemStack smeltItem;
			if (input.getItem().equals(Items.IRON_INGOT) && OreDictionary.doesOreNameExist("ingotSteel"))
			{
				smeltItem = OreDictionary.getOres("ingotSteel").get(0);
			} else
			{
				smeltItem = FurnaceRecipes.instance().getSmeltingResult(input);
			}
			if (smeltItem != null)
			{
				if (output != null)
				{
					if (Helper.isSameStackIgnoreAmount(output, smeltItem))
					{
						int temp = Math.min(input.stackSize,
								(output.getMaxStackSize() - output.stackSize) / smeltItem.stackSize);
						input.stackSize -= temp;
						output.stackSize += temp * smeltItem.stackSize;
					}
				} else
				{
					int temp = Math.min(input.stackSize, smeltItem.getMaxStackSize() / smeltItem.stackSize);
					input.stackSize -= temp;
					iInventory.setStackInSlot(1, Helper.copyStack(smeltItem, temp * smeltItem.stackSize));
				}
				if (input != null && input.stackSize <= 0)
					this.iInventory.removeStackFromSlot(0);
			}
		}
		this.markDirty();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
		{
			T result = (T) this.iInventory;
			return result;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			this.iInventory.deserializeNBT(compound.getCompoundTag("iInventory"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		return compound;
	}

	public ItemStackHandlerModify getiInventory()
	{
		return iInventory;
	}
}
