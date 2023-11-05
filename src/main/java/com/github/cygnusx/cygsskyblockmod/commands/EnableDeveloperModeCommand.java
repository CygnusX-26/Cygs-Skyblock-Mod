package com.github.cygnusx.cygsskyblockmod.commands;

import com.github.cygnusx.cygsskyblockmod.CygsSkyblockMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class EnableDeveloperModeCommand extends CommandBase {
    public final int argCount = 0;

    @Override
    public String getCommandName() {
        return "devmode";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "enables developer mode, use at own risk.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > this.argCount){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"This command expects " + this.argCount + " argument"));
            return;
        }
        if (CygsSkyblockMod.RISK) {
            CygsSkyblockMod.RISK = false;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD+"Developer mode disabled"));
            return;
        }
        CygsSkyblockMod.RISK = true;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Developer mode enabled"));
    }
}
