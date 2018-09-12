package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.client.BXZRenderHelper;
import com.bxzmod.someusefulthings.gui.server.TestContainer;
import net.minecraft.client.gui.inventory.GuiContainer;

public class TestGuiContainer extends GuiContainer
{
	public TestGuiContainer(TestContainer inventorySlotsIn)
	{
		super(inventorySlotsIn);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{

	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		/*BXZRenderHelper.drawTableLine(240,50,2,2,2*32, 2*27,3,new BXZRenderHelper.OpenGLColor(255,0,0,255));
		BXZRenderHelper.drawTableRectangle(50,50,4,5,5*32 + 2, 4*27+2,2,new BXZRenderHelper.OpenGLColor(255,0,0,255), new BXZRenderHelper.OpenGLColor(0,255,0,255));*/
		BXZRenderHelper.drawTableWithBackground(50,50,4,5,5*32, 4*27,3,new BXZRenderHelper.OpenGLColor(128,128,128,255), new BXZRenderHelper.OpenGLColor(0,0,0,255));
	}
}
