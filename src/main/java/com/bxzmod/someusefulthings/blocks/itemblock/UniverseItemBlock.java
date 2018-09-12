package com.bxzmod.someusefulthings.blocks.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class UniverseItemBlock extends ItemBlock
{

	public UniverseItemBlock(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setRegistryName(block.getRegistryName());
	}

	@Override
	public int getMetadata(int metadata)
	{
		return metadata;
	}

}
