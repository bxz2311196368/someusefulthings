package com.bxzmod.someusefulthings.tileentity;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.TicketManager;
import com.bxzmod.someusefulthings.TicketManager.BlockPosWithDim;
import com.bxzmod.someusefulthings.TicketManager.ChunkInfo;
import com.bxzmod.someusefulthings.blocks.BlockLoader;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;

public class LavaPumpTileEntity extends TileFluidHandler implements ITickable
{
	public static final int MAXRANGE = 16;

	private String player;

	private BlockPos posToDrain, offset, posBase;

	private ChunkPos chunkBase, chunkoffset, chunkNow, chunkOld;

	private boolean firstSet = false, firstLoad = true, needUpdate = false, canWork = true, canLoad = true;

	private int facing = 0, round = 1, roundoffset = 0;

	private Ticket ticket;

	public LavaPumpTileEntity()
	{
		tank = new FluidTank(new FluidStack(FluidRegistry.LAVA, 0), Integer.MAX_VALUE)
		{

			@Override
			public boolean canFillFluidType(FluidStack fluid)
			{
				return false;
			}
		};
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if (canLoad)
		{
			if (needUpdate)
				this.forceLoad();
		}
		if (firstSet)
		{
			chunkBase = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
			chunkoffset = new ChunkPos(0, 0);
			posBase = chunkBase.getBlock(0, 1, 0);
			offset = new BlockPos(0, 0, 0);
			posToDrain = posBase;
			firstSet = false;
		} else
		{
			if (firstLoad)
			{
				chunkBase = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
				chunkOld = new ChunkPos(chunkoffset.chunkXPos + chunkBase.chunkXPos,
						chunkoffset.chunkZPos + chunkBase.chunkZPos);
				posBase = chunkOld.getBlock(0, 1, 0);
				posToDrain = posBase.add(offset);
				firstLoad = false;
				if (ticket != null)
					ForgeChunkManager.forceChunk(ticket, chunkOld);
				;
			}
		}
		if (canWork)
			tryDrainLava();
		fillFluidTo();
	}

	public void fillFluidTo()
	{
		if (tank.getFluid().amount <= 0)
			return;
		BlockPos pos = this.pos.up();
		TileEntity te = world.getTileEntity(pos);
		FluidStack fluid = this.tank.getFluid();
		int amount = 0;
		if (te != null && fluid != null)
			if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
				amount = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN).fill(fluid,
						true);
		if (amount > 0)
			tank.getFluid().amount -= amount;
	}

	private void tryDrainLava()
	{
		if (tank.fillInternal(new FluidStack(FluidRegistry.LAVA, 1000), false) != 1000)
			return;
		for (int i = pos.getY() < 20 ? 0 : pos.getY() - 20; i < pos.getY(); i++)
		{
			IBlockState iblockstate = world.getBlockState(posToDrain.add(0, i, 0));
			Material material = iblockstate.getMaterial();
			if (material == Material.LAVA && iblockstate.getValue(BlockLiquid.LEVEL) == 0)
			{
				world.setBlockState(posToDrain.add(0, i, 0), Blocks.DIRT.getDefaultState());
				tank.fillInternal(new FluidStack(FluidRegistry.LAVA, 1000), true);
			}
		}
		moveToNext();
	}

	private void moveToNext()
	{

		offset = offset.add(1, 0, 0);
		if (offset.getX() < 16)
		{
			posToDrain = posBase.add(offset);
			return;
		} else
		{
			offset = offset.add(-16, 0, 1);
			if (offset.getZ() < 16)
			{
				posToDrain = posBase.add(offset);
				return;
			} else
			{
				offset = new BlockPos(0, 0, 0);
			}
		}
		switch (facing)
		{
		case 0:
			if (roundoffset < round * 2 - 1)
			{
				nextChunk(1, 0);
				if (++roundoffset >= round * 2 - 1)
				{
					facing++;
					roundoffset = 0;
				}
			}
			return;
		case 1:
			if (roundoffset < round * 2 - 1)
			{
				nextChunk(0, 1);
				if (++roundoffset >= round * 2 - 1)
				{
					facing++;
					roundoffset = 0;
				}
			}
			return;
		case 2:
			if (roundoffset < round * 2)
			{
				nextChunk(-1, 0);
				if (++roundoffset >= round * 2)
				{
					facing++;
					roundoffset = 0;
				}
			}
			return;
		case 3:
			if (roundoffset < round * 2)
			{
				nextChunk(0, -1);
				if (++roundoffset >= round * 2)
				{
					facing = 0;
					roundoffset = 0;
					round++;
				}
			}
			if (round > MAXRANGE)
			{
				canWork = false;
				ForgeChunkManager.unforceChunk(ticket, chunkOld);
			}
			return;
		}
	}

	private void nextChunk(int x, int z)
	{
		chunkOld = new ChunkPos(chunkoffset.chunkXPos + chunkBase.chunkXPos,
				chunkoffset.chunkZPos + chunkBase.chunkZPos);
		chunkoffset = new ChunkPos(chunkoffset.chunkXPos + x, chunkoffset.chunkZPos + z);
		chunkNow = new ChunkPos(chunkoffset.chunkXPos + chunkBase.chunkXPos,
				chunkoffset.chunkZPos + chunkBase.chunkZPos);
		posBase = chunkNow.getBlock(0, 1, 0);
		posToDrain = new BlockPos(posBase);
		if (!world.isBlockLoaded(posBase))
			world.getChunkProvider().provideChunk(chunkNow.chunkXPos, chunkNow.chunkZPos);
		ForgeChunkManager.forceChunk(ticket, chunkNow);
		if (!chunkOld.equals(chunkBase))
			ForgeChunkManager.unforceChunk(ticket, chunkOld);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
			setDataFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		return this.setNBTFromData(super.writeToNBT(compound));
	}

	public void setDataFromNBT(NBTTagCompound compound)
	{
		player = compound.getString("Player");
		offset = Helper.getBlockPosFromstring(compound.getString("offset"));
		chunkoffset = Helper.getChunkPosFromString(compound.getString("chunkoffset"));
		canWork = compound.getBoolean("canWork");
		facing = compound.getInteger("facing");
		round = compound.getInteger("round");
		roundoffset = compound.getInteger("roundoffset");
	}

	public NBTTagCompound setNBTFromData(NBTTagCompound compound)
	{
		compound.setString("Player", player);
		compound.setString("offset", offset.toString());
		compound.setString("chunkoffset", chunkoffset.toString());
		compound.setBoolean("canWork", canWork);
		compound.setInteger("facing", facing);
		compound.setInteger("round", round);
		compound.setInteger("roundoffset", roundoffset);
		return compound;
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if (world.isRemote)
			return;
		unforceLoad();
	}

	@Override
	public void validate()
	{
		super.validate();
		if (world.isRemote)
			return;
		forceLoad();
	}

	private void forceLoad()
	{
		if (player == null)
		{
			this.needUpdate = true;
			return;
		} else
			this.needUpdate = false;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		EntityPlayer playerBind = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayerByUsername(player);
		if (playerBind == null)
		{
			if (TicketManager.WAIT_PLAYER_PUMP.isEmpty() || !TicketManager.WAIT_PLAYER_PUMP.containsKey(player))
				TicketManager.WAIT_PLAYER_PUMP.put(player, Sets.newHashSet());
			TicketManager.WAIT_PLAYER_PUMP.get(player).add(new BlockPosWithDim(pos, world.provider.getDimension()));
			return;
		}
		ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		ChunkInfo info = new ChunkInfo(world.provider.getDimension(), chunk);
		if (TicketManager.PUMP_TICKETS.containsKey(info))
		{
			this.canLoad = false;
			return;
		}
		if (ticket == null)
			ticket = ForgeChunkManager.requestPlayerTicket(Main.instance, player, world, Type.ENTITY);
		ticket.bindEntity(playerBind);
		TicketManager.PUMP_TICKETS.put(info, ticket);
		if (TicketManager.LOADED_PUMP.isEmpty()
				|| !TicketManager.LOADED_PUMP.containsKey(world.provider.getDimension()))
			TicketManager.LOADED_PUMP.put(world.provider.getDimension(), Sets.newHashSet());
		TicketManager.LOADED_PUMP.get(world.provider.getDimension()).add(getPos());
		ForgeChunkManager.forceChunk(ticket, chunk);

	}

	private void unforceLoad()
	{
		if (world.getBlockState(pos).getBlock() != BlockLoader.lavaPump)
			TicketManager.LOADED_PUMP.get(world.provider.getDimension()).remove(getPos());
		if (ticket == null)
			return;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if (player == null)
			throw new RuntimeException("Can't find player");
		ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		ForgeChunkManager.unforceChunk(ticket, chunk);
		ForgeChunkManager.releaseTicket(ticket);
		TicketManager.PUMP_TICKETS.remove(new ChunkInfo(world.provider.getDimension(), chunk));
	}

	public void onPlayerLogOut(String player)
	{
		if (this.player.equals(player))
		{
			ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
			ForgeChunkManager.unforceChunk(ticket, chunk);
			ForgeChunkManager.releaseTicket(ticket);
			TicketManager.PUMP_TICKETS.remove(new ChunkInfo(world.provider.getDimension(), chunk));
			this.ticket = null;
		}
	}

	public void showWork(EntityPlayer player)
	{
		player.sendMessage(new TextComponentTranslation("lavaPump.msg", TextFormatting.GREEN, "\n",
				StringUtils.substringBetween(this.posToDrain.toString(), "{", "}"), "\n", tank.getFluid().amount));
	}

	public void setFirstSet(boolean firstSet)
	{
		this.firstSet = firstSet;
	}

	public void setNeedUpdate(boolean needUpdate)
	{
		this.needUpdate = needUpdate;
	}

	public FluidTank getTank()
	{
		return tank;
	}

	public String getPlayer()
	{
		return player;
	}

	public void setPlayer(String player)
	{
		this.player = player;
	}

}
