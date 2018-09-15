package com.bxzmod.someusefulthings.asm;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Arrays;

public class BXZASMModContainer extends DummyModContainer
{

	public BXZASMModContainer()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "bxzasm";
		meta.name = "BXZ ASM";
		meta.description = "Fix bucket";
		meta.version = "1.10.2-1.0";
		meta.authorList = Arrays.asList("BXZ");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

}
