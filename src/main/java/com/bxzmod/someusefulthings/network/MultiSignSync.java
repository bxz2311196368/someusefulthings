package com.bxzmod.someusefulthings.network;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.capability.ITPLocation;
import com.bxzmod.someusefulthings.capability.ITPLocation.BlockPosWithDimAndName;
import com.bxzmod.someusefulthings.tileentity.CraftingTableTileEntity;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import com.bxzmod.someusefulthings.tileentity.XPReservoirTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MultiSignSync implements IMessage
{

	public NBTTagCompound nbt;

	@Override
	public void fromBytes(ByteBuf buf)
	{
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbt);
	}

	public static class ToClientHandler implements IMessageHandler<MultiSignSync, IMessage>
	{

		@Override
		public IMessage onMessage(MultiSignSync message, MessageContext ctx)
		{
			if (ctx.side == Side.CLIENT)
			{
				final NBTTagCompound nbt = message.nbt;
				final String type = nbt.getString("Type");
				Minecraft.getMinecraft().addScheduledTask(() -> {
					switch (type)
					{
					case "MS_Sync":
					{
						NBTTagCompound data = (NBTTagCompound) nbt.getTag("Data");
						MobSummonTileEntity.NBTToData(data);
					}
					}
				});
			}
			return null;
		}

	}

	public static class ToServerHandler implements IMessageHandler<MultiSignSync, IMessage>
	{

		@Override
		public IMessage onMessage(MultiSignSync message, MessageContext ctx)
		{
			if (ctx.side == Side.SERVER)
			{
				final MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();
				final NBTTagCompound nbt = message.nbt;
				final String type = nbt.getString("Type");
				// System.out.println(nbt.toString());
				Server.addScheduledTask(() -> {
					switch (type)
					{
					case "Crafter":
						if (nbt.hasKey("world") && nbt.hasKey("BlockPos"))
						{
							WorldServer world = Server.worldServerForDimension(nbt.getInteger("world"));
							BlockPos pos = Helper.getBlockPosFromstring(nbt.getString("BlockPos"));
							boolean auto = nbt.getBoolean("Auto");
							boolean output = nbt.getBoolean("Output");
							TileEntity te = world.getTileEntity(pos);
							if (te != null && te instanceof CraftingTableTileEntity)
							{
								CraftingTableTileEntity t = (CraftingTableTileEntity) te;
								t.setAuto(auto);
								t.setStayInSlot(output);
							}
						}
						break;
					case "TP":
						if (nbt.hasKey("Player") && nbt.hasKey("Button") && nbt.hasKey("Select"))
						{
							EntityPlayerMP player = Server.getPlayerList().getPlayerByUsername(nbt.getString("Player"));
							ITPLocation cp = player.getCapability(CapabilityLoader.TP_MENU, null);
							int button = nbt.getInteger("Button"), select = nbt.getInteger("Select"), page = nbt
								.getInteger("Page");
							switch (button)
							{
							case 25:
								if (nbt.hasKey("new") && nbt.getBoolean("new") && nbt.hasKey("PosName"))
								{
									cp.addPos(new BlockPosWithDimAndName(
										new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY),
											MathHelper.floor(player.posZ)), player.dimension,
										nbt.getString("PosName")));
								}
								break;
							case 26:
								if (nbt.hasKey("Rename") && nbt.getBoolean("Rename"))
								{
									cp.rename(select + (page - 1) * 25, nbt.getString("PosRename"));
								}
								break;
							case 27:
								if (nbt.hasKey("Teleport") && nbt.getBoolean("Teleport"))
								{
									player.closeScreen();
									BlockPosWithDimAndName move_to = cp.getPos(select + (page - 1) * 25);
									Helper.teleportEntity(player, move_to.pos, move_to.dim);
								}
								return;
							case 28:
								if (nbt.hasKey("Remove") && nbt.getBoolean("Remove"))
								{
									cp.delPos(select + (page - 1) * 25);
								}
								break;
							}
							TPLocationSync message1 = new TPLocationSync();
							IStorage<ITPLocation> storage = CapabilityLoader.TP_MENU.getStorage();
							message1.nbt = (NBTTagCompound) storage.writeNBT(CapabilityLoader.TP_MENU, cp, null);
							NetworkLoader.instance.sendTo(message1, player);
						}
					case "XPReservoir":
						if (nbt.hasKey("Player") && nbt.hasKey("Button") && nbt.hasKey("Dim") && nbt.hasKey("BlockPos"))
						{
							EntityPlayer player = Server.getPlayerList().getPlayerByUsername(nbt.getString("Player"));
							TileEntity te = Server.worldServerForDimension(nbt.getInteger("Dim"))
								.getTileEntity(Helper.getBlockPosFromstring(nbt.getString("BlockPos")));
							if (te != null && te instanceof XPReservoirTileEntity)
								((XPReservoirTileEntity) te).setXPFromPlayer(player, nbt.getInteger("Button"));
						}
					case "MobSummon":
						if (nbt.hasKey("selectEntity") && nbt.hasKey("BlockPos"))
						{
							WorldServer world = Server.worldServerForDimension(nbt.getInteger("world"));
							BlockPos pos = Helper.getBlockPosFromstring(nbt.getString("BlockPos"));
							MobSummonTileEntity te = (MobSummonTileEntity) world.getTileEntity(pos);
							if (nbt.getString("selectEntity").equals("null"))
								te.setSelectEntity("");
							else
								te.setSelectEntity(nbt.getString("selectEntity"));
						}
					}
				});
			}
			return null;
		}

	}

}
