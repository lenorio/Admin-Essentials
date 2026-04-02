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
import net.russianphonks.adminmod.util.LocalizationManager;
import net.russianphonks.adminmod.util.PermissionChecker;

public class MuteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("mute")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.mute"))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .suggests((context, builder) -> CommandSource.suggestMatching(
                                        context.getSource().getServer().getPlayerManager().getPlayerList().stream().map(p -> p.getName().getString()),
                                        builder
                                ))
                                .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "player"), LocalizationManager.tr("punish.default.reason")))
                                .then(CommandManager.argument("reason", StringArgumentType.greedyString())
                                        .executes(context -> execute(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "player"),
                                                StringArgumentType.getString(context, "reason")
                                        ))
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity target, String reason) {
        String actor = source.getName();
        AdminMod.punishmentManager.mute(target, reason, actor);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("punish.success.mute", target.getName().getString(), reason)), true);
        target.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.mute.message")));
        target.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.mute.reason.line", reason)));
        return 1;
    }
}
