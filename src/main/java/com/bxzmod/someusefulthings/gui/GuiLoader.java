package com.bxzmod.someusefulthings.gui;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.gui.client.*;
import com.bxzmod.someusefulthings.gui.server.*;
import com.bxzmod.someusefulthings.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiLoader implements IGuiHandler
{
	public static final int GUI_R_E = 1;
	public static final int GUI_C_E = 2;
	public static final int GUI_R_M = 3;
	public static final int GUI_P_I = 4;
	public static final int GUI_T_S = 5;
	public static final int GUI_F_E_T = 6;
	public static final int GUI_G_B = 7;
	public static final int GUI_C_T = 8;
	public static final int GUI_W_B_C = 9;
	public static final int GUI_T_P = 10;
	public static final int GUI_X_P = 11;
	public static final int GUI_G_H = 12;
	public static final int GUI_F_F = 13;
	public static final int GUI_M_S = 14;
	public static final int GUI_E_B = 15;
	public static final int GUI_P_C = 16;

	public static final int DATA_R_E = 1;
	public static final int DATA_C_E = 2;
	public static final int DATA_R_M = 3;
	public static final int DATA_P_I = 4;
	public static final int DATA_X_P = 5;

	public GuiLoader(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
		case GUI_R_E:
			return new RemoveEnchantmentContainer(player,
				(RemoveEnchantmentTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_C_E:
			return new CopyEnchantmentContainer(player,
				(CopyEnchantmentTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_R_M:
			return new ReinforcementMachineContainer(player,
				(ReinforcementMachineTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_P_I:
			return new PortableInventoryContainer(player);
		case GUI_T_S:
			return new ToolSettingContainer(player);
		case GUI_F_E_T:
			return new ForceEnchTableContainer(player,
				(ForceEnchTableTiltEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_G_B:
			return new GarbageBagContainer(player);
		case GUI_C_T:
			return new CraftingTableContainer(player, world,
				(CraftingTableTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_W_B_C:
			return new ContainerWorkbenchTweak(player.inventory, world, new BlockPos(x, y, z));
		case GUI_T_P:
			return new TPMenuContainer(player);
		case GUI_X_P:
			return new XPReservoirContainer(player, world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_G_H:
			return new GreenHouseContainer(player, world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_F_F:
			return new FastFurnaceContainer(player, (FastFurnaceTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_M_S:
			return new MobSummonContainer(player, (MobSummonTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_E_B:
			return new EnergyBlockContainer(player, (EnergyBlockTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_P_C:
			return new ContainerWorkbenchTweak(player.inventory, world, new BlockPos(x, y, z));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
		case GUI_R_E:
			return new RemoveEnchantmentGuiContainer(new RemoveEnchantmentContainer(player,
				(RemoveEnchantmentTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_C_E:
			return new CopyEnchantmentGuiContainer(new CopyEnchantmentContainer(player,
				(CopyEnchantmentTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_R_M:
			return new ReinforcementMachineGuiContainer(new ReinforcementMachineContainer(player,
				(ReinforcementMachineTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_P_I:
			return new PortableInventoryGuiContainer(new PortableInventoryContainer(player));
		case GUI_T_S:
			return new ToolSettingGuiContainer(new ToolSettingContainer(player));
		case GUI_F_E_T:
			return new ForceEnchTableGuiContainer(new ForceEnchTableContainer(player,
				(ForceEnchTableTiltEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_G_B:
			return new GarbageBagGuiContainer(new GarbageBagContainer(player));
		case GUI_C_T:
			return new CraftingTableGuiContainer(new CraftingTableContainer(player, world,
				(CraftingTableTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_W_B_C:
			return new GuiCraftingTweak(player.inventory, world, new BlockPos(x, y, z));
		case GUI_T_P:
			return new TPMenuGuiContainer(new TPMenuContainer(player));
		case GUI_X_P:
			return new XPReservoirGuiContainer(
				new XPReservoirContainer(player, world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_G_H:
			return new GreenHouseGuiContainer(
				new GreenHouseContainer(player, world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_F_F:
			return new FastFurnaceGuiContainer(
				new FastFurnaceContainer(player, (FastFurnaceTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_M_S:
			return new MobSummonGuiContainer(
				new MobSummonContainer(player, (MobSummonTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_E_B:
			return new EnergyBlockGuiContainer(
				new EnergyBlockContainer(player, (EnergyBlockTileEntity) world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_P_C:
			return new GuiCraftingTweak(player.inventory, world, new BlockPos(x, y, z));
		default:
			return null;
		}
	}

}
