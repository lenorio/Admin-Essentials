package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.config.ConfigManager;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.PermissionChecker;

import java.util.Locale;
import java.util.Map;

public class BroadcastCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("bc")
                .requires(source -> PermissionChecker.hasPermission(source, "adminmod.broadcast"))
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        String message = StringArgumentType.getString(context, "message");

                        return sendBroadcast(source, "broadcast", message);
                    })
                )
                .then(CommandManager.argument("tag", StringArgumentType.word())
                    .suggests((context, builder) -> CommandSource.suggestMatching(
                            ConfigManager.getConfig().broadcast_tags.keySet(),
                            builder
                    ))
                    .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            String tag = StringArgumentType.getString(context, "tag");
                            String message = StringArgumentType.getString(context, "message");

                            return sendBroadcast(source, tag, message);
                        })
                    )
                )
        );
    }

    private static int sendBroadcast(ServerCommandSource source, String tagKey, String message) {
        String tag = resolveTag(tagKey);
        Text coloredMessage = ColorUtil.colorize("&8[&e" + tag + "&8] &r" + message);
        source.getServer().getPlayerManager().broadcast(coloredMessage, false);
        source.sendFeedback(() -> Text.literal("§aBroadcast sent!"), false);
        return 1;
    }

    private static String resolveTag(String tagKey) {
        Map<String, String> tags = ConfigManager.getConfig().broadcast_tags;
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tagKey)) {
                return entry.getValue();
            }
        }
        return tagKey.toUpperCase(Locale.ROOT);
    }
}
