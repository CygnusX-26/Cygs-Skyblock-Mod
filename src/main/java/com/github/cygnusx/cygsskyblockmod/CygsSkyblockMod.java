package com.github.cygnusx.cygsskyblockmod;

import com.github.cygnusx.cygsskyblockmod.commands.InventoryViewerCommand;
import com.github.cygnusx.cygsskyblockmod.commands.SkyblockCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "cygsskyblockmod", useMetadata=true, clientSideOnly = true)
public class CygsSkyblockMod {

    public GuiScreen openGui = null;
    public static CygsSkyblockMod INSTANCE = null;


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Dirt: " + Blocks.dirt.getUnlocalizedName());
        ClientCommandHandler.instance.registerCommand(new SkyblockCommand());
        ClientCommandHandler.instance.registerCommand(new InventoryViewerCommand());
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (Minecraft.getMinecraft().thePlayer == null) {
            openGui = null;
            return;
        }
        if (openGui != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }
            Minecraft.getMinecraft().displayGuiScreen(openGui);
            openGui = null;
        }
    }
}
