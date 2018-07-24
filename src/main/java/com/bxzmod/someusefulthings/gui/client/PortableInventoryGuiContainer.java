package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.gui.GuiButtonBackPack;
import com.bxzmod.someusefulthings.gui.server.PortableInventoryContainer;
import com.bxzmod.someusefulthings.items.ItemLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class PortableInventoryGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/PortableInventory.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private PortableInventoryContainer gui;
	private int meta;

	public PortableInventoryGuiContainer(Container inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 222;
		this.gui = (PortableInventoryContainer) inventorySlotsIn;
		this.meta = gui.getPlayer().getCapability(CapabilityLoader.PORTABLE_INVENTORY, null).getOpen();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = I18n.format("item.portableInventoryItem." + EnumDyeColor.byMetadata(meta).toString() + ".name");
		this.fontRendererObj.drawString(title, 8, 5, 0x404040);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		for (int i = 0; i < 16; i++)
		{
			if (i < 8)
				this.buttonList.add(new GuiButtonBackPack(i, offsetX - 40, offsetY + 18 + i * 20, 40, 20,
						StringUtils.substringBetween(
								new ItemStack(ItemLoader.portableInventoryItem, 1, i).getDisplayName(), "(", ")"),
						i != meta));
			else
				this.buttonList.add(new GuiButtonBackPack(i, offsetX + this.xSize, offsetY + 18 + (i - 8) * 20, 40, 20,
						StringUtils.substringBetween(
								new ItemStack(ItemLoader.portableInventoryItem, 1, i).getDisplayName(), "(", ")"),
						i != meta));

		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled && button.id >= 0 && button.id < 16)
		{
			gui.getPlayer().getCapability(CapabilityLoader.PORTABLE_INVENTORY, null).setOpen(button.id);
			gui.setReOpen();
			gui.syncData(gui.getPlayer());
		}
		super.actionPerformed(button);
	}

}
