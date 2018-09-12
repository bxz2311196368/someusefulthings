package com.bxzmod.someusefulthings.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.google.common.base.Charsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.model.ITransformation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;

import static org.lwjgl.opengl.GL11.*;

public class BXZRenderHelper
{
	public static final FaceBakery FACE_BAKERY = new FaceBakery();

	public static final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

	public static final ResourceLocation DEFAULT = new ResourceLocation(
			ModInfo.MODID + ":" + "textures/gui/container/default.png");

	public static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace blockPartFace, TextureAtlasSprite sprite,
			EnumFacing facing, ModelRotation rotation, boolean uvLocked)
	{
		return makeBakedQuad(blockPart, blockPartFace, sprite, facing, (ITransformation) rotation, uvLocked);
	}

	public static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace blockPartFace, TextureAtlasSprite sprite,
			EnumFacing facing, ITransformation rotation, boolean uvLocked)
	{
		return FACE_BAKERY
				.makeBakedQuad(blockPart.positionFrom, blockPart.positionTo, blockPartFace, sprite, facing, rotation,
						blockPart.partRotation, uvLocked, blockPart.shade);
	}

	public static ModelBlock getModelBlock(ResourceLocation location)
	{
		Reader reader = null;
		IResource iresource = null;
		ModelBlock modelBlock = null;

		try
		{
			iresource = resourceManager.getResource(location);
			reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
			modelBlock = ModelBlock.deserialize(reader);
			modelBlock.name = StringUtils.substringBefore(location.toString(), ".");
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(iresource);
			if (modelBlock == null)
				throw new RuntimeException("load model Block fail!");
		}
		return modelBlock;
	}

	public static int getHexColorARGB(int red, int green, int blue, int alpha)
	{
		return ((alpha * 256 + red) * 256 + green) * 256 + blue;
	}

	/**
	 * 绘制缩放的材质
	 *
	 * @param x                 显示起始位置
	 * @param y                 显示起始位置
	 * @param textureX          材质起始位置
	 * @param textureY          材质起始位置
	 * @param width             显示宽度
	 * @param height            显示高度
	 * @param textureAreaWidth  材质区域宽度（像素点）
	 * @param textureAreaHeight 材质区域高度（像素点）
	 * @param textureWidth      材质文件的总宽度（像素点）
	 * @param textureHeight     材质文件的总高度（像素点）
	 * @param zLevel            显示的图层
	 * @see net.minecraft.client.gui.Gui#drawScaledCustomSizeModalRect(int, int, float, float, int, int, int, int, float, float)
	 */
	public static void drawZoomTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height,
			float textureAreaWidth, float textureAreaHeight, float textureWidth, float textureHeight, float zLevel)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double) x, (double) (y + height), (double) zLevel)
				.tex((double) (textureX * f), (double) ((textureY + textureAreaHeight) * f1)).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), (double) zLevel)
				.tex((double) ((textureX + textureAreaWidth) * f), (double) ((textureY + textureAreaHeight) * f1))
				.endVertex();
		vertexbuffer.pos((double) (x + width), (double) y, (double) zLevel)
				.tex((double) ((textureX + textureAreaWidth) * f), (double) (textureY * f1)).endVertex();
		vertexbuffer.pos((double) x, (double) y, (double) zLevel).tex((double) (textureX * f), (double) (textureY * f1))
				.endVertex();
		tessellator.draw();
	}

	/**
	 * 绘制表格线条
	 *
	 * @param startX
	 * @param startY
	 * @param row
	 * @param column
	 * @param width
	 * @param height
	 * @param lineWidth
	 * @param lineColor
	 */
	public static void drawTableLine(int startX, int startY, int row, int column, int width, int height, int lineWidth,
			OpenGLColor lineColor)
	{
		int remainW = width % column, remainH = height % row;
		/*if (remainW != 0 && remainW != lineWidth)
			throw new RuntimeException("error arg width!");
		if (remainH !=0 && remainH != lineWidth)
			throw new RuntimeException("error arg height!");*/
		width -= remainW;
		height -= remainH;
		GlStateManager.disableTexture2D();
		lineColor.setColor();
		glLineWidth(lineWidth);
		int tempWidth = width / column, tempHeight = height / row;
		glBegin(GL_LINES);
		for (int i = 0; i <= row; i++)
		{
			glVertex2i(startX, startY + i * tempHeight);
			glVertex2i(startX + width, startY + i * tempHeight);
		}
		for (int j = 0; j <= column; j++)
		{
			glVertex2i(startX + j * tempWidth, startY);
			glVertex2i(startX + j * tempWidth, startY + height);
		}
		glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	/**
	 * 绘制表格矩形
	 *
	 * @param startX
	 * @param startY
	 * @param row
	 * @param column
	 * @param width
	 * @param height
	 * @param lineWidth
	 * @param lineColor
	 * @param backgroundColor
	 */
	public static void drawTableRectangle(int startX, int startY, int row, int column, int width, int height,
			int lineWidth, OpenGLColor lineColor, OpenGLColor backgroundColor)
	{
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();//顶点顺序不一致需要关闭culling
		lineColor.setColor();
		glRecti(startX, startY, startX + width, startY + height);
		int slotWidth = (width - lineWidth * (column + 1)) / column, slotHeight =
				(height - lineWidth * (row + 1)) / row, tempWidth = slotWidth + lineWidth, tempHeight =
				slotHeight + lineWidth, startXOffset = startX + lineWidth, startYOffset = startY + lineWidth;
		backgroundColor.setColor();
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < column; j++)
			{
				glRecti(startXOffset + j * tempWidth, startYOffset + i * tempHeight,
						startXOffset + j * tempWidth + slotWidth, startYOffset + i * tempHeight + slotHeight);
			}
		}
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawTableWithBackground(int startX, int startY, int row, int column, int width, int height,
			int lineWidth, OpenGLColor lineColor, OpenGLColor backgroundColor)
	{
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		backgroundColor.setColor();
		glRecti(startX, startY, startX + width, startY + height);
		drawTableLine(startX, startY, row, column, width, height, lineWidth, lineColor);
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawTableLineUseGui(int startX, int startY, int row, int column, int width, int height,
			int lineWidth, int lineColor, int zlevel)
	{
		int tempWidth = width / column, tempHeight = height / row;
		for (int i = 0; i <= row; i++)
			drawRectangle(startX, startY + i * tempHeight, startX + width + lineWidth,
					startY + i * tempHeight + lineWidth, lineColor, zlevel);
		for (int j = 0; j <= column; j++)
			drawRectangle(startX + j * tempWidth, startY, startX + j * tempWidth + lineWidth,
					startY + height + lineWidth, lineColor, zlevel);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawRectangle(int left, int top, int right, int bottom, int color, double zLevel)
	{
		if (left < right)
		{
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			int j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos((double) left, (double) bottom, zLevel).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, zLevel).endVertex();
		vertexbuffer.pos((double) right, (double) top, zLevel).endVertex();
		vertexbuffer.pos((double) left, (double) top, zLevel).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawString(FontRenderer fontRendererIn, String text, int x, int y, float size, int color)
	{
		GlStateManager.scale(size, size, size);
		float mSize = 1.0f / size;
		fontRendererIn.drawString(text, Math.round(x / size), Math.round(y / size), color);
		GlStateManager.scale(mSize, mSize, mSize);
	}

	public static void drawCenteredString(FontRenderer fontRendererIn, String text, int xSize, int y, float size, int color)
	{
		float length = fontRendererIn.getStringWidth(text);
		int offsetX = Math.round(xSize - Math.round(length / 2 * size));
		drawString(fontRendererIn, text, offsetX, y, size, color);
	}

	public static class OpenGLColor
	{
		private float red, green, blue, alpha;

		public OpenGLColor(float red, float green, float blue, float alpha)
		{
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}

		public OpenGLColor(int red, int green, int blue, int alpha)
		{
			this.red = (red & 255) / 255F;
			this.green = (green & 255) / 255F;
			this.blue = (blue & 255) / 255F;
			this.alpha = (alpha & 255) / 255F;
		}

		/**
		 * 用作ARGB模式转换
		 *
		 * @param color 顺序为alpha,red,green,blue
		 */
		public OpenGLColor(int color)
		{
			this.red = (color >> 16 & 255) / 255F;
			this.green = (color >> 8 & 255) / 255F;
			this.blue = (color & 255) / 255F;
			this.alpha = (color >> 24 & 255) / 255F;
		}

		public void setColor()
		{
			GlStateManager.color(red, green, blue, alpha);
		}

		public int getHexColor()
		{
			return ((Math.round(this.alpha * 255) * 256 + Math.round(this.red * 255)) * 256 + Math
					.round(this.green * 255)) * 256 + Math.round(this.blue * 255);
		}
	}

}
