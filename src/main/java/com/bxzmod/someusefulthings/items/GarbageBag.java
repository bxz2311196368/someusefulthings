package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import com.bxzmod.someusefulthings.network.GarbageBagSetting;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;

public class GarbageBag extends Item
{
	private ArrayList<ItemStack> item = new ArrayList<ItemStack>();

	public GarbageBag()
	{
		super();
		this.setUnlocalizedName("garbageBag");
		this.setRegistryName("garbage_bag");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		this.onItemRightClick(stack, worldIn, playerIn, hand);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			GarbageBagSetting message = new GarbageBagSetting();
			message.nbt = new NBTTagCompound();
			message.nbt = playerIn.getCapability(CapabilityLoader.GARBAGBAG, null).serializeNBT();
			NetworkLoader.instance.sendTo(message, FMLCommonHandler.instance().getMinecraftServerInstance()
					.getPlayerList().getPlayerByUsername(playerIn.getName()));
			BlockPos pos = playerIn.getPosition();
			int id = GuiLoader.GUI_G_B;
			playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

}
