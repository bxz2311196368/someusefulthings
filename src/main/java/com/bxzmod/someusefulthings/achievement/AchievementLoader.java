package com.bxzmod.someusefulthings.achievement;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.items.ItemLoader;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class AchievementLoader
{
	public static final AchievementPage PAGE = new AchievementPage(Info.MODNAME);

	public static Achievement kill_creative;
	public static Achievement build_ring;
	public static Achievement build_tool;
	public static Achievement build_backpack;
	public static Achievement build_garbagebag;

	public AchievementLoader(FMLInitializationEvent event)
	{
		build_backpack = addAchievement("build_backpack", 0, 0, ItemLoader.portableInventoryItem, null, false);
		build_garbagebag = addAchievement("build_garbagebag", 1, 0, ItemLoader.garbagebag, null, false);
		build_tool = addAchievement("build_tool", 2, 0, ItemLoader.limitlesstool, null, false);
		build_ring = addAchievement("build_ring", 0, 1, ItemLoader.invinciblering, null, true);
		kill_creative = addAchievement("kill_creative", 0, 2, ItemLoader.artifactsword, null, true);
		AchievementPage.registerAchievementPage(PAGE);
	}

	private static Achievement addAchievement(String name, int column, int row, Item item, Achievement parent,
			boolean isSpecial)
	{
		Achievement achievement = new Achievement(Info.MODID + ".achievement." + name, Info.MODID + "." + name, column,
				row, item, parent);
		PAGE.getAchievements().add(isSpecial ? achievement.setSpecial().registerStat() : achievement.registerStat());
		return achievement;
	}

}
