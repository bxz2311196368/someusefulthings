package com.bxzmod.someusefulthings.gui.server;

import com.bxzmod.someusefulthings.items.ItemLoader;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.network.ToolSettingSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;

public class ToolSettingContainer extends Container
{
	EntityPlayer p;

	private static int a = 1;
	private static int b = 1;

	NBTTagList ench;

	public ToolSettingContainer(EntityPlayer player)
	{
		this.p = player;
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		if (player.getHeldItemMainhand().getItem() == ItemLoader.limitlesstool)
		{
			if (player.getHeldItemMainhand().hasTagCompound())
			{
				if (!(player.getHeldItemMainhand().getTagCompound().hasKey("dig_parameter")))
				{
					player.getHeldItemMainhand().getTagCompound().setTag("dig_parameter", dig);
				} else
				{
					this.a = ((NBTTagCompound) player.getHeldItemMainhand().getTagCompound().getTag("dig_parameter"))
							.getInteger("dig_range");
					this.b = ((NBTTagCompound) player.getHeldItemMainhand().getTagCompound().getTag("dig_parameter"))
							.getInteger("dig_depth");
				}
				if (player.getHeldItemMainhand().isItemEnchanted())
				{
					this.ench = player.getHeldItemMainhand().getEnchantmentTagList();
				} else
				{
					player.getHeldItemMainhand().addEnchantment(Enchantments.LOOTING, 10);
					player.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, 10);
					this.ench = player.getHeldItemMainhand().getEnchantmentTagList();
				}
			} else
			{
				player.getHeldItemMainhand().addEnchantment(Enchantments.LOOTING, 10);
				player.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, 10);
				this.ench = player.getHeldItemMainhand().getEnchantmentTagList();
				player.getHeldItemMainhand().getTagCompound().setTag("dig_parameter", dig);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if (playerIn.getHeldItemMainhand().getItem() == ItemLoader.limitlesstool)
			return true;
		return false;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			EntityPlayerSP the_player = Minecraft.getMinecraft().player;
			NBTTagCompound dig = new NBTTagCompound();
			dig.setInteger("dig_range", this.a);
			dig.setInteger("dig_depth", this.b);
			int r = this.a * 2 - 1;
			if (the_player.getHeldItemMainhand().getTagCompound().hasKey("dig_parameter"))
			{
				the_player.getHeldItemMainhand().getTagCompound().removeTag("dig_parameter");
			}
			the_player.getHeldItemMainhand().getTagCompound().setTag("dig_parameter", dig);
			if (the_player.getHeldItemMainhand().getTagCompound().hasKey("ench"))
			{
				the_player.getHeldItemMainhand().getTagCompound().removeTag("ench");
			}
			the_player.getHeldItemMainhand().getTagCompound().setTag("ench", this.ench);
			ToolSettingSync message = new ToolSettingSync();
			message.nbt = new NBTTagCompound();
			message.nbt.setString("name", this.p.getName());
			the_player.getHeldItemMainhand().writeToNBT(message.nbt);
			NetworkLoader.instance.sendToServer(message);
		}
	}

	public EntityPlayer getPlayer()
	{
		return this.p;
	}

	public int getRange()
	{
		return this.a;
	}

	public int getDepth()
	{
		return this.b;
	}

	public NBTTagList getEnch()
	{
		return this.ench.copy();
	}

	public void setRange(int range)
	{
		this.a = range;
	}

	public void setDepth(int depth)
	{
		this.b = depth;
	}

	public void setEnch()
	{
		int flag_0 = -1, flag_1 = -1, flag_2 = -1;
		boolean flag = false;
		if (this.p.getHeldItemMainhand().getItem() == ItemLoader.limitlesstool)
		{
			NBTTagList ench = this.p.getHeldItemMainhand().getEnchantmentTagList();
			if (this.p.getHeldItemMainhand().getTagCompound().hasKey("RepairCost"))
				this.p.getHeldItemMainhand().getTagCompound().removeTag("RepairCost");
			if (ench.hasNoTags())
			{
				this.p.getHeldItemMainhand().addEnchantment(Enchantments.LOOTING, 10);
				this.p.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, 10);
			} else
			{
				NBTTagList enchtemp = ench.copy();
				for (int i = 0; i < enchtemp.tagCount(); i++)
				{
					int m = ((NBTTagCompound) ench.get(i)).getShort("id");
					int n = ((NBTTagCompound) ench.get(i)).getShort("lvl");
					if (m == 21)
						flag_0 = n < 10 ? i : -2;
					if (m == 35)
					{
						flag_1 = i;
						flag = true;
					}
					if (m == 33)
					{
						flag_2 = i;
					}
				}
				int[] enchlist = { flag_0, flag_1, flag_2 };
				Arrays.sort(enchlist);
				for (int k = 2; k >= 0; k--)
					if (enchlist[k] >= 0)
						ench.removeTag(enchlist[k]);
				if (flag_0 > -2)
					this.p.getHeldItemMainhand().addEnchantment(Enchantments.LOOTING, 10);
				if (flag_1 >= 0 && flag)
					this.p.getHeldItemMainhand().addEnchantment(Enchantments.SILK_TOUCH, 1);
				if (flag_2 >= 0 && !flag)
					this.p.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, 10);
				if (flag_1 == -1 && flag_2 == -1)
					this.p.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, 10);
			}
		}
		this.ench = this.p.getHeldItemMainhand().getEnchantmentTagList().copy();
	}
}
