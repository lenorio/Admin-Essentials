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

public class BanCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("ban")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.ban"))
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
        AdminMod.punishmentManager.ban(target, reason, actor);
        source.sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("punish.success.ban", target.getName().getString(), reason)), true);
        target.networkHandler.disconnect(ColorUtil.colorize(LocalizationManager.tr("punish.ban.message", reason)));
        return 1;
    }
}
