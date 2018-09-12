package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.bxzmod.someusefulthings.client.BXZRenderHelper.*;

public class BaseGuiListString extends GuiSlot
{
	public static final ResourceLocation BUTTON = new ResourceLocation(
			ModInfo.MODID + ":" + "textures/gui/container/button.png");

	protected int posX;
	protected int posY;
	protected int slotWidth;
	protected int slotInRow;
	protected int slotInColum;
	protected int scrollWidth = 6;
	protected int lineWidth;
	protected BaseGuiContainer gui;
	protected List<String> list;
	protected String listHead;

	public BaseGuiListString(Minecraft mcIn, String listHead, int posX, int posY, int headerHeight, int slotWidth,
			int slotHeight, int slotInRow, int slotInColum, int lineWidth, BaseGuiContainer guiIn, List<String> listIn)
	{
		this(mcIn, listHead, posX, posY, headerHeight, slotWidth, slotHeight, slotInRow, slotInColum, lineWidth, 12 + lineWidth * 2,
				guiIn, listIn);
	}

	/**
	 * 用于在GUI中显示一个列表
	 *
	 * @param mcIn         Minecraft实例，一般传入GuiContainer.mc
	 * @param listHead     列表头
	 * @param posX         列表起始X坐标
	 * @param posY         列表起始Y坐标
	 * @param headerHeight 列表头部高度
	 * @param slotWidth    列表每个元素的宽度
	 * @param slotHeight   列表每个元素的高度
	 * @param slotInRow    列表行数
	 * @param slotInColum  列表列数
	 * @param lineWidth    列表边框宽度
	 * @param scrollWidth  滚轴宽度
	 * @param guiIn        该列表所处的GUI实例
	 * @param listIn       列表应显示的字符串list
	 */
	public BaseGuiListString(Minecraft mcIn, String listHead, int posX, int posY, int headerHeight, int slotWidth,
			int slotHeight, int slotInRow, int slotInColum, int lineWidth, int scrollWidth, BaseGuiContainer guiIn,
			List<String> listIn)
	{
		super(mcIn, (lineWidth + slotWidth) * slotInRow + scrollWidth,
				(lineWidth + slotHeight) * slotInColum + headerHeight, 0, headerHeight, slotHeight);
		this.posX = posX;
		this.posY = posY;
		this.slotWidth = slotWidth;
		this.scrollWidth = scrollWidth;
		this.lineWidth = lineWidth;
		this.setHasListHeader(headerHeight != 0, headerHeight);
		this.gui = guiIn;
		this.list = listIn;
		this.slotInRow = slotInRow;
		this.slotInColum = slotInColum;
		this.listHead = listHead;
	}

	@Override
	protected int getSize()
	{
		return this.list.size();
	}

	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
	{
		/*this.gui.guiServer.getBasicTe().setSelectEntity(this.list.get(slotIndex));
		MultiSignSync message = new MultiSignSync();
		message.nbt = new NBTTagCompound();
		message.nbt.setString("Type", "MobSummon");
		message.nbt.setInteger("world", this.mc.world.provider.getDimension());
		message.nbt.setString("BlockPos", this.gui.te.getPos().toString());
		message.nbt.setString("selectEntity", this.gui.te.getSelectEntity());
		NetworkLoader.instance.sendToServer(message);*/
	}

	@Override
	protected boolean isSelected(int slotIndex)
	{
		return false;
	}

	@Override
	protected void clickedHeader(int posX, int posY)
	{

	}

	@Override
	public int getSlotIndexFromScreenCoords(int posX, int posY)
	{
		float x = posX - this.posX;
		float y = posY - this.posY - this.headerPadding;
		if (x > 0 && x < this.width && y > 0 && y < this.height)
		{
			float realY = this.amountScrolled + y;
			return MathHelper.ceil(realY / this.slotHeight) * this.slotInRow + MathHelper.ceil(posX / this.slotWidth);
		}
		return 0;
	}

	@Override
	public void handleMouseInput()
	{

	}

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks)
	{
		if (this.visible)
		{
			this.bindAmountScrolled();
			this.mouseX = mouseXIn;
			this.mouseY = mouseYIn;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Tessellator tessellator = Tessellator.getInstance();
			this.drawBackground();
			this.drawListHeader(this.posX, this.posY, tessellator);
			this.drawScrollBar(this.posX + this.width - this.scrollWidth + this.lineWidth, this.posY + this.headerPadding + this.lineWidth + MathHelper.floor(this.amountScrolled));
			this.drawSlotBackGround(this.posX, this.posY + this.headerPadding);

			for (int i = 0; i < this.slotInRow; i++)
				for (int j = 0; j < this.slotInColum; j++)
				{
					int id = this.slotInRow * i + j;
					int startX = this.posX + i * (this.slotWidth + this.lineWidth) + 1;
					int startY = this.posY + this.headerPadding + j * (this.slotHeight + this.lineWidth) + 1;
					this.drawSlot(id, startX, startY, this.slotHeight, mouseXIn, mouseYIn);
				}
		}

	}

	@Override
	protected void drawBackground()
	{
		drawRectangle(this.posX, this.posY, this.posX + width, this.posY + height, 0xFF0F0F0F, 0);
	}

	@Override
	protected void drawListHeader(int posX, int posY, Tessellator tessellatorIn)
	{
		drawRectangle(posX, posY, posX + this.width, posY + this.headerPadding, 0xFFFFFFFF, 0);
		this.mc.getTextureManager().bindTexture(DEFAULT);
		drawZoomTexturedModalRect(posX +2, posY +2, 10, 10, this.width - 4,this.headerPadding -4, 200, 200, 256, 256,0);
		drawCenteredString(this.mc.fontRendererObj, this.listHead, this.posX + (this.width - this.scrollWidth) /2, this.posY + this.headerPadding / 2 - 8, 2F, getHexColorARGB(255,0,0,255));
	}

	@Override
	protected void drawSlot(int entryID, int xPos, int yPos, int SlotHeight, int mouseXIn, int mouseYIn)
	{
		String s = MobSummonTileEntity.EntityDisplayName.get(this.list.get(entryID));
		drawCenteredString(this.mc.fontRendererObj, s == null ? this.list.get(entryID) : s, xPos + this.lineWidth + this.slotWidth / 2, yPos + this.lineWidth + this.slotHeight / 2 - 4, 1F, 0xFFF0F0F0);
	}

	protected void drawScrollBar(int posX, int posY)
	{
		this.mc.getTextureManager().bindTexture(BUTTON);
		drawZoomTexturedModalRect(posX, posY, 40, 0, this.scrollWidth - 2* this.lineWidth, 15, 12, 15, 256, 256, 300);
		drawRectangle(this.posX +this.width - this.scrollWidth, this.posY + this.headerPadding, this.posX + this.width, this.posY + this.height + this.lineWidth, 0xFFC6C6C6, 10);
		drawRectangle(this.posX +this.width - this.scrollWidth, this.posY + this.headerPadding + this.lineWidth, this.posX + this.width - this.lineWidth, this.posY + this.height, 0xFFFFFFC0, 10);
	}

	public void resetPosition(int posX, int posY)
	{
		this.posX = posX;
		this.posY = posY;
	}

	protected void drawSlotBackGround(int posX, int posY)
	{
		drawTableLineUseGui(posX, posY, 8, 2, this.width - this.scrollWidth, this.height - this.headerPadding, this.lineWidth, 0xFFC6C6C6, 100);
	}

}
