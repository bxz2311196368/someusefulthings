package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.gui.server.CopyEnchantmentContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class CopyEnchantmentGuiContainer extends GuiContainer
{

	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/CopyEnchantment.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	CopyEnchantmentContainer inventory;

	public CopyEnchantmentGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 133;
		this.inventory = (CopyEnchantmentContainer) inventorySlotsIn;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = I18n.format("tile.copyEnchantment.name");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6,
				0x404040);

	}

}
