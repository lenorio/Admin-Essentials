package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.DurationParser;
import net.russianphonks.adminmod.util.LocalizationManager;
import net.russianphonks.adminmod.util.PermissionChecker;

public class TempMuteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tempmute")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.tempmute"))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .suggests((context, builder) -> CommandSource.suggestMatching(
                                        context.getSource().getServer().getPlayerManager().getPlayerList().stream().map(p -> p.getName().getString()),
                                        builder
                                ))
                                .then(CommandManager.argument("duration", StringArgumentType.word())
                                        .executes(context -> execute(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "player"),
                                                StringArgumentType.getString(context, "duration"),
                                                LocalizationManager.tr("punish.default.reason")
                                        ))
                                        .then(CommandManager.argument("reason", StringArgumentType.greedyString())
                                                .executes(context -> execute(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayer(context, "player"),
                                                        StringArgumentType.getString(context, "duration"),
                                                        StringArgumentType.getString(context, "reason")
                                                ))
                                        )
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity target, String duration, String reason) {
        long parsed = DurationParser.parseMillis(duration);
        if (parsed <= 0) {
            source.sendError(ColorUtil.colorize(LocalizationManager.tr("punish.duration.invalid")));
            return 0;
        }

        long expiresAt = System.currentTimeMillis() + parsed;
        String actor = source.getName();
        AdminMod.punishmentManager.tempMute(target, expiresAt, reason, actor);

        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("punish.success.tempmute", target.getName().getString(), duration, reason)), true);
        target.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.mute.message")));
        target.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.temp.mute.reason.line", reason, duration)));
        return 1;
    }
}
