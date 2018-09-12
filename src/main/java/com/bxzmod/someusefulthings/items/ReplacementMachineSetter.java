package com.bxzmod.someusefulthings.items;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.creativetabs.CreativeTabsLoader;
import com.bxzmod.someusefulthings.tileentity.ReplacementMachineTileEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ReplacementMachineSetter extends Item
{

	public ReplacementMachineSetter()
	{
		super();
		this.setUnlocalizedName("replacementMachineSetter");
		this.setRegistryName("replacement_machine_setter");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.tabsomeusefulthings);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim")
					|| !stack.getTagCompound().hasKey("BlockSelect"))
			{
				if (worldIn.getTileEntity(pos) != null
						&& worldIn.getTileEntity(pos) instanceof ReplacementMachineTileEntity)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setInteger("dim", ((ReplacementMachineTileEntity) worldIn.getTileEntity(pos)).getDim());
					nbt.setString("BlockSelect", pos.toString());
					stack.setTagCompound(nbt);
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_10", TextFormatting.GREEN));
					return EnumActionResult.SUCCESS;
				} else
				{
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_3", TextFormatting.RED, '\n'));
					return EnumActionResult.FAIL;
				}
			}
			if (playerIn.isSneaking())
			{
				if (worldIn.getTileEntity(pos) != null
						&& worldIn.getTileEntity(pos) instanceof ReplacementMachineTileEntity)
				{
					stack.setTagCompound(null);
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_2", TextFormatting.RED));
					return EnumActionResult.SUCCESS;
				} else
				{
					if (worldIn.isBlockLoaded(
							Helper.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect")))
							&& worldIn.getTileEntity(Helper
									.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect"))) != null
							&& worldIn.getTileEntity(Helper.getBlockPosFromstring(stack.getTagCompound()
									.getString("BlockSelect"))) instanceof ReplacementMachineTileEntity)
					{
						if (((ReplacementMachineTileEntity) worldIn.getTileEntity(
								Helper.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect"))))
										.removePos(pos))
						{
							playerIn.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_4",
									TextFormatting.GREEN, pos.toString()));
							return EnumActionResult.SUCCESS;
						} else
						{
							playerIn.sendStatusMessage(new TextComponentTranslation("replacementMachine.msg_5",
									TextFormatting.YELLOW, pos.toString()));
							return EnumActionResult.FAIL;
						}
					} else
					{
						playerIn.sendStatusMessage(
								new TextComponentTranslation("replacementMachine.msg_11", TextFormatting.RED));
						return EnumActionResult.FAIL;
					}
				}
			} else
			{
				if (worldIn.provider.getDimension() != stack.getTagCompound().getInteger("dim"))
				{
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_7", TextFormatting.YELLOW));
					return EnumActionResult.FAIL;
				}
				if (worldIn.isBlockLoaded(Helper.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect")))
						&& worldIn.getTileEntity(
								Helper.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect"))) != null
						&& worldIn.getTileEntity(Helper.getBlockPosFromstring(stack.getTagCompound()
								.getString("BlockSelect"))) instanceof ReplacementMachineTileEntity)
				{
					((ReplacementMachineTileEntity) worldIn.getTileEntity(
							Helper.getBlockPosFromstring(stack.getTagCompound().getString("BlockSelect"))))
									.addToList(playerIn, pos);
					return EnumActionResult.SUCCESS;
				} else
				{
					playerIn.sendStatusMessage(
							new TextComponentTranslation("replacementMachine.msg_11", TextFormatting.RED));
					return EnumActionResult.FAIL;
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(I18n.format("tooltip.replacementMachineSetter", TextFormatting.BLUE));
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			tooltip.add(I18n.format("tooltip.replacementMachineSetter.info", TextFormatting.BLUE, nbt.getInteger("dim"),
					StringUtils.substringBetween(nbt.getString("BlockSelect"), "{", "}")));
		} else
		{
			tooltip.add(I18n.format("tooltip.replacementMachineSetter.Unbound", TextFormatting.BLUE));
		}
	}

}
