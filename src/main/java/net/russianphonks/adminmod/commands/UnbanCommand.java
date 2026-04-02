package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.PermissionChecker;

public class UnbanCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("unban")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.unban"))
                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .suggests((context, builder) -> CommandSource.suggestMatching(
                                        AdminMod.punishmentManager.getPunishedPlayerNames(),
                                        builder
                                ))
                                .executes(context -> {
                                    String player = StringArgumentType.getString(context, "player");
                                    boolean removed = AdminMod.punishmentManager.unbanByName(player);
                                    if (!removed) {
                                        context.getSource().sendError(ColorUtil.colorize("&cNo ban found for &e" + player));
                                        return 0;
                                    }

                                    context.getSource().sendFeedback(() -> ColorUtil.colorize("&aUnbanned &e" + player), true);
                                    return 1;
                                })
                        )
        );
    }
}
