package com.bxzmod.someusefulthings.blocks.Model;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

public class BakedQuadRebuild extends BakedQuad
{
	public BakedQuadRebuild(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn,
		boolean applyDiffuseLighting, VertexFormat format)
	{
		super(vertexDataIn, tintIndexIn, faceIn, spriteIn, applyDiffuseLighting, format);
	}

	public BakedQuadRebuild(BakedQuad bakedQuadIn)
	{
		this(Arrays.copyOf(bakedQuadIn.vertexData, 28), bakedQuadIn.tintIndex, bakedQuadIn.face, bakedQuadIn.sprite,
			bakedQuadIn.shouldApplyDiffuseLighting(), bakedQuadIn.getFormat());
	}

	public BakedQuadRebuild setPosition(Vector3f from, Vector3f to, EnumFacing facing)
	{
		float[] position = this.getPositionsToFloat(from, to);
		for (int i = 0; i < 4; ++i)
		{
			EnumFaceDirection.VertexInformation information = EnumFaceDirection.getFacing(facing)
				.getVertexInformation(i);
			vertexData[i * 7] = Float.floatToRawIntBits(position[information.xIndex]);
			vertexData[i * 7 + 1] = Float.floatToRawIntBits(position[information.yIndex]);
			vertexData[i * 7 + 2] = Float.floatToRawIntBits(position[information.zIndex]);
		}
		return this.setFacing(facing);
	}

	public BakedQuadRebuild setTexture(TextureAtlasSprite spriteIn)
	{
		this.sprite = spriteIn;
		for (int i = 0; i < 4; ++i)
		{
			int j = format.getIntegerSize() * i;
			int uvIndex = format.getUvOffsetById(0) / 4;
			this.vertexData[j + uvIndex] = Float.floatToRawIntBits(this.sprite.getInterpolatedU(
				(double) this.sprite.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[j + uvIndex]))));
			this.vertexData[j + uvIndex + 1] = Float.floatToRawIntBits(this.sprite.getInterpolatedV(
				(double) this.sprite.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[j + uvIndex + 1]))));
		}
		return this;
	}

	private BakedQuadRebuild setFacing(EnumFacing facing)
	{
		this.face = facing;
		return this;
	}

	private float[] getPositionsToFloat(Vector3f pos1, Vector3f pos2)
	{
		float[] afloat = new float[EnumFacing.values().length];
		afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x;
		afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y;
		afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z;
		afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x;
		afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y;
		afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z;
		return afloat;
	}

	/**
	 * 按Y轴旋转Quad
	 *
	 * @param times 旋转次数
	 * @return 旋转完成的Quad
	 */
	public BakedQuadRebuild rotateQuadY(int times)
	{
		for (; times > 0; times--)
			this.rotateQuadY();
		return this;
	}

	private void rotateQuadY()
	{
		if (this.face.getHorizontalIndex() > -1)
		{
			for (int i = 0; i < 4; ++i)
			{
				float z = Float.intBitsToFloat(vertexData[i * 7 + 2]);
				vertexData[i * 7 + 2] = vertexData[i * 7];
				vertexData[i * 7] = Float.floatToRawIntBits(1.0F - z);
			}
			this.face = this.face.rotateY();
		}
	}
}
