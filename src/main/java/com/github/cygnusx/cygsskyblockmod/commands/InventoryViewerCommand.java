package com.github.cygnusx.cygsskyblockmod.commands;

import com.github.cygnusx.cygsskyblockmod.CygsSkyblockMod;
import com.github.cygnusx.cygsskyblockmod.GUIs.InventoryViewerGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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

        //make call to API and create new inventory with inventory contents.
        String playerName = args[0];
        EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(playerName);
        InventoryBasic inventory = new InventoryBasic("test", false, 36);
        inventory.setInventorySlotContents(0, new ItemStack(Blocks.dirt));
        InventoryViewerGUI inventoryContainerGui = new InventoryViewerGUI(inventory); //temp for now TODO: fix

        if (player != null) {
            CygsSkyblockMod.INSTANCE.openGui = inventoryContainerGui;
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Something went wrong..."));
        }
    }
}
