package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerTweak;
import com.bxzmod.someusefulthings.SideInventory;
import com.bxzmod.someusefulthings.SidedInvWrapperTweak;
import com.bxzmod.someusefulthings.fakeplayer.FakePlayerLoader;
import com.bxzmod.someusefulthings.gui.InventoryCraftingTweak;
import com.bxzmod.someusefulthings.gui.server.CraftingTableContainer;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class CraftingTableTileEntity extends TileEntity implements ITickable
{
	protected SideInventoryMe iInventory_crafting = new SideInventoryMe(9, 4);

	private ItemStackHandlerTweak lockedRecipeCache = new ItemStackHandlerTweak(9)
	{

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			return stack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return null;
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack)
		{
			return 1;
		}

	};

	protected boolean isAuto = false, needUpdate = false, stayInSlot = false, lockRecipe = false, firstLoad = true;

	EntityPlayer fakePlayer = null;

	InventoryCraftingTweak toCraft, cacheCraft;

	IRecipe tempRecipe;

	public ItemStack cache_stack;

	private final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

	public CraftingTableTileEntity()
	{

	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if (needUpdate)
		{
			this.markDirty();
			needUpdate = false;
		}
		if (fakePlayer == null)
			fakePlayer = FakePlayerLoader.getFakePlayer(FMLCommonHandler.instance().getMinecraftServerInstance()
					.worldServerForDimension(world.provider.getDimension()));
		if (toCraft == null)
			toCraft = new InventoryCraftingTweak(new CraftingTableContainer(fakePlayer, world, pos),
					iInventory_crafting.getStacksInput());
		if (lockRecipe)
		{
			if (firstLoad)
			{
				cacheCraft = new InventoryCraftingTweak(new CraftingTableContainer(fakePlayer, world, pos),
						lockedRecipeCache.getStacks());
				tempRecipe = this.findMatchingRecipe(cacheCraft, world);
				cache_stack = tempRecipe.getCraftingResult(cacheCraft);
				firstLoad = false;
			}
			if (cacheCraft == null)
				cacheCraft = new InventoryCraftingTweak(new CraftingTableContainer(fakePlayer, world, pos),
						lockedRecipeCache.getStacks());
			if (tempRecipe == null || !tempRecipe.matches(cacheCraft, world))
			{
				lockRecipe = false;
				return;
			}
			cache_stack = tempRecipe.getCraftingResult(cacheCraft);
			if (!tempRecipe.matches(toCraft, world))
				return;
		}
		if (isAuto)
		{
			if (tempRecipe == null || !tempRecipe.matches(toCraft, world))
				tempRecipe = this.findMatchingRecipe(toCraft, world);
			if (tempRecipe == null)
				return;
			ItemStack result = tempRecipe.getCraftingResult(toCraft);
			net.minecraftforge.common.ForgeHooks.setCraftingPlayer(fakePlayer);
			ItemStack[] remainItems = tempRecipe.getRemainingItems(toCraft);
			net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
			if (result != null)
			{
				ItemStack cache = Helper.copyStack(result);
				if (this.canMergeOutput(cache, remainItems, this.iInventory_crafting.getStacksOutput()))
				{
					this.mergeItemStack(Helper.copyStack(result), this.iInventory_crafting.getStacksOutput(), false);
					doCraft(result);
					this.markDirty();
				}
			}
		}
		this.tryOutPut();
	}

	private void tryOutPut()
	{
		for (EnumFacing face : EnumFacing.VALUES)
		{
			if (face == EnumFacing.UP)
				continue;
			BlockPos pos = this.pos.offset(face);
			TileEntity te = this.world.getTileEntity(pos);
			if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite()))
			{
				IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
				for (int i = 0; i < 4; i++)
				{
					if (this.iInventory_crafting.getStacksOutput()[i] != null)
					{
						Helper.mergeItemStack(this.iInventory_crafting.getStacksOutput()[i], 0, inv.getSlots(), false,
								inv);
						if (this.iInventory_crafting.getStacksOutput()[i].stackSize == 0)
							this.iInventory_crafting.removeStackFromSlot(i + 9);
						this.markDirty();
					}

				}
			}
		}
	}

	private boolean canMergeOutput(ItemStack result, ItemStack[] remainItems, ItemStack[] OutputSlots)
	{
		if (!this.mergeItemStack(result, OutputSlots, true))
			return false;
		if (stayInSlot)
		{
			for (int i = 0; i < remainItems.length; i++)
			{
				ItemStack itemstack = toCraft.getStackInSlot(i);
				ItemStack itemstack1 = remainItems[i];

				if (itemstack1 == null || itemstack == null || itemstack.stackSize <= 1)
					continue;

				if (itemstack1 != null)
					if (!ItemStack.areItemsEqual(itemstack, itemstack1)
							|| !ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
						return false;
			}
		} else
		{
			for (ItemStack s : remainItems)
				if (s != null)
					if (!this.mergeItemStack(s, OutputSlots, true))
						return false;
		}
		return true;
	}

	private IRecipe findMatchingRecipe(InventoryCraftingTweak craftMatrix, World worldIn)
	{
		for (IRecipe irecipe : this.recipes)
		{
			if (irecipe.matches(craftMatrix, worldIn))
			{
				return irecipe;
			}
		}
		return null;
	}

	public boolean mergeItemStack(ItemStack stackIn, ItemStack[] array, boolean simulate)
	{
		ItemStack[] stacks;
		boolean flag = false;
		if (simulate)
		{
			stacks = new ItemStack[array.length];
			for (int i = 0; i < array.length; i++)
				if (array[i] == null)
					stacks[i] = null;
				else
					stacks[i] = Helper.copyStack(array[i]);
		} else
		{
			stacks = array;
		}
		for (int i = 0; i < stacks.length; i++)
		{
			if (stackIn.stackSize == 0)
				break;
			ItemStack s = stacks[i];
			if (s != null && Helper.isSameStackIgnoreAmount(s, stackIn))
			{
				int j = s.stackSize + stackIn.stackSize;
				int maxSize = Math.min(64, stackIn.getMaxStackSize());

				if (j <= maxSize)
				{
					stackIn.stackSize = 0;
					s.stackSize = j;
					flag = true;
				} else if (s.stackSize < maxSize)
				{
					stackIn.stackSize -= maxSize - s.stackSize;
					s.stackSize = maxSize;
					flag = true;
				}
			} else if (s == null)
			{
				stacks[i] = Helper.copyStack(stackIn);
				stackIn.stackSize = 0;
				flag = true;
			}
		}
		if (stackIn.stackSize > 0)
			return false;
		return flag;
	}

	private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
	{
		return stackB.getItem() == stackA.getItem()
				&& (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata())
				&& ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	private void doCraft(ItemStack stack)
	{
		if (world.isRemote)
			return;
		net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(fakePlayer, stack, toCraft);
		stack.onCrafting(world, fakePlayer, 1);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(fakePlayer);
		ItemStack[] aitemstack = CraftingManager.getInstance().getRemainingItems(toCraft, fakePlayer.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		for (int i = 0; i < aitemstack.length; ++i)
		{
			ItemStack itemstack = toCraft.getStackInSlot(i);
			ItemStack itemstack1 = aitemstack[i];

			if (itemstack != null)
			{
				toCraft.decrStackSize(i, 1);
				itemstack = toCraft.getStackInSlot(i);
			}

			if (itemstack1 != null)
			{
				if (stayInSlot)
				{
					if (itemstack == null)
					{
						toCraft.setInventorySlotContents(i, itemstack1);
					} else if (ItemStack.areItemsEqual(itemstack, itemstack1)
							&& ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
					{
						itemstack1.stackSize += itemstack.stackSize;
						toCraft.setInventorySlotContents(i, itemstack1);
					} else
					{
						this.mergeItemStack(Helper.copyStack(itemstack1), this.iInventory_crafting.getStacksOutput(),
								false);
					}
				} else
				{
					this.mergeItemStack(Helper.copyStack(itemstack1), this.iInventory_crafting.getStacksOutput(),
							false);
				}
			}
		}
	}

	public SideInventory getSideInventory()
	{
		return iInventory_crafting;
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
			return (T) new SidedInvWrapperTweak(this.iInventory_crafting, facing);
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.setDataFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		return this.setNBTFromData(super.writeToNBT(compound));
	}

	public void setDataFromNBT(NBTTagCompound compound)
	{
		this.iInventory_crafting.deserializeNBT(compound.getCompoundTag("iInventory_crafting"));
		this.lockedRecipeCache.deserializeNBT(compound.getCompoundTag("Cache_Recipe"));
		this.iInventory_crafting.setCache(lockedRecipeCache);
		this.isAuto = compound.getBoolean("Auto");
		this.stayInSlot = compound.getBoolean("Output_Type");
		this.lockRecipe = compound.getBoolean("Lock");
	}

	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		compound.setTag("iInventory_crafting", this.iInventory_crafting.serializeNBT());
		compound.setTag("Cache_Recipe", this.lockedRecipeCache.serializeNBT());
		compound.setBoolean("Auto", isAuto);
		compound.setBoolean("Output_Type", stayInSlot);
		compound.setBoolean("Lock", lockRecipe);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), this.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			net.sendPacket(pkt);
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			BlockPos pos = pkt.getPos();
			if (pos == this.pos)
				this.readFromNBT(pkt.getNbtCompound());
		}
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			MultiSignSync message = new MultiSignSync();
			message.nbt = new NBTTagCompound();
			message.nbt.setInteger("world", world.provider.getDimension());
			message.nbt.setString("BlockPos", pos.toString());
			message.nbt.setString("Type", "Crafter");
			message.nbt.setString("Player", Minecraft.getMinecraft().player.getName());
			message.nbt.setBoolean("Need_Update", true);
			NetworkLoader.instance.sendToServer(message);
		}
	}

	public boolean isAuto()
	{
		return isAuto;
	}

	public void setAuto(boolean isAuto)
	{
		this.isAuto = isAuto;
		needUpdate = true;
	}

	public boolean isStayInSlot()
	{
		return stayInSlot;
	}

	public void setStayInSlot(boolean stayInSlot)
	{
		this.stayInSlot = stayInSlot;
	}

	public boolean isLockRecipe()
	{
		return lockRecipe;
	}

	public void setLockRecipe(boolean lockRecipe)
	{
		this.lockRecipe = lockRecipe;
		if (!this.world.isRemote)
			this.syncData();
	}

	public ItemStackHandlerTweak getLockedRecipeCache()
	{
		return lockedRecipeCache;
	}

	public void LockRecipe()
	{
		tempRecipe = this.findMatchingRecipe(toCraft, world);
		if (tempRecipe != null)
		{
			this.lockRecipe = true;
			this.lockedRecipeCache.setStacks(this.copyItemStackArray(this.iInventory_crafting.getStacksInput()));
			this.iInventory_crafting.setCache(lockedRecipeCache);
			this.cacheCraft = new InventoryCraftingTweak(new CraftingTableContainer(fakePlayer, world, pos),
					lockedRecipeCache.getStacks());
			cache_stack = tempRecipe.getCraftingResult(cacheCraft);
		}
		syncData();
		this.markDirty();
	}

	@SideOnly(Side.CLIENT)
	public void clinetSetCache()
	{
		toCraft = new InventoryCraftingTweak(new CraftingTableContainer(Minecraft.getMinecraft().player, world, pos),
				iInventory_crafting.getStacksInput());
		tempRecipe = this.findMatchingRecipe(toCraft, world);
		if (tempRecipe != null)
		{
			this.lockRecipe = true;
			this.lockedRecipeCache.setStacks(this.copyItemStackArray(this.iInventory_crafting.getStacksInput()));
			this.iInventory_crafting.setCache(lockedRecipeCache);
			cache_stack = tempRecipe.getCraftingResult(cacheCraft);
		}
	}

	public ItemStack[] copyItemStackArray(ItemStack[] in)
	{
		ItemStack[] copied = new ItemStack[in.length];
		for (int i = 0; i < this.iInventory_crafting.getStacksInput().length; i++)
			if (in[i] != null)
			{
				copied[i] = Helper.copyStack(in[i]);
				copied[i].stackSize = 1;
			} else
				copied[i] = null;
		return copied;
	}

	public void syncData()
	{
		this.syncData(null);
	}

	public void syncData(EntityPlayerMP player)
	{
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		message.nbt.setInteger("world", world.provider.getDimension());
		message.nbt.setString("BlockPos", pos.toString());
		message.nbt.setString("Type", "Crafter");
		message.nbt.setTag("Full_Data", this.writeToNBT(new NBTTagCompound()));
		if (isLockRecipe())
			message.nbt.setTag("cache_stack", cache_stack.serializeNBT());
		if (player == null)
			NetworkLoader.instance.sendToAllAround(message,
					new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
		else
			NetworkLoader.instance.sendTo(message, player);
		;
	}

	public class SideInventoryMe extends SideInventory
	{
		ItemStackHandlerTweak cache;

		public SideInventoryMe(int input, int output)
		{
			super(input, output);
		}

		public void setCache(ItemStackHandlerTweak i)
		{
			this.cache = i;
		}

		@Override
		public void markDirty()
		{
			needUpdate = true;

		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			try
			{
				if (lockRecipe && index < input)
					return cache.getStackInSlot(index) != null
							&& Helper.isSameStackIgnoreAmount(stack, cache.getStackInSlot(index));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return super.isItemValidForSlot(index, stack);
		}
	}
}
