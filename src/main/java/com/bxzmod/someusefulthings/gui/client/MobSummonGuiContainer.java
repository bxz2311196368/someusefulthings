package com.bxzmod.someusefulthings.gui.client;

import com.bxzmod.someusefulthings.ModInfo;
import com.bxzmod.someusefulthings.gui.server.MobSummonContainer;
import com.bxzmod.someusefulthings.network.MultiSignSync;
import com.bxzmod.someusefulthings.network.NetworkLoader;
import com.bxzmod.someusefulthings.tileentity.MobSummonTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class MobSummonGuiContainer extends BaseGuiContainer
{
	private static final String TEXTURE_PATH = ModInfo.MODID + ":" + "textures/gui/container/CopyEnchantment.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	public static final ResourceLocation TEXTURES_B = new ResourceLocation(
		ModInfo.MODID + ":" + "textures/gui/container/button.png");

	public MobSummonTileEntity te;

	private GuiSlot list;

	public MobSummonGuiContainer(MobSummonContainer inventorySlotsIn)
	{
		super(inventorySlotsIn, "", TEXTURE, 220, 166);
		this.te = inventorySlotsIn.getBasicTe();
		this.te.setClinetWorldEntityNames();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		//no op
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//no op
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2;
		int offsetY = (this.height - this.ySize) / 2;
		int lineWidth = 2;
		int slotWidth = (this.xSize - 12 - lineWidth * 2) / 2 - lineWidth;
		int slotHeight = (this.ySize - 30) / 8 - lineWidth;
		this.list = new BaseGuiListString(this.mc, "list", offsetX, offsetY, 30, slotWidth, slotHeight, 2, 8, 2, this, MobSummonTileEntity.getEntitys());
		this.list.registerScrollButtons(7, 8);
		/*this.buttonList.add(new GuiButton(100, offsetX, offsetY + this.ySize, this.xSize, 18, "reset")
		{

			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY)
			{
				if (this.visible)
				{
					FontRenderer fontrenderer = mc.fontRendererObj;
					mc.getTextureManager().bindTexture(TEXTURES_B);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					MobSummonGuiContainer.this.drawZoomTexturedModalRect(this.xPosition, this.yPosition, 1, 18,
							this.width, this.height, 40, 20, 256, 256);
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
					this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
							this.yPosition + (this.height - 8) / 2, j);
				}
			}

		});*/
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.list.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		switch (button.id)
		{
		case 100:
			MobSummonGuiContainer.this.te.setSelectEntity("");
			MultiSignSync message = new MultiSignSync();
			message.nbt = new NBTTagCompound();
			message.nbt.setString("Type", "MobSummon");
			message.nbt.setInteger("world", this.mc.world.provider.getDimension());
			message.nbt.setString("BlockPos", this.te.getPos().toString());
			message.nbt.setString("selectEntity", "null");
			NetworkLoader.instance.sendToServer(message);
		}
		this.list.actionPerformed(button);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.list.handleMouseInput();
	}

	@SideOnly(Side.CLIENT)
	class ListEntityDisplay extends GuiSlot
	{
		private List<String> EntityName = MobSummonTileEntity.getEntitys();

		public ListEntityDisplay(Minecraft mcIn, int width, int height, int left, int right, int topIn, int bottomIn,
				int slotHeightIn)
		{
			super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
			this.left = left;
			this.right = right;
		}

		@Override
		protected int getSize()
		{
			return this.EntityName.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			MobSummonGuiContainer.this.te.setSelectEntity(EntityName.get(slotIndex));
			MultiSignSync message = new MultiSignSync();
			message.nbt = new NBTTagCompound();
			message.nbt.setString("Type", "MobSummon");
			message.nbt.setInteger("world", this.mc.world.provider.getDimension());
			message.nbt.setString("BlockPos", MobSummonGuiContainer.this.te.getPos().toString());
			message.nbt.setString("selectEntity", MobSummonGuiContainer.this.te.getSelectEntity());
			NetworkLoader.instance.sendToServer(message);
		}

		@Override
		protected boolean isSelected(int slotIndex)
		{
			return EntityName.size() > slotIndex && EntityName.get(slotIndex)
					.equals(MobSummonGuiContainer.this.te.getSelectEntity());
		}

		@Override
		protected void drawBackground()
		{
			int offsetX = (MobSummonGuiContainer.this.width - MobSummonGuiContainer.this.xSize) / 2,
					offsetY = (MobSummonGuiContainer.this.height - MobSummonGuiContainer.this.ySize) / 2;
			MobSummonGuiContainer.this.drawGradientRect(offsetX, offsetY, offsetX + MobSummonGuiContainer.this.xSize,
					offsetY + MobSummonGuiContainer.this.ySize, -1072689136, -804253680);
		}

		@Override
		protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn)
		{
			if (yPos > this.bottom - 15 || yPos < this.top - 3)
				return;
			try
			{
				MobSummonGuiContainer.this.fontRendererObj.setBidiFlag(true);
				String s = MobSummonTileEntity.EntityDisplayName.get(EntityName.get(entryID));
				if (s == null || s.isEmpty())
					s = EntityName.get(entryID);
				MobSummonGuiContainer.this.drawCenteredString(MobSummonGuiContainer.this.fontRendererObj, s,
						this.width / 2, yPos + 1, 16777215);
				MobSummonGuiContainer.this.fontRendererObj.setBidiFlag(this.isSelected(entryID));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		protected int getScrollBarX()
		{
			return this.right - 6;
		}

		@Override
		public void setSlotXBoundsFromLeft(int leftIn)
		{
			this.left = leftIn;
			this.right = this.left + 170;
		}

		@Override
		protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
		{

		}

		@Override
		public int getListWidth()
		{
			return 170;
		}

		@Override
		protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn)
		{
			int i = this.getSize();
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();

			for (int j = 0; j < i; ++j)
			{
				int k = insideTop + j * this.slotHeight + this.headerPadding;
				int l = this.slotHeight - 4;

				if (k > this.bottom || k + l < this.top)
				{
					this.updateItemPos(j, insideLeft, k);
				}

				if (!(k > this.bottom - 18 || k + l < this.top + 18) && this.showSelectionBox && this.isSelected(j))
				{
					int i1 = this.left + 1;
					int j1 = this.right - 7;
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.disableTexture2D();
					vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
					vertexbuffer.pos((double) i1, (double) (k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255)
							.endVertex();
					vertexbuffer.pos((double) j1, (double) (k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255)
							.endVertex();
					vertexbuffer.pos((double) j1, (double) (k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255)
							.endVertex();
					vertexbuffer.pos((double) i1, (double) (k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255)
							.endVertex();
					vertexbuffer.pos((double) (i1 + 1), (double) (k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
							.endVertex();
					vertexbuffer.pos((double) (j1 - 1), (double) (k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
							.endVertex();
					vertexbuffer.pos((double) (j1 - 1), (double) (k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255)
							.endVertex();
					vertexbuffer.pos((double) (i1 + 1), (double) (k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255)
							.endVertex();
					tessellator.draw();
					GlStateManager.enableTexture2D();
				}

				this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn);
			}
		}

	}

}
