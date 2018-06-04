package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.SideInventory;
import com.bxzmod.someusefulthings.SidedInvWrapperTweak;
import com.bxzmod.someusefulthings.fakeplayer.FakePlayerLoader;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ReplacementMachineTileEntity extends TileEntity implements ITickable
{
	protected SideInventory iInventory = new SideInventory(1, 1)
	{
		@Override
		public void markDirty()
		{
			ReplacementMachineTileEntity.this.markDirty();
		}

		@Override
		public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
		{
			if (direction != EnumFacing.UP)
				return false;
			return true;
		}

		@Override
		public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
		{
			if (direction != EnumFacing.UP)
				return true;
			return false;
		}

		@Override
		public int[] getSlotsForFace(EnumFacing side)
		{
			return side == EnumFacing.UP ? this.generateArray(input, 0) : this.generateArray(output, input);
		}
	};

	private ArrayList<BlockPos> posList = Lists.<BlockPos>newArrayList();

	private FakePlayer fakePlayer = null;

	private MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

	public ReplacementMachineTileEntity()
	{

	}

	public SideInventory getInventory()
	{
		return iInventory;
	}

	public void addToList(EntityPlayer player, BlockPos p)
	{
		if (p.equals(pos))
		{
			player.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_6", TextFormatting.RED));
			return;
		}
		if (p != null && posList.size() < 64)
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
		} else if (posList.size() >= 64)
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
			T result = (T) new SidedInvWrapperTweak(iInventory, facing);
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
	public void update()
	{
		if (world.isRemote)
			return;
		if (fakePlayer == null)
			fakePlayer = FakePlayerLoader.getFakePlayer(server.worldServerForDimension(world.provider.getDimension()));
		IItemHandler inv = null;
		for (EnumFacing face : EnumFacing.HORIZONTALS)
			if (world.getTileEntity(pos.offset(face)) != null)
			{
				TileEntity te = world.getTileEntity(pos.offset(face));
				if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite()))
				{
					inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
					break;
				}
			}
		if (inv == null || posList.isEmpty())
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
						for (ItemStack stack : drops)
						{
							if (stack != null && stack.stackSize > 0)
							{
								ItemStack temp = Helper.mergeItemStack(stack, 0, inv.getSlots(), false, inv, true);
								if (temp == null)
								{
									world.setBlockState(p,
											Block.getBlockFromItem(iInventory.getStackInSlot(0).getItem())
													.getDefaultState());
									iInventory.decrStackSize(0, 1);
									Helper.mergeItemStack(stack, 0, inv.getSlots(), false, inv);
								}
							}
						}
					}

				}
			}
		}
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			this.iInventory.deserializeNBT(compound.getCompoundTag("iInventory"));
			NBTTagList list = (NBTTagList) compound.getTag("pos_list");
			if (list != null && !list.hasNoTags())
				for (int i = 0; i < list.tagCount(); i++)
					posList.add(Helper.getBlockPosFromstring(((NBTTagString) list.get(i)).getString()));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("iInventory", this.iInventory.serializeNBT());
		compound.setTag("pos_list", setNBTFromData());
		return compound;
	}

	public NBTTagList setNBTFromData()
	{
		NBTTagList list = new NBTTagList();
		for (BlockPos p : posList)
			list.appendTag(new NBTTagString(p.toString()));
		return list;
	}

}
