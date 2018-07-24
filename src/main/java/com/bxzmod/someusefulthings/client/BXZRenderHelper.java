package com.bxzmod.someusefulthings.client;

import com.google.common.base.Charsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.ITransformation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;

public class BXZRenderHelper
{
	public static final FaceBakery FACE_BAKERY = new FaceBakery();

	public static final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

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
				throw new RuntimeException("load Model Block fail!");
		}
		return modelBlock;
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
	 */
	public static void drawZoomTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height,
		float textureAreaWidth, float textureAreaHeight, float textureWidth, float textureHeight, float zLevel)
	{
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

}
