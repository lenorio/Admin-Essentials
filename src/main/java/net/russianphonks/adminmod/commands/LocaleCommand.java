package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.config.ConfigManager;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.LocalizationManager;
import net.russianphonks.adminmod.util.PermissionChecker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocaleCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("locale")
                        .requires(source -> PermissionChecker.hasPermission(source, "adminmod.locale"))
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("code", StringArgumentType.word())
                                        .executes(context -> {
                                            String code = StringArgumentType.getString(context, "code");
                                            Path file = Paths.get("config/locales/locale_" + code.toLowerCase() + ".loc");
                                            if (!Files.exists(file)) {
                                                context.getSource().sendError(ColorUtil.colorize(LocalizationManager.tr("locale.set.missing", code)));
                                                return 0;
                                            }

                                            ConfigManager.getConfig().locale = code.toLowerCase();
                                            ConfigManager.saveConfig();
                                            LocalizationManager.setLocale(code);
                                            context.getSource().sendFeedback(() -> ColorUtil.colorize(LocalizationManager.tr("locale.set.success", code.toLowerCase())), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("reload")
                                .executes(context -> {
                                    LocalizationManager.initialize();
                                    context.getSource().sendFeedback(() -> ColorUtil.colorize("&aLocales reloaded. Active: &e" + LocalizationManager.getActiveLocale()), false);
                                    return 1;
                                })
                        )
        );
    }
}
