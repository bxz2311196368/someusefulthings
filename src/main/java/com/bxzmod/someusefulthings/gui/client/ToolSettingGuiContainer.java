package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.ToolSettingContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;

public class ToolSettingGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/ToolSetting.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	private static final Logger LOGGER = LogManager.getLogger();

	private static final int BUTTON_RANGE = 0;
	private static final int BUTTON_DEPTH = 1;
	private static final int BUTTON_ENCH = 2;

	private static int a = 1;
	private static int b = 1;

	EntityPlayerSP p;

	ToolSettingContainer gui;

	NBTTagList ench;

	public ToolSettingGuiContainer(ToolSettingContainer inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 133;
		this.p = Minecraft.getMinecraft().player;
		this.a = inventorySlotsIn.getRange();
		this.b = inventorySlotsIn.getDepth();
		this.gui = inventorySlotsIn;
		this.ench = inventorySlotsIn.getEnch();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		int r = this.a * 2 - 1;
		String title = I18n.format("gui.toolsetting");
		String range = I18n.format("gui.toolsetting.range") + ":" + r + "*" + r;
		String depth = I18n.format("gui.toolsetting.depth") + ":" + this.b;
		String enchs = I18n.format("gui.toolsetting.ench") + ":";
		if (getEnchantmentLevel(Enchantments.FORTUNE, this.ench) > 0)
		{
			enchs = enchs + I18n.format("gui.toolsetting.fortune");
		} else if (getEnchantmentLevel(Enchantments.SILK_TOUCH, this.ench) > 0)
		{
			enchs = enchs + I18n.format("gui.toolsetting.silktouch");
		} else
		{
			enchs = enchs + "null";
		}
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6,
				0x404040);
		this.fontRendererObj.drawString(range, (this.xSize - this.fontRendererObj.getStringWidth(range)) / 2, 26,
				0x404040);
		this.fontRendererObj.drawString(depth, (this.xSize - this.fontRendererObj.getStringWidth(depth)) / 2, 56,
				0x404040);
		this.fontRendererObj.drawString(enchs, (this.xSize - this.fontRendererObj.getStringWidth(enchs)) / 2, 86,
				0x404040);

	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_RANGE, offsetX + 81, offsetY + 40, 15, 10, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;

					if (x >= 0 && y >= 0 && x < this.width && y < this.height)
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 146, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 134, this.width, this.height);
					}
				}
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_DEPTH, offsetX + 81, offsetY + 70, 15, 10, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;

					if (x >= 0 && y >= 0 && x < this.width && y < this.height)
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 146, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 134, this.width, this.height);
					}
				}
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_ENCH, offsetX + 81, offsetY + 100, 15, 10, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;

					if (x >= 0 && y >= 0 && x < this.width && y < this.height)
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 146, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 134, this.width, this.height);
					}
				}
			}
		});
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id)
		{
		case BUTTON_RANGE:
			if (this.a < 5)
				++this.a;
			else
				this.a = 1;
			this.gui.setRange(this.a);
			break;
		case BUTTON_DEPTH:
			if (this.b < 9)
				++this.b;
			else
				this.b = 1;
			this.gui.setDepth(this.b);
			break;
		case BUTTON_ENCH:
			this.gui.setEnch();
			this.ench = this.gui.getEnch();
			break;
		default:
			super.actionPerformed(button);
			return;
		}
	}

	public int getEnchantmentLevel(Enchantment enchID, @Nullable NBTTagList nbttaglist)
	{
		if (nbttaglist == null)
		{
			return 0;
		} else
		{
			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				Enchantment enchantment = Enchantment.getEnchantmentByID(nbttaglist.getCompoundTagAt(i).getShort("id"));
				int j = nbttaglist.getCompoundTagAt(i).getShort("lvl");

				if (enchantment == enchID)
				{
					return j;
				}
			}
			return 0;
		}
	}
}
