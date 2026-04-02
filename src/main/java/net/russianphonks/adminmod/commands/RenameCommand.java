package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.util.ColorUtil;

public class RenameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("rename")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        String newName = StringArgumentType.getString(context, "name");

                        if (!(source.getEntity() instanceof PlayerEntity player)) {
                            source.sendError(Text.literal("§cOnly players can use this command!"));
                            return 0;
                        }

                        ItemStack item = player.getMainHandStack();
                        if (item.isEmpty()) {
                            source.sendError(Text.literal("§cYou must hold an item in your main hand!"));
                            return 0;
                        }

                        // Replace &color codes with minecraft format codes
                        String coloredName = newName.replace("\\n", "\n");
                        coloredName = coloredName.replace("&0", "§0")
                                .replace("&1", "§1")
                                .replace("&2", "§2")
                                .replace("&3", "§3")
                                .replace("&4", "§4")
                                .replace("&5", "§5")
                                .replace("&6", "§6")
                                .replace("&7", "§7")
                                .replace("&8", "§8")
                                .replace("&9", "§9")
                                .replace("&a", "§a")
                                .replace("&b", "§b")
                                .replace("&c", "§c")
                                .replace("&d", "§d")
                                .replace("&e", "§e")
                                .replace("&f", "§f")
                                .replace("&l", "§l")
                                .replace("&n", "§n")
                                .replace("&o", "§o")
                                .replace("&r", "§r");

                        item.set(DataComponentTypes.CUSTOM_NAME, Text.literal(coloredName));

                        final String displayName = coloredName;
                        source.sendFeedback(() -> ColorUtil.colorize("&aItem renamed to: &r" + displayName), false);
                        return 1;
                    })
                )
        );
    }
}
