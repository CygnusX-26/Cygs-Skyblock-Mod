package com.github.cygnusx.cygsskyblockmod.commands;

import com.github.cygnusx.cygsskyblockmod.CygsSkyblockMod;
import com.github.cygnusx.cygsskyblockmod.GUIs.InventoryViewerGUI;
import com.github.cygnusx.cygsskyblockmod.utils.HandleAPIRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class InventoryViewerCommand extends CommandBase {
    public final int argCount = 1;

    @Override
    public String getCommandName() {
        return "spy";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "view a player's inventory";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != this.argCount) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"This command expects " + this.argCount + " argument"));
            return;
        }
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Requesting API Data"));
        String playerName = args[0];
        HandleAPIRequest
                .request()
                .host("api.mojang.com").path("/users/profiles/minecraft/"+playerName)
                .method("GET")
                .requestJson().thenAcceptAsync(jsonObject -> {
                    String uuid = jsonObject.get("id").getAsString();
                    HandleAPIRequest
                            .request()
                            .host("api.hypixel.net")
                            .path("/skyblock/profiles")
                            .addHeader("API-Key", "ae0804c8-d15c-486c-a460-83efc7d2dbbe")
                            .addArgument("uuid", uuid)
                            .method("GET").requestJson().thenAcceptAsync(jsonObjectSub -> {
                                String nbtDataString = jsonObjectSub.get("profiles").getAsJsonArray()
                                        .get(getLastActiveProfileID(jsonObjectSub, uuid)).getAsJsonObject()
                                        .get("members").getAsJsonObject()
                                        .get(uuid).getAsJsonObject() //uuid here
                                        .get("inv_contents").getAsJsonObject()
                                        .get("data").getAsString();
                                try {
                                    byte[] nbtDataBytes = Base64.getDecoder().decode(nbtDataString.getBytes());
                                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nbtDataBytes);
                                    NBTTagCompound nbtTagCompound = CompressedStreamTools.readCompressed(byteArrayInputStream);
                                    InventoryViewerGUI inventoryContainerGui = generateInventoryContents(nbtTagCompound, playerName);
                                    if (CygsSkyblockMod.RISK) {
                                        //set RISK to true to enable inv container viewer... Unsure if causes weird connections from client
                                        CygsSkyblockMod.INSTANCE.openGui = inventoryContainerGui;
                                    } else {
                                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Something went wrong..."));
                                    }
                                    byteArrayInputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }

    /*
     * generates the inventory GUI using NBT data
     */
    private InventoryViewerGUI generateInventoryContents(NBTTagCompound nbtTagCompound, String playerName) {
        InventoryBasic inventory = new InventoryBasic(String.format("Inventory of %s", playerName), false, 36);
        for (int i = 0; i < 27; i++) {
            if (ItemStack.loadItemStackFromNBT(nbtTagCompound
                    .getTagList("i", 10)
                    .getCompoundTagAt(i)) != null) {
                inventory.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(nbtTagCompound
                        .getTagList("i", 10)
                        .getCompoundTagAt(i)));
            }

        }
        return new InventoryViewerGUI(inventory);
    }

    /*
     * gets the currently selected profile of a player
     */
    private int getLastActiveProfileID(JsonObject jsonObject, String uuid) {
        JsonArray jsonArray = jsonObject.get("profiles").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getAsJsonObject().get("selected").getAsBoolean()) {
                return i;
            }
        }
        return -1;
    }
}
