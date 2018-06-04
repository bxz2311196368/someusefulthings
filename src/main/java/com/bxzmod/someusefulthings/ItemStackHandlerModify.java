package com.bxzmod.someusefulthings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerModify extends ItemStackHandler implements IItemIOConfig<ItemStackHandlerModify>
{
	private HashSet<Integer> item_in = Sets.newHashSet(), item_out = Sets.newHashSet();
	private HashSet<EnumFacing> side_in = Sets.newHashSet(), side_out = Sets.newHashSet();
	private HashMap<Integer, ILimitSlot> slotCheckerIn = Maps.newHashMap(), slotCheckerOut = Maps.newHashMap();

	public ItemStackHandlerModify()
	{
		super();
		this.resetAllItemIO().resetAllSide();
	}

	public ItemStackHandlerModify(int size)
	{
		super(size);
		this.resetAllItemIO().resetAllSide();
	}

	public ItemStackHandlerModify(int in, int out)
	{
		super(in + out);
		this.setAllInputs(this.generateArray(0, in)).setAllOutputs(this.generateArray(in, in + out)).resetAllSide();
	}

	public ItemStackHandlerModify(int size, int[] in, int[] out)
	{
		super(size);
		this.setAllInputs(in).setAllOutputs(out).resetAllSide();
	}

	public ItemStackHandlerModify(ItemStack[] stacks)
	{
		super(stacks);
		this.resetAllItemIO().resetAllSide();
	}

	public ItemStackHandlerModify(ItemStack[] stacks, int[] in, int[] out)
	{
		super(stacks);
		this.setAllInputs(in).setAllOutputs(out).resetAllSide();
	}

	public ItemStack[] getStacks()
	{
		return this.stacks;
	}

	public void setStacks(ItemStack[] stacks)
	{
		this.stacks = stacks;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setIntArray("input_side", Ints.toArray(this.item_in));
		nbt.setIntArray("output_side", Ints.toArray(this.item_out));
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		this.setAllInputs(nbt.getIntArray("input_side"));
		this.setAllOutputs(nbt.getIntArray("output_side"));
	}

	@Override
	public void setSlotIO(int slot, boolean in, boolean out)
	{
		if (slot >= this.getSlots())
			return;
		if (in)
			this.item_in.add(slot);
		else
			this.item_in.remove(slot);
		if (out)
			this.item_out.add(slot);
		else
			this.item_out.remove(slot);
	}

	@Override
	public IItemIOConfig resetAllItemIO()
	{
		this.item_in.addAll(Stream.iterate(0, slot -> slot + 1).limit(this.getSlots()).collect(Collectors.toSet()));
		this.item_out.addAll(this.item_in);
		return this;
	}

	@Override
	public boolean canInput(int slot)
	{
		return this.item_in.contains(slot);
	}

	@Override
	public boolean canOutput(int slot)
	{
		return this.item_out.contains(slot);
	}

	@Override
	public HashSet<Integer> getAllInputs()
	{
		return this.item_in;
	}

	@Override
	public IItemIOConfig setAllInputs(int[] slots)
	{
		this.item_in.clear();
		this.item_in.addAll(Ints.asList(slots));
		return this;
	}

	@Override
	public HashSet<Integer> getAllOutputs()
	{
		return this.item_out;
	}

	@Override
	public IItemIOConfig setAllOutputs(int[] slots)
	{
		this.item_out.clear();
		this.item_out.addAll(Ints.asList(slots));
		return this;
	}

	@Override
	public ItemStackHandlerModify getInventory()
	{
		return this;
	}

	@Override
	public void tryInAndOut(World world, BlockPos pos)
	{
		for (EnumFacing side : EnumFacing.VALUES)
		{
			TileEntity te = Helper.getAdjacentTileEntity(world, pos, side);
			if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()))
			{
				IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
				if (this.side_in.contains(side))
					for (int i : this.item_in)
						Helper.mergeInventory(inv, this, i);
				if (this.side_out.contains(side))
					for (int i : this.item_out)
						if (this.getStackInSlot(i) != null)
							this.setStackInSlot(i,
									Helper.mergeItemStack(this.getStackInSlot(i), 0, inv.getSlots(), false, inv));
			}
		}
	}

	@Override
	public HashSet<EnumFacing> getInputSides()
	{
		return this.side_in;
	}

	@Override
	public IItemIOConfig setInputSides(EnumFacing[] sides)
	{
		this.side_in.clear();
		this.side_in.addAll(Arrays.asList(sides));
		return this;
	}

	@Override
	public HashSet<EnumFacing> getOutputSides()
	{
		return this.side_out;
	}

	@Override
	public IItemIOConfig setOutputSides(EnumFacing[] sides)
	{
		this.side_out.clear();
		this.side_out.addAll(Arrays.asList(sides));
		return this;
	}

	@Override
	public IItemIOConfig resetAllSide()
	{
		this.setInputSides(new EnumFacing[] { EnumFacing.UP });
		this.setOutputSides(new EnumFacing[] { EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST,
				EnumFacing.WEST });
		return this;
	}

	@Override
	public IItemIOConfig setInputSide(EnumFacing side)
	{
		this.side_in.add(side);
		return this;
	}

	@Override
	public IItemIOConfig setOutputSide(EnumFacing side)
	{
		this.side_out.add(side);
		return this;
	}

	public IItemIOConfig removeInputSide(EnumFacing side)
	{
		this.side_in.remove(side);
		return this;
	}

	public IItemIOConfig removeOutputSide(EnumFacing side)
	{
		this.side_out.remove(side);
		return this;
	}

	@Override
	public IItemIOConfig setSlotChecker(int slot, ILimitSlot checker)
	{
		this.slotCheckerIn.put(slot, checker);
		return this;
	}

	@Override
	public IItemIOConfig setAllInputSlotChecker(ILimitSlot checker)
	{
		for (int i : this.item_in)
			this.slotCheckerIn.put(i, checker);
		return this;
	}

	@Override
	public IItemIOConfig setAllOutputSlotChecker(ILimitSlot checker)
	{
		for (int i : this.item_out)
			this.slotCheckerOut.put(i, checker);
		return this;
	}

	@Override
	public IItemIOConfig removeSlotChecker(int slot)
	{
		this.slotCheckerIn.remove(slot);
		return this;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (this.canInput(slot) && this.isItemValidForSlot(slot, stack))
			return super.insertItem(slot, stack, simulate);
		return stack;
	}

	@Override
	public ItemStack addStackInSlot(int slot, ItemStack stack, boolean simulate)
	{
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (this.canOutput(slot) && ((this.slotCheckerOut.containsKey(slot) && this.getStackInSlot(slot) != null)
				? this.slotCheckerOut.get(slot).test(this.getStackInSlot(slot))
				: true))
			return super.extractItem(slot, amount, simulate);
		return null;
	}

	@Override
	public ItemStack removeStackInSlot(int slot, int amount, boolean simulate)
	{
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSizeInventory()
	{
		return this.getSlots();
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (getStackInSlot(index) != null)
		{
			ItemStack newStack;
			if (getStackInSlot(index).stackSize <= count)
			{
				newStack = getStackInSlot(index);
				removeStackFromSlot(index);
			} else
			{
				newStack = getStackInSlot(index).splitStack(count);
				if (getStackInSlot(index).stackSize == 0)
				{
					removeStackFromSlot(index);
				}
			}
			return newStack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (getStackInSlot(index) != null)
		{
			ItemStack stack = getStackInSlot(index);
			setStackInSlot(index, null);
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.setStackInSlot(index, stack);
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return this.item_in.contains(index)
				? this.slotCheckerIn.containsKey(index) && stack != null ? this.slotCheckerIn.get(index).test(stack)
						: true
				: false;
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{

	}

	@Override
	public void closeInventory(EntityPlayer player)
	{

	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{

	}

	/**
	 * @param start
	 *            开始
	 * @param end
	 *            结束（不包括）
	 * @return 生成的整型数组
	 */
	private int[] generateArray(int start, int end)
	{
		int[] ints = new int[end - start];
		for (int i = 0; i < ints.length; i++)
			ints[i] = start + i;
		return ints;
	}

	private int[] sidesToArray(EnumFacing[] sides)
	{
		int[] array = new int[sides.length];
		for (int i = 0; i < sides.length; i++)
			array[i] = sides[i].getIndex();
		return array;
	}

}
