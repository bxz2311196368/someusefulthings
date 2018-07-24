package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.EnergyBlockContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.text.DecimalFormat;

public class EnergyBlockGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/EnergyBlock.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	public EnergyBlockGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn, "energyBlock", TEXTURE, 176, 133);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		double energy = ((EnergyBlockContainer) this.inventorySlots).getTotalEnergy() / 2.5D;
		DecimalFormat decimalFormat = new DecimalFormat("0.000"), decimalFormat1 = new DecimalFormat("0.###E0");
		String i18n = I18n.format("gui.energyBlock.totalEnergy"), num_format =
			energy > 1000000000 ? decimalFormat1.format(energy) : decimalFormat.format(energy);
		String rf = i18n + ": " + num_format + "RF";
		this.fontRendererObj.drawString(rf, 6, 40, 0x404040);
	}
}
