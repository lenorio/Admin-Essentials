package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.LocalizationManager;
import net.russianphonks.adminmod.util.PermissionChecker;

public class HelpCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("help")
                        .then(CommandManager.literal("adminmod")
                                .requires(source -> PermissionChecker.hasPermission(source, "adminmod.help"))
                                .executes(context -> sendHelp(context.getSource()))
                        )
        );

        dispatcher.register(
                CommandManager.literal("adminhelp")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.help"))
                        .executes(context -> sendHelp(context.getSource()))
        );
    }

    private static int sendHelp(ServerCommandSource source) {
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.title")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.tps")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.invsee")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.near")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.vanish")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.rename")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.lore")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.allow")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.deny")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.locale")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.ban")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.mute")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.tempban")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.tempmute")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.unban")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.unmute")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.untempban")), false);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("help.line.untempmute")), false);
        return 1;
    }
}
