package com.github.cygnusx.cygsskyblockmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class SkyblockCommand extends CommandBase {
    public final String[] technobladeQuote =
            {"Skyblock teaches us that no matter how ridiculous the odds may seem,",
            "within us resides the power to overcome these challenges and achieve something beautiful.",
            "That one day, we'll look back at where we started and be amazed by how far we've come.",
            " - " + EnumChatFormatting.LIGHT_PURPLE + "Technoblade"};
    @Override
    public String getCommandName() {
        return "skyblock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Rest in Peace";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        for (int i = 0; i < technobladeQuote.length; i++) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(technobladeQuote[i]));
        }
    }
}
