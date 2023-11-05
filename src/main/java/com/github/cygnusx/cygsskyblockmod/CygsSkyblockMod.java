package com.github.cygnusx.cygsskyblockmod;

import com.github.cygnusx.cygsskyblockmod.commands.EnableDeveloperModeCommand;
import com.github.cygnusx.cygsskyblockmod.commands.InventoryViewerCommand;
import com.github.cygnusx.cygsskyblockmod.commands.SkyblockCommand;
import com.github.cygnusx.cygsskyblockmod.utils.HandleAPIRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = "cygsskyblockmod", useMetadata=true, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class CygsSkyblockMod {

    public GuiScreen openGui = null;
    public static boolean auctionCache = false;
    public long lastAuctionQuery = 0;
    public static CygsSkyblockMod INSTANCE = null;
    public static boolean RISK = false;
    public static ExecutorService fixedPool = Executors.newFixedThreadPool(4);
    public static Gson gson = new Gson();
    public static JsonObject auctionJSON = null;


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Dirt: " + Blocks.dirt.getUnlocalizedName());
        ClientCommandHandler.instance.registerCommand(new SkyblockCommand());
        ClientCommandHandler.instance.registerCommand(new InventoryViewerCommand());
        ClientCommandHandler.instance.registerCommand(new EnableDeveloperModeCommand());
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
        if (auctionCache) {
            //Query Auction Cache

            auctionCache = false;
        }
        if (System.currentTimeMillis() - lastAuctionQuery > 600000) {
            lastAuctionQuery = System.currentTimeMillis();
            HandleAPIRequest
                    .request()
                    .host("api.hypixel.net")
                    .path("/skyblock/auctions")
                    .method("GET").requestJson().thenAcceptAsync((jsonObject -> {
                        auctionJSON = jsonObject;
                    }));
        }
    }
}
