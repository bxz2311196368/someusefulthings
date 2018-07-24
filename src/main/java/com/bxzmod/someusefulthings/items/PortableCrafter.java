package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.ItemStackHandlerModify;
import com.bxzmod.someusefulthings.Main;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.gui.GuiLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class PortableCrafter extends Item
{

	public PortableCrafter()
	{
		super();
		this.setUnlocalizedName("portableCrafter");
		this.setRegistryName("portable_crafter");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			int id = GuiLoader.GUI_P_C;
			playerIn.openGui(Main.instance, id, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		}

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if (this.getClass() == PortableCrafter.class)
			return new ICapabilityProvider()
			{
				@Override
				public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
				{
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability);
				}

				@Override
				public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
				{
					if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability))
						return (T) new ItemStackHandlerModify(9);
					return null;
				}
			};
		return super.initCapabilities(stack, nbt);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(I18n.format("tooltip.portableCrafter", TextFormatting.BLUE));
	}

}
