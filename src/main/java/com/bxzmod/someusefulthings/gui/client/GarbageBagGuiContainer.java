package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.gui.server.GarbageBagContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GarbageBagGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/PortableInventory.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private static final int BUTTON_ENABLE = 0;
	private GarbageBagContainer gui;

	public GarbageBagGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 222;
		this.gui = (GarbageBagContainer) inventorySlotsIn;
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
		String title = I18n.format("item.garbageBag.name");
		String state = I18n.format(gui.isEnable() ? "gui.garbageBag.enable" : "gui.garbageBag.disable");
		fontRendererObj.drawString(title, 8, 5, 0x404040);
		fontRendererObj.drawString(state, 30, 127, 0x404040);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_ENABLE, offsetX + 8, offsetY + 125, 16, 16, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

					if (gui.isEnable())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 207, 0, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 191, 0, this.width, this.height);
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
		case 0:
			gui.changeEnable();
			break;
		default:
			super.actionPerformed(button);
		}
	}

}
