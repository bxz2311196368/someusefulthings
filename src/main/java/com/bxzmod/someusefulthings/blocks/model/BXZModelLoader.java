package com.bxzmod.someusefulthings.blocks.model;

import com.bxzmod.someusefulthings.ModInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BXZModelLoader implements ICustomModelLoader
{
	public static BXZModelLoader INSTANCE;
	private HashMap<ResourceLocation, IModel> modelCache = Maps.newHashMap();
	private ModelLoader loader;
	private Method method;
	private Field base, parts;
	public static List<String> customModelBlock = Lists.newArrayList();

	public BXZModelLoader(FMLPreInitializationEvent event)
	{
		INSTANCE = this;
		ModelLoaderRegistry.registerLoader(INSTANCE);
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		if (modelLocation.getResourceDomain().equalsIgnoreCase(ModInfo.MODID))
		{
			if (modelLocation instanceof ModelResourceLocation)
			{
				ModelResourceLocation location = (ModelResourceLocation) modelLocation;
				return location.getVariant().contains("normal") && customModelBlock
					.contains(modelLocation.getResourcePath());
			}
		}
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		if (loader == null)
			this.setModelLoader();
		IModel model = null;
		if (modelCache.containsKey(modelLocation))
			model = modelCache.get(modelLocation);
		else
		{
			model = this.getModel(modelLocation);
			modelCache.put(modelLocation, model);
		}
		return model;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{

	}

	private IModel getModel(ResourceLocation modelLocation) throws Exception
	{
		IModel model = null;
		try
		{
			ModelBlockDefinition modelBlockDefinition = (ModelBlockDefinition) this.method
				.invoke(loader, modelLocation);
			IModel mainModel = null;
			Map<String, IModel> models = Maps.newHashMap();
			Set<ResourceLocation> textures = Sets.newHashSet();
			List<Variant> variantList = modelBlockDefinition
				.getVariant(((ModelResourceLocation) modelLocation).getVariant()).getVariantList();
			for (Variant variant : variantList)
			{
				IModel m = variant.process(ModelLoaderRegistry.getModel(variant.getModelLocation()));
				if (m instanceof MultiModel)
				{
					try
					{
						mainModel = (IModel) this.base.get(m);
						Map<String, Pair<IModel, IModelState>> submodels = (Map<String, Pair<IModel, IModelState>>) this.parts
							.get(m);
						for (Map.Entry<String, Pair<IModel, IModelState>> entry : submodels.entrySet())
							models.put(entry.getKey(), entry.getValue().getLeft());
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				textures.addAll(m.getTextures());
			}
			model = new BXZModelBlock(mainModel, models, textures);
		} catch (RuntimeException e)
		{
			e.printStackTrace();
		} finally
		{
			if (model == null)
				throw new RuntimeException("can't load model");
		}
		return model;
	}

	private void setModelLoader()
	{
		try
		{
			for (Class clazz : ModelLoader.class.getDeclaredClasses())
			{
				if (clazz.getName().contains("VanillaLoader"))
				{
					Field field = clazz.getDeclaredField("loader"), field1 = clazz.getDeclaredField("INSTANCE");
					field.setAccessible(true);
					field1.setAccessible(true);
					loader = (ModelLoader) field.get(field1.get(null));
					method = ModelLoader.class.getDeclaredMethod("getModelBlockDefinition", ResourceLocation.class);
					method.setAccessible(true);
				}
			}
			this.base = MultiModel.class.getDeclaredField("base");
			this.parts = MultiModel.class.getDeclaredField("parts");
			this.base.setAccessible(true);
			this.parts.setAccessible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (loader == null)
				throw new RuntimeException("can't find ModelLoader");
		}
	}

}
