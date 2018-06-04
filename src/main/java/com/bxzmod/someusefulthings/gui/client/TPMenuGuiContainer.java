package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.Info;
import com.bxzmod.someusefulthings.capability.CapabilityLoader;
import com.bxzmod.someusefulthings.capability.ITPLocation;
import com.bxzmod.someusefulthings.capability.ITPLocation.BlockPosWithDimAndName;
import com.bxzmod.someusefulthings.gui.server.TPMenuContainer;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class TPMenuGuiContainer extends GuiContainer
{
	private static final String TEXTURE_PATH = Info.MODID + ":" + "textures/gui/container/TPMenu.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	public static final ResourceLocation TEXTURES_B = new ResourceLocation(
			Info.MODID + ":" + "textures/gui/container/button.png");

	private GuiTextField nameField;

	private TPMenuContainer gui;

	private int button = 0, select = -1, page = 1;

	private String tip = "", info1 = "", info2 = "";

	private ITPLocation cp;

	private GuiButtonMe newPos, rename, teleport, delete, pre_page, next_page;

	public TPMenuGuiContainer(TPMenuContainer inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.xSize = 256;
		this.ySize = 256;
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;
		this.gui = inventorySlotsIn;
		this.cp = gui.player.getCapability(CapabilityLoader.TP_MENU, null);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (tip != null && !tip.isEmpty())
		{
			this.fontRendererObj.drawString(tip, (this.xSize - this.fontRendererObj.getStringWidth(tip)) / 2, 230,
					0x404040);
		} else
		{
			if (select >= 0)
			{
				this.fontRendererObj.drawString(info1, (this.xSize - this.fontRendererObj.getStringWidth(info1)) / 2,
						225, 0x404040);
				this.fontRendererObj.drawString(info2, (this.xSize - this.fontRendererObj.getStringWidth(info2)) / 2,
						238, 0x404040);
			}
		}
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
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		Keyboard.enableRepeatEvents(true);
		this.nameField = new GuiTextField(0, this.fontRendererObj, offsetX + 21, offsetY + 173, 103, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(0xDC143C);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(30);
		this.nameField.setEnabled(true);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				this.addButton(new GuiButtonMe(i * 5 + j, offsetX + 18 + j * 45, offsetY + 31 + i * 25, 40, 20, ""));
		this.newPos = this
				.addButton(new GuiButtonMe(25, offsetX + 153, offsetY + 168, 40, 20, I18n.format("gui.tp.button.new")));
		this.rename = this.addButton(
				new GuiButtonMe(26, offsetX + 198, offsetY + 168, 40, 20, I18n.format("gui.tp.button.rename")));
		this.teleport = this.addButton(
				new GuiButtonMe(27, offsetX + 18, offsetY + 199, 40, 20, I18n.format("gui.tp.button.teleport")));
		this.delete = this.addButton(
				new GuiButtonMe(28, offsetX + 63, offsetY + 199, 40, 20, I18n.format("gui.tp.button.delete")));
		this.pre_page = this.addButton(
				new GuiButtonMe(29, offsetX + 153, offsetY + 199, 40, 20, I18n.format("gui.tp.button.previous")));
		this.next_page = this.addButton(
				new GuiButtonMe(30, offsetX + 198, offsetY + 199, 40, 20, I18n.format("gui.tp.button.next")));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (!this.nameField.textboxKeyTyped(typedChar, keyCode))
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id >= 0 && button.id <= 24)
		{
			select = button.id;
			BlockPosWithDimAndName pos = cp.getPos(button.id - 25 + page * 25);
			tip = "";
			info1 = I18n.format("bxz.tpPos.detailedInformation") + I18n.format("bxz.tpPos.name") + pos.name;
			info2 = I18n.format("bxz.tpPos.dim") + pos.dim + " X:" + pos.pos.getX() + ",Y:" + pos.pos.getY() + ",Z:"
					+ pos.pos.getZ();
			return;
		}
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		if (button.id > 25 && button.id < 29 && select < 0)
		{
			tip = I18n.format("gui.tp.tip.needSelect", TextFormatting.RED);
			return;
		}
		tip = "";
		switch (button.id)
		{
		case 25:
			if (this.nameField.getText() == null || this.nameField.getText().equals(""))
			{
				tip = I18n.format("gui.tp.tip.needName", TextFormatting.RED);
				return;
			}
			message.nbt.setBoolean("new", true);
			message.nbt.setString("PosName", this.nameField.getText());
			break;
		case 26:
			if (this.nameField.getText() == null || this.nameField.getText().equals(""))
			{
				tip = I18n.format("gui.tp.tip.needName", TextFormatting.RED);
				return;
			}
			message.nbt.setBoolean("Rename", true);
			message.nbt.setString("PosRename", this.nameField.getText());
			break;
		case 27:
			message.nbt.setBoolean("Teleport", true);
			break;
		case 28:
			message.nbt.setBoolean("Remove", true);
			cp.delPos(select + (page - 1) * 25);
			if (page > 1 && cp.getPosAmount() + 25 - page * 25 <= 0)
				page--;
			break;
		case 29:
			if (page > 1)
			{
				page--;
				this.select = -1;
			}
			return;
		case 30:
			if (cp.getPosAmount() > page * 25)
			{
				page++;
				this.select = -1;
			}
			return;
		}
		message.nbt.setInteger("Select", select);
		message.nbt.setString("Type", "TP");
		message.nbt.setString("Player", gui.player.getName());
		message.nbt.setInteger("Button", button.id);
		message.nbt.setInteger("Page", page);
		NetworkLoader.instance.sendToServer(message);
		this.select = -1;
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.nameField.drawTextBox();
	}

	public class GuiButtonMe extends GuiButton
	{

		public GuiButtonMe(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
		{
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			this.visible = cp.getPosAmount() + 25 - page * 25 > this.id || this.id > 24;
			if (this.visible)
			{
				FontRenderer fontrenderer = mc.fontRendererObj;
				mc.getTextureManager().bindTexture(TEXTURES_B);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				if (select == this.id)
					this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 40, this.width, this.height);
				else
				{
					if (id == 29)
					{
						if (page <= 1)
						{
							this.enabled = false;
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
						} else
						{
							this.enabled = true;
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 20, this.width, this.height);
						}
					} else if (id == 30)
					{
						if (page * 25 >= cp.getPosAmount())
						{
							this.enabled = false;
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
						} else
						{
							this.enabled = true;
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 20, this.width, this.height);
						}
					} else
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 20, this.width, this.height);
				}

				int j = 14737632;

				if (packedFGColour != 0)
				{
					j = packedFGColour;
				} else if (!this.enabled)
				{
					j = 10526880;
				} else if (this.hovered)
				{
					j = 16777120;
				}

				if (id > 24)
					this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
							this.yPosition + (this.height - 8) / 2, j);
				else
				{
					this.displayString = cp.getName(this.id - 25 + page * 25);
					double lenth = fontrenderer.getStringWidth(this.displayString) <= 36 ? this.displayString.length()
							: 36.0D / fontrenderer.getStringWidth(this.displayString) * this.displayString.length();
					String temp = this.displayString.substring(0, MathHelper.floor(lenth));
					this.drawCenteredString(fontrenderer, temp, this.xPosition + this.width / 2,
							this.yPosition + (this.height - 8) / 2, j);
				}
			}
		}

	}

}
