package net.russianphonks.adminmod.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.config.ConfigManager;

public class PermissionChecker {
    public static boolean hasPermission(CommandContext<ServerCommandSource> context, String permission) {
        return hasPermission(context.getSource(), permission);
    }

    public static boolean hasPermission(ServerCommandSource source, String permission) {
        if (source.getServer() == null || !source.isExecutedByPlayer()) {
            return true;
        }

        if (source.hasPermissionLevel(4)) {
            return true;
        }

        if (source.getEntity() == null || source.getEntity().getName() == null) {
            return false;
        }

        String playerName = source.getEntity().getName().getString();

        if (AdminMod.permissionManager != null && AdminMod.permissionManager.hasPermission(playerName, permission)) {
            return true;
        }

        return ConfigManager.playerHasCommand(playerName, permission);
    }

    public static void sendNoPermissionMessage(ServerCommandSource source) {
        source.sendError(ColorFormatter.coloredText("&cYou don't have permission to execute this command!"));
    }
}
