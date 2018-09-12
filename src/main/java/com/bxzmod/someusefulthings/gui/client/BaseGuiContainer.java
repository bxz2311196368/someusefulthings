package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.gui.server.AbstractContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class BaseGuiContainer extends GuiContainer
{
	private String tile;
	private ResourceLocation texture;
	public AbstractContainer guiServer;

	public BaseGuiContainer(AbstractContainer inventorySlotsIn, String tile, ResourceLocation texture, int xSize, int ySize)
	{
		super(inventorySlotsIn);
		this.guiServer =inventorySlotsIn;
		this.tile = "tile." + tile + ".name";
		this.texture = texture;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(this.texture);
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String title = I18n.format(this.tile);
		this.fontRendererObj
			.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6, 0x404040);

	}
}
