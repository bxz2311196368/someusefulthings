package com.bxzmod.someusefulthings.blocks.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BXZModelBlock implements IModel
{
	private IModel mainModel;
	private Map<String, IModel> models = Maps.newHashMap();
	private Set<ResourceLocation> dependencies = Sets.newHashSet(), textures = Sets.newHashSet();

	public BXZModelBlock(IModel mainModel, Map<String, IModel> model, Collection<ResourceLocation> textures)
	{
		this.mainModel = mainModel;
		this.models.putAll(model);
		for (IModel iModel : this.models.values())
		{
			this.dependencies.addAll(iModel.getDependencies());
			this.textures.addAll(textures);
		}
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableSet.copyOf(this.dependencies);
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableSet.copyOf(this.textures);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
		Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		Map<String, IBakedModel> iBakedModels = Maps.newHashMap();
		for (Map.Entry<String, IModel> entry : this.models.entrySet())
			iBakedModels.put(entry.getKey(), entry.getValue().bake(state, format, bakedTextureGetter));
		return new BXZBakedModelBlock(this.mainModel.bake(state, format, bakedTextureGetter), iBakedModels);
	}

	@Override
	public IModelState getDefaultState()
	{
		return this.mainModel.getDefaultState();
	}
}
