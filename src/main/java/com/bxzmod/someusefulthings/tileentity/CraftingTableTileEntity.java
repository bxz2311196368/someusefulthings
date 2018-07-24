package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.fakeplayer.FakePlayerLoader;
import com.bxzmod.someusefulthings.gui.InventoryCraftingTweak;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.List;

public class CraftingTableTileEntity extends TileEntityBase
{
	private static List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
	private InventoryCraftingTweak craft;
	private EntityPlayer fakePlayer;
	private CacheRecipe cacheCraft = new CacheRecipe();
	private boolean stayInSlot = false, auto = false;

	public CraftingTableTileEntity()
	{
		super(new ItemStackHandlerModify(9, 9), new DefaultSide());
	}

	@Override
	public void work()
	{
		if (this.auto && this.cacheCraft.hasRecipe && this.doCraft(true))
			this.doCraft(false);
	}

	@Override
	public void setDataFromNBT(NBTTagCompound compound)
	{
		super.setDataFromNBT(compound);
		this.stayInSlot = compound.getBoolean("stayInSlot");
		this.auto = compound.getBoolean("auto");
		this.cacheCraft.readFromNBT(compound.getCompoundTag("RecipeStored"));
		this.init();
	}

	@Override
	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		compound.setBoolean("stayInSlot", this.stayInSlot);
		compound.setBoolean("auto", this.auto);
		compound.setTag("RecipeStored", this.cacheCraft.writeToNBT(new NBTTagCompound()));
		return super.setNBTFromData(compound);
	}

	private void init()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			this.initServer();
		else
			this.initClient();
	}

	private void initServer()
	{
		if (this.fakePlayer != null)
			this.fakePlayer = FakePlayerLoader.getFakePlayer((WorldServer) this.world);
		this.craft = new InventoryCraftingTweak(this.cacheCraft.cache);
		this.setRecipe();
	}

	private void initClient()
	{

	}

	private IRecipe findRecipe()
	{
		for (IRecipe iRecipe : recipes)
			if (iRecipe.matches(this.craft, world))
				return iRecipe;
		return null;
	}

	public void setRecipe()
	{
		IRecipe recipe = this.findRecipe();
		if (recipe == null)
		{
			this.cacheCraft.hasRecipe = false;
			return;
		}
		this.cacheCraft.hasRecipe = true;
		for (int i = 0; i < 9; i++)
			this.cacheCraft.cache[i] = Helper.copyStack(this.iInventory.getStackInSlot(i), 1);
		ForgeHooks.setCraftingPlayer(this.fakePlayer);
		this.cacheCraft.remainItem.clear();
		try
		{
			for (ItemStack stack : recipe.getRemainingItems(this.craft))
				if (stack != null)
					this.cacheCraft.remainItem.add(stack);
		} catch (Exception e)
		{
			this.cacheCraft.remainItem.clear();
			this.cacheCraft.hasRecipe = false;
			this.cacheCraft.cache = new ItemStack[9];
			return;
		}
		this.cacheCraft.craftResult = recipe.getCraftingResult(this.craft);
		ForgeHooks.setCraftingPlayer(null);
	}

	public boolean doCraft(boolean simulate)
	{
		if (!this.extractCraftItem(simulate))
			return false;
		for (ItemStack stack : this.cacheCraft.remainItem)
			if (!this.mergeItem(Helper.copyStack(stack), simulate, this.stayInSlot ? 0 : 9, this.stayInSlot ? 9 : 18))
				return false;
		return this.mergeItem(Helper.copyStack(this.cacheCraft.craftResult), simulate, 9, 18);
	}

	private boolean extractCraftItem(boolean simulate)
	{
		for (int i = 0; i < 9; i++)
			if (this.cacheCraft.cache[i] != null)
				if (!this.findAndExtract(this.cacheCraft.cache[i], simulate))
					return false;
		return true;
	}

	private boolean findAndExtract(ItemStack stack, boolean simulate)
	{
		for (int i = 0; i < 9; i++)
			if (Helper.isSameStackIgnoreAmount(stack, this.iInventory.getStackInSlot(i)))
			{
				if (!simulate)
					this.iInventory.removeStackInSlot(i, 1, false);
				return true;
			}
		return false;
	}

	private boolean mergeItem(ItemStack stack, boolean simulate, int start, int end)
	{
		try
		{
			for (int i = start; i < end && stack.stackSize > 0; i++)
				stack = this.iInventory.addStackInSlot(i, stack, simulate);
			return stack.stackSize <= 0;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean canTakeCraftResult()
	{
		return this.extractCraftItem(true);
	}

	public boolean isStayInSlot()
	{
		return stayInSlot;
	}

	public void setStayInSlot(boolean stayInSlot)
	{
		this.stayInSlot = stayInSlot;
	}

	public boolean isAuto()
	{
		return auto;
	}

	public void setAuto(boolean auto)
	{
		this.auto = auto;
	}

	public ItemStack[] getRecipeStacks()
	{
		return this.cacheCraft.cache;
	}

	private final class CacheRecipe
	{
		ItemStack[] cache = new ItemStack[9];
		ItemStack craftResult = null;
		List<ItemStack> remainItem = Lists.newArrayList();
		boolean hasRecipe = false;

		NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			if (this.hasRecipe)
			{
				nbt.setTag("recipe", Helper.writeStacksToNBT(this.cache));
				if (!this.remainItem.isEmpty())
					nbt.setTag("remainItem",
						Helper.writeStacksToNBT(this.remainItem.toArray(new ItemStack[this.remainItem.size()])));
				nbt.setTag("craftResult", this.craftResult.serializeNBT());
			}
			return nbt;
		}

		void readFromNBT(NBTTagCompound nbt)
		{
			if (nbt.hasKey("recipe"))
			{
				this.hasRecipe = true;
				this.cache = Helper.loadStacksFromNBT(9, (NBTTagList) nbt.getTag("recipe"));
				this.remainItem.clear();
				if (nbt.hasKey("remainItem"))
				{
					NBTTagList list = (NBTTagList) nbt.getTag("remainItem");
					this.remainItem.addAll(Arrays.asList(Helper.loadStacksFromNBT(list.tagCount(), list)));
				}
				this.craftResult = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("craftResult"));
			}
		}

	}

}
