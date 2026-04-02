package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.config.ConfigManager;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.PermissionChecker;
import net.russianphonks.adminmod.util.PermissionNodes;

public class AllowCommandCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("allowcommand")
                .requires(source -> source.hasPermissionLevel(4) || PermissionChecker.hasPermission(source, "adminmod.allowcommand"))
                .then(CommandManager.argument("PlayerEntity", StringArgumentType.word())
                    .suggests((context, builder) -> CommandSource.suggestMatching(
                            context.getSource().getServer().getPlayerManager().getPlayerList().stream()
                                    .map(player -> player.getName().getString()),
                            builder
                    ))
                    .then(CommandManager.argument("permission", StringArgumentType.greedyString())
                        .suggests((context, builder) -> CommandSource.suggestMatching(PermissionNodes.ALL, builder))
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            String playerName = StringArgumentType.getString(context, "PlayerEntity");
                            String permission = StringArgumentType.getString(context, "permission");

                            AdminMod.permissionManager.grantPermission(playerName, permission);
                            ConfigManager.grantCommand(playerName, permission);

                            source.sendFeedback(() -> ColorUtil.colorize(
                                    "&aPermission &e" + permission + "&a granted to &e" + playerName
                            ), false);

                            // Notify the PlayerEntity if online
                            var target = source.getServer().getPlayerManager().getPlayer(playerName);
                            if (target != null) {
                                target.sendMessage(ColorUtil.colorize(
                                        "&aYou have been granted permission: &e" + permission
                                ));
                            }

                            return 1;
                        })
                    )
                )
        );
    }
}
