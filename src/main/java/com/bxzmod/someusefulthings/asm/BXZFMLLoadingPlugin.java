package com.bxzmod.someusefulthings.asm;

import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.security.CodeSource;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.10.2")
@IFMLLoadingPlugin.TransformerExclusions({ "com.bxzmod.someusefulthings.asm" })
public class BXZFMLLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean deobf = false;

	/*
	static
	{
		CodeSource src = BXZFMLLoadingPlugin.class.getProtectionDomain().getCodeSource();
		if (src != null)
		{
			URL jar = src.getLocation();
			String name = StringUtils.substringAfterLast(jar.getFile(), "/");
			int i = 0;
			for (String s : CoreModManager.getIgnoredMods())
			{
				if (s.equals(name))
				{
					CoreModManager.getIgnoredMods().remove(i);
					break;
				}
				i++;
			}

		}
	}
	*/

	@Override
	public String[] getASMTransformerClass()
	{

		return new String[] { "com.bxzmod.someusefulthings.asm.ItemBucketClassTransformer" };
	}

	@Override
	public String getModContainerClass()
	{
		return "com.bxzmod.someusefulthings.asm.BXZASMModContainer";
	}

	@Override
	public String getSetupClass()
	{

		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		deobf = !(boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass()
	{

		return null;
	}

}
