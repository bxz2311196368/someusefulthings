package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Helper;
import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.gui.server.XPReservoirContainer;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class XPReservoirGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/ToolSetting.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	public static final ResourceLocation TEXTURES_B = new ResourceLocation(
			Info.MODID + ":" + "textures/gui/container/button.png");
	private static final ResourceLocation XP_BAR = new ResourceLocation("textures/gui/icons.png");
	private XPReservoirContainer gui;

	public XPReservoirGuiContainer(XPReservoirContainer inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 133;
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;
		this.gui = inventorySlotsIn;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String title = I18n.format("tile.xpReservoir.name");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6,
				0x404040);
		String info = String.format("xp=%d,level=%d", gui.getXp(), Helper.getXpTotalLevel(gui.getXp()));
		this.fontRendererObj.drawString(info, (this.xSize - this.fontRendererObj.getStringWidth(info)) / 2, 60,
				0x404040);

		GlStateManager.color(1.0F, 1.0F, 1.0F);
		int textureWidth = 2 + (int) Math.ceil(180 * this.getXPLevelPercent(this.gui.getXp()));
		this.mc.getTextureManager().bindTexture(XP_BAR);
		this.drawZoomTexturedModalRect(42, 56, 0, 69, textureWidth / 2, 5, textureWidth, 5, 256, 256);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

		this.mc.getTextureManager().bindTexture(XP_BAR);
		this.drawZoomTexturedModalRect(offsetX + 42, offsetY + 56, 0, 64, 91, 5, 182, 5, 256, 256);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 3; j++)
				this.addButton(new GuiButtonMe(i * 3 + j, offsetX + 48 + j * 32, offsetY + 30 + i * 48, 16, 16, ""));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		message.nbt.setString("Type", "XPReservoir");
		message.nbt.setString("Player", Minecraft.getMinecraft().player.getName());
		if (button.id < 6 && button.id >= 0)
		{
			message.nbt.setInteger("Button", button.id);
			message.nbt.setInteger("Dim", Minecraft.getMinecraft().world.provider.getDimension());
			message.nbt.setString("BlockPos", this.gui.getTe().getBlockPos().toString());
			NetworkLoader.instance.sendToServer(message);
		}
		super.actionPerformed(button);
	}

	public void drawZoomTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height,
			float textureAreaWidth, float textureAreaHeight, float textureWidth, float textureHeight)
	{
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double) x, (double) (y + height), (double) this.zLevel)
				.tex((double) (textureX * f), (double) ((textureY + textureAreaHeight) * f1)).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
				.tex((double) ((textureX + textureAreaWidth) * f), (double) ((textureY + textureAreaHeight) * f1))
				.endVertex();
		vertexbuffer.pos((double) (x + width), (double) y, (double) this.zLevel)
				.tex((double) ((textureX + textureAreaWidth) * f), (double) (textureY * f1)).endVertex();
		vertexbuffer.pos((double) x, (double) y, (double) this.zLevel)
				.tex((double) (textureX * f), (double) (textureY * f1)).endVertex();
		tessellator.draw();
	}

	public double getXPLevelPercent(int xp)
	{
		return (double) (xp - Helper.getTotalXp(Helper.getXpTotalLevel(xp)))
				/ Helper.getXpBarCap(Helper.getXpTotalLevel(xp));
	}

	public class GuiButtonMe extends GuiButton
	{

		public GuiButtonMe(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
		{
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(TEXTURES_B);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawZoomTexturedModalRect(this.xPosition, this.yPosition, 0, 20, width, height, 40, 20, 256, 256);
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
			this.drawCenteredString(fontrenderer, this.getDisplayString(this.id), this.xPosition + this.width / 2,
					this.yPosition + (this.height - 8) / 2, j);
		}

		private String getDisplayString(int id)
		{
			switch (id)
			{
			case 0:
				return "+1";
			case 1:
				return "+10";
			case 2:
				return "+++";
			case 3:
				return "-1";
			case 4:
				return "-10";
			case 5:
				return "---";
			}
			return "";
		}

		public void drawZoomTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height,
				float textureAreaWidth, float textureAreaHeight, float textureWidth, float textureHeight)
		{
			float f = 1.0F / textureWidth;
			float f1 = 1.0F / textureHeight;
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos((double) x, (double) (y + height), (double) this.zLevel)
					.tex((double) (textureX * f), (double) ((textureY + textureAreaHeight) * f1)).endVertex();
			vertexbuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
					.tex((double) ((textureX + textureAreaWidth) * f), (double) ((textureY + textureAreaHeight) * f1))
					.endVertex();
			vertexbuffer.pos((double) (x + width), (double) y, (double) this.zLevel)
					.tex((double) ((textureX + textureAreaWidth) * f), (double) (textureY * f1)).endVertex();
			vertexbuffer.pos((double) x, (double) y, (double) this.zLevel)
					.tex((double) (textureX * f), (double) (textureY * f1)).endVertex();
			tessellator.draw();
		}

	}

}
