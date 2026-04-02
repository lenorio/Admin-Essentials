package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.util.ColorUtil;

public class LoreCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("lore")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("lore", StringArgumentType.greedyString())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        String loreText = StringArgumentType.getString(context, "lore");

                        if (!(source.getEntity() instanceof PlayerEntity player)) {
                            source.sendError(Text.literal("§cOnly players can use this command!"));
                            return 0;
                        }

                        ItemStack item = player.getMainHandStack();
                        if (item.isEmpty()) {
                            source.sendError(Text.literal("§cYou must hold an item in your main hand!"));
                            return 0;
                        }

                        // Split by \n to allow multi-line lore
                        String[] lines = loreText.split("\\\\n");

                        List<Text> loreTexts = new ArrayList<>();
                        for (String line : lines) {
                            String coloredLine = line.replace("&0", "§0").replace("&1", "§1").replace("&2", "§2").replace("&3", "§3")
                                    .replace("&4", "§4").replace("&5", "§5").replace("&6", "§6").replace("&7", "§7").replace("&8", "§8")
                                    .replace("&9", "§9").replace("&a", "§a").replace("&b", "§b").replace("&c", "§c").replace("&d", "§d")
                                    .replace("&e", "§e").replace("&f", "§f").replace("&l", "§l").replace("&n", "§n").replace("&o", "§o").replace("&r", "§r");
                            loreTexts.add(Text.literal(coloredLine));
                        }
                        item.set(DataComponentTypes.LORE, new LoreComponent(loreTexts));

                        source.sendFeedback(() -> ColorUtil.colorize("&aLore set!"), false);
                        return 1;
                    })
                )
        );
    }
}
