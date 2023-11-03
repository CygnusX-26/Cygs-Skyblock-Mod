package com.github.cygnusx.cygsskyblockmod.GUIs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


import java.io.IOException;

public class InventoryViewerGUI extends GuiContainer {

    private IInventory inventory;

    public final ResourceLocation GUITEXTURE = new ResourceLocation("cygsskyblockmod", "InventoryViewer.png");

    public InventoryViewerGUI(IInventory containerInventory) {
        super(new InventoryViewerContainer(containerInventory));
        this.inventory = containerInventory;
        this.allowUserInput = false;
//        this.xSize = 176;
//        this.ySize = 108;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawDefaultBackground();
        this.mc.renderEngine.bindTexture(GUITEXTURE);
        this.drawTexturedModalRect(Minecraft.getMinecraft().currentScreen.width/2-88,Minecraft.getMinecraft().currentScreen.height/2-84,0, 0, 256, 256);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(inventory.getName(), (176) / 2 - 28, 6, 4210752);
    }
}
