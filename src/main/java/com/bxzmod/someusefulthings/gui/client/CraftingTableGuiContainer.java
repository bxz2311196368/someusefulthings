package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.gui.server.CraftingTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;

public class CraftingTableGuiContainer extends GuiContainer
{
	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation(
			"textures/gui/container/crafting_table.png");
	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/CraftingTable.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/spectator_widgets.png");

	CraftingTableContainer gui;

	private static final int BUTTON_ENABLE = 0;
	private static final int BUTTON_OUTPUT = 1;
	private static final int BUTTON_LOCK = 2;

	public CraftingTableGuiContainer(CraftingTableContainer inventorySlotsIn)
	{
		super(inventorySlotsIn);
		gui = inventorySlotsIn;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if (gui.isTable())
		{
			this.fontRendererObj.drawString(I18n
					.format(gui.isAuto() ? "gui.craftTable.enable" : "gui.craftTable.disable")
					+ "  "
					+ I18n.format(gui.getTe().isStayInSlot() ? "gui.craftTable.Output.enable"
							: "gui.craftTable.Output.disable")
					+ "  "
					+ I18n.format(
							gui.getTe().isLockRecipe() ? "gui.craftTable.Lock.enable" : "gui.craftTable.Lock.disable"),
					7, 73, 4210752);
			if (gui.getTe().cache_stack != null)
			{
				this.fontRendererObj.drawString(I18n.format("gui.craftTable.CraftingItem"), 97, 16, 4210752);
				this.itemRender.renderItemAndEffectIntoGUI(gui.getTe().cache_stack, 133, 16);
			}
		}
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (gui.isTable())
			this.mc.getTextureManager().bindTexture(TEXTURE);
		else
			this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		if (!gui.isTable())
			return;
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_ENABLE, offsetX + 63, offsetY + 57, 16, 16, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

					if (gui.isAuto())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 207, 0, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 191, 0, this.width, this.height);
					}
				}
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_OUTPUT, offsetX + 81, offsetY + 57, 16, 16, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURE);

					if (gui.getTe().isStayInSlot())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 128, 0, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 163, 0, this.width, this.height);
					}
				}
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_LOCK, offsetX + 63, offsetY + 17, 18, 18, "")
		{
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

					if (gui.getTe().isLockRecipe())
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 1, 147, this.width, this.height);
					} else
					{
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 21, 147, this.width, this.height);
					}
				}
			}
		});
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id)
		{
		case BUTTON_ENABLE:
			gui.setAuto(!gui.isAuto());
			gui.syncData();
			break;
		case BUTTON_OUTPUT:
			gui.getTe().setStayInSlot(!gui.getTe().isStayInSlot());
			gui.syncData();
			break;
		case BUTTON_LOCK:
			gui.getTe().setLockRecipe(!gui.getTe().isLockRecipe());
			gui.syncData();
			break;
		}

		super.actionPerformed(button);
	}

}
