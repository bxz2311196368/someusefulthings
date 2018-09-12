package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.DefaultSide;
import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.fakeplayer.FakePlayerLoader;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class ReplacementMachineTileEntity extends TileEntityBase
{

	private ArrayList<BlockPos> posList = Lists.<BlockPos>newArrayList();

	private FakePlayer fakePlayer = null;

	private MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

	public ReplacementMachineTileEntity()
	{
		super(new ItemStackHandlerModify(1, 9).setSlotChecker(0, stack -> stack.getItem() instanceof ItemBlock)
			.getInventory(), new DefaultSide());
	}

	public void addToList(EntityPlayer player, BlockPos p)
	{
		if (p.equals(pos))
		{
			player.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_6", TextFormatting.RED));
			return;
		}
		if (posList.size() < 64)
		{
			for (BlockPos p1 : posList)
				if (p.equals(p1))
				{
					player.sendStatusMessage(
						new TextComponentTranslation("replacementMachine.msg_9", TextFormatting.RED));
					return;
				}
			posList.add(p);
			player.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_8", TextFormatting.GREEN));
		} else
			player.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_0", TextFormatting.RED));

	}

	public void clearData()
	{
		posList.clear();
	}

	public boolean removePos(BlockPos pos)
	{
		if (posList.size() > 0)
			for (int i = 0; i < posList.size(); i++)
				if (posList.get(i).equals(pos))
				{
					posList.remove(i);
					return true;
				}
		return false;
	}

	public int getDim()
	{
		return world.provider.getDimension();
	}

	@Override
	public void work()
	{
		if (fakePlayer == null)
			fakePlayer = FakePlayerLoader.getFakePlayer(server.worldServerForDimension(world.provider.getDimension()));
		if (posList.isEmpty())
			return;
		for (BlockPos p : posList)
		{
			if (world.isBlockLoaded(p) && iInventory.getStackInSlot(0) != null
				&& iInventory.getStackInSlot(0).stackSize > 0)
			{
				if (world.getBlockState(getPos().down()).equals(world.getBlockState(p)))
				{
					IBlockState blockstate = world.getBlockState(p);
					Block block = blockstate.getBlock();
					List<ItemStack> drops = Lists.newArrayList();
					if (block.canSilkHarvest(world, p, blockstate, fakePlayer))
						drops.add(Helper.getSilkTouchDrop(blockstate, block));
					else
						drops.addAll(block.getDrops(world, p, blockstate, 10));
					if (!drops.isEmpty())
					{
						boolean canReplace = true;
						for (ItemStack stack : drops)
						{
							if (stack != null && stack.stackSize > 0)
							{
								for (int i = iInventory.getAllInputs().size(); i < iInventory.getSlots(); i++)
									if (iInventory.addStackInSlot(i, stack, true) != null)
									{
										canReplace = false;
										break;
									}
							}
						}
						if (canReplace)
						{
							world.setBlockState(p, Block.getBlockFromItem(iInventory.getStackInSlot(0).getItem())
								.getStateFromMeta(iInventory.getStackInSlot(0).getMetadata()));
							iInventory.decrStackSize(0, 1);
							for (ItemStack stack : drops)
								for (int i = iInventory.getAllInputs().size(); i < iInventory.getSlots(); i++)
								{
									ItemStack temp = iInventory.addStackInSlot(i, stack, false);
									if (temp == null)
										break;
									else
										stack.splitStack(temp.stackSize);
								}
						} else
							break;
					}

				}
			}
		}
		this.markDirty();
	}

}
