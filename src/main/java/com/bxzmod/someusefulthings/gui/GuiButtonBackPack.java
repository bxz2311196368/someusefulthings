package com.bxzmod.someusefulthings.gui;

import com.bxzmod.someusefulthings.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonBackPack extends GuiButton
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(
		ModInfo.MODID + ":" + "textures/gui/container/button.png");

	public GuiButtonBackPack(int buttonId, int x, int y, String buttonText)
	{
		super(buttonId, x, y, buttonText);
	}

	public GuiButtonBackPack(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean isenable)
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		enabled = isenable;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		FontRenderer fontrenderer = mc.fontRendererObj;
		mc.getTextureManager().bindTexture(TEXTURES);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.enabled)
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 20, this.width, this.height);
		else
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);

		int j = 14737632;

		if (packedFGColour != 0)
		{
			j = packedFGColour;
		} else if (!this.enabled)
		{
			j = 10526880;
		} else if (this.hovered)
		{
			j = 16777120;
		}
		this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
				this.yPosition + (this.height - 8) / 2, j);
	}

}
