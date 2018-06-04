package com.bxzmod.someusefulthings.throwable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class InfinityArrow extends EntityArrow
{
	private int ticksInGround;
	private EntityLivingBase shootingEntity;
	private int time = 0;

	public InfinityArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);

	}

	public InfinityArrow(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn, shooter);
		this.shootingEntity = shooter;

	}

	public InfinityArrow(World worldIn)
	{
		super(worldIn);

	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
	{
		if (!this.world.isRemote)
		{
			if (raytraceResultIn.entityHit != null)
			{
				EntityLivingBase e;
				if (raytraceResultIn.entityHit instanceof EntityLivingBase)
					e = (EntityLivingBase) raytraceResultIn.entityHit;
				else if (raytraceResultIn.entityHit instanceof EntityDragonPart)
					e = (EntityLivingBase) ((EntityDragonPart) raytraceResultIn.entityHit).entityDragonObj;
				else
					return;
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityPlayer)
				{
					EntityPlayer p = (EntityPlayer) this.shootingEntity;
					if (!e.isDead && e.getHealth() > 0)
					{
						raytraceResultIn.entityHit.attackEntityFrom(new EntityDamageSource("infinity", p),
								e.getHealth());
						e.setHealth(0);
						e.onDeath(new EntityDamageSource("infinity", p));
					}
				}
			}
		}
		this.setDead();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.ticksExisted > 360)
			this.setDead();

	}

	@Override
	protected ItemStack getArrowStack()
	{
		return new ItemStack(Items.SNOWBALL, 64, 0);
	}

	@Override
	public boolean hasNoGravity()
	{
		return true;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.world.isRemote && this.inGround)
		{
			this.setDead();
		}
	}

}