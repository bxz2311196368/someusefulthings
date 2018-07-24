package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.CraftingTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CraftingTableGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/CraftingTable.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/spectator_widgets.png");

	CraftingTableContainer gui;

	private static final int BUTTON_ENABLE = 0;
	private static final int BUTTON_OUTPUT = 1;

	public CraftingTableGuiContainer(CraftingTableContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "craftingTable", TEXTURE, 176, 206);
		gui = inventorySlotsIn;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString(
			I18n.format(gui.getTe().isAuto() ? "gui.craftTable.enable" : "gui.craftTable.disable") + "  " + I18n
				.format(gui.getTe().isStayInSlot() ? "gui.craftTable.Output.enable" : "gui.craftTable.Output.disable"),
			7, 73, 4210752);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_ENABLE, offsetX + 63, offsetY + 57, 16, 16, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

					if (gui.getTe().isAuto())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 207, 0, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 191, 0, this.width, this.height);
					}
				}
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_OUTPUT, offsetX + 81, offsetY + 57, 16, 16, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURE);

					if (gui.getTe().isStayInSlot())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 128, 0, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 163, 0, this.width, this.height);
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
		case BUTTON_ENABLE:
			gui.getTe().setAuto(!gui.getTe().isAuto());
			gui.syncData();
			break;
		case BUTTON_OUTPUT:
			gui.getTe().setStayInSlot(!gui.getTe().isStayInSlot());
			gui.syncData();
			break;
		}
		super.actionPerformed(button);
	}

}
