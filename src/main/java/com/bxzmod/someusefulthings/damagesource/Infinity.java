package com.bxzmod.someusefulthings.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class Infinity extends EntityDamageSource
{
	public Entity damageSourceEntity;

	public Infinity(Entity damageSourceEntityIn)
	{
		super("infinity", damageSourceEntityIn);

	}

	@Override
	public boolean isDifficultyScaled()
	{
		return false;
	}

}
