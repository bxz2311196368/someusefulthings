package com.bxzmod.someusefulthings.blocks.model;

import com.bxzmod.someusefulthings.blocks.property.BlockPropertys;
import com.bxzmod.someusefulthings.blocks.property.EnumIO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BXZBakedModelBlock implements IBakedModel
{
	private IBakedModel mainBakedModel;
	private Map<String, IBakedModel> bakedModels = Maps.newHashMap();
	private Map<EnumIO, IBakedModel> ioConfig = Maps.newHashMap();
	private boolean isContorlModel = false;

	/**
	 * @param mainBakedModel 主要的模型放在第一个，显示在最底层。
	 * @param bakedModel     对应的不同次要模型
	 */
	public BXZBakedModelBlock(IBakedModel mainBakedModel, Map<String, IBakedModel> bakedModel)
	{
		this.mainBakedModel = mainBakedModel;
		this.bakedModels.putAll(bakedModel);
		this.initIOConfig();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		if (state == null || !(state instanceof IExtendedBlockState))
			return Collections.emptyList();
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
		List<BakedQuad> quads = Lists.newArrayList();
		EnumFacing facing = extendedBlockState.getValue(BlockPropertys.FACING);
		quads.addAll(this.reBuild(state, side, rand, facing));
		if (this.isContorlModel)
		{
			if (side != null)
			{
				EnumIO io = extendedBlockState.getValue(BlockPropertys.getPropertyIOFromFacing(side));
				if (this.ioConfig.containsKey(io))
					quads.addAll(this.ioConfig.get(io).getQuads(state, side, rand));
			}
		} else
		{
			for (IBakedModel m : this.bakedModels.values())
				quads.addAll(m.getQuads(state, side, rand));
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return this.mainBakedModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return this.mainBakedModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return this.mainBakedModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return this.mainBakedModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return this.mainBakedModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return this.mainBakedModel.getOverrides();
	}

	private void initIOConfig()
	{
		for (Map.Entry<String, IBakedModel> entry : this.bakedModels.entrySet())
			if (EnumIO.isEnumIO(entry.getKey()))
				this.ioConfig.put(EnumIO.getIOByName(entry.getKey()), entry.getValue());
		if (!this.ioConfig.isEmpty())
			this.isContorlModel = true;
	}

	private List<BakedQuad> reBuild(IBlockState state, EnumFacing side, long rand, EnumFacing facing)
	{
		if (side == null)
			return this.mainBakedModel.getQuads(state, null, rand);
		List<BakedQuad> reBuild = Lists.newArrayList();
		EnumFacing sideCache = side;
		if (side.getHorizontalIndex() > -1)
			for (EnumFacing rotateTemp = facing; rotateTemp != EnumFacing.NORTH; rotateTemp = rotateTemp.rotateYCCW())
				side = side.rotateYCCW();
		List<BakedQuad> quads = this.mainBakedModel.getQuads(state, sideCache, rand);
		List<BakedQuad> quadsGetSprite = this.mainBakedModel.getQuads(state, side, rand);
		if (quads.isEmpty() || quadsGetSprite.isEmpty() || quads.size() != quadsGetSprite.size())
			return Collections.emptyList();
		for (int i = 0; i < quads.size(); i++)
		{
			BakedQuadRetextured bakedQuadRetextured = new BakedQuadRetextured(quads.get(i),
				quadsGetSprite.get(i).sprite);
			reBuild.add(bakedQuadRetextured);
		}
		return reBuild;
	}

}
