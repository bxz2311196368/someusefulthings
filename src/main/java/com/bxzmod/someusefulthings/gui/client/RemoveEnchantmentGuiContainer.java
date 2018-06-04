package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.gui.server.RemoveEnchantmentContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RemoveEnchantmentGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/RemoveEnchantment.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

	RemoveEnchantmentContainer inventory;

	int totalWorkTime;

	public RemoveEnchantmentGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 133;
		this.inventory = (RemoveEnchantmentContainer) inventorySlotsIn;
		this.totalWorkTime = this.inventory.getTotalWorkTime();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

		int workTime = this.inventory.getWorkTime();
		int textureWidth = 1 + (int) Math.ceil(22.0 * workTime / this.totalWorkTime);
		this.drawTexturedModalRect(offsetX + 76, offsetY + 20, 0, 133, textureWidth, 17);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = I18n.format("tile.removeEnchantment.name");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6,
				0x404040);

	}

}
