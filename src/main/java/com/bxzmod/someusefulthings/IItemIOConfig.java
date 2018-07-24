package com.bxzmod.someusefulthings;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public interface IItemIOConfig<T extends ItemStackHandlerModify> extends IInventory
{
	public void setSlotIO(int slot, boolean in, boolean out);

	public IItemIOConfig resetAllItemIO();

	public boolean canInput(int slot);

	public boolean canOutput(int slot);

	public HashSet<Integer> getAllInputs();

	public IItemIOConfig setAllInputs(int[] slots);

	public HashSet<Integer> getAllOutputs();

	public IItemIOConfig setAllOutputs(int[] slots);

	public IItemIOConfig setSlotChecker(int slot, ILimitSlot checker);

	public IItemIOConfig setAllInputSlotChecker(ILimitSlot checker);

	public IItemIOConfig setAllOutputSlotChecker(ILimitSlot checker);

	public IItemIOConfig removeSlotChecker(int slot);

	public T getInventory();

	public void tryInAndOut(World world, BlockPos pos);

	public ItemStack addStackInSlot(int slot, ItemStack stack, boolean simulate);

	public ItemStack removeStackInSlot(int slot, int amount, boolean simulate);

	public IConfigSide setConfigSide(IConfigSide configSide);

	public IConfigSide getConfigSide();
}
