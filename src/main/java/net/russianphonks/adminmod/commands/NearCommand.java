package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.config.ConfigManager;
import net.russianphonks.adminmod.util.ColorUtil;

public class NearCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("near")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeNear(context.getSource(), ConfigManager.getConfig().near_default_radius))
                .then(CommandManager.argument("radius", IntegerArgumentType.integer(1, 500))
                    .executes(context -> executeNear(context.getSource(), IntegerArgumentType.getInteger(context, "radius")))
                )
        );
    }

    private static int executeNear(ServerCommandSource source, int radius) {
        if (!(source.getEntity() instanceof PlayerEntity player)) {
            source.sendError(Text.literal("§cOnly players can use this command!"));
            return 0;
        }

        source.sendFeedback(() -> ColorUtil.colorize("&8---=== &fPlayers near you (&e" + radius + " blocks&f) &8===-"), false);

        java.util.List<ServerPlayerEntity> nearbyPlayers = source.getServer().getPlayerManager().getPlayerList().stream()
            .filter(p -> p != player)
            .filter(p -> p.distanceTo(player) <= radius)
            .sorted((a, b) -> Double.compare(a.distanceTo(player), b.distanceTo(player)))
            .toList();

        if (nearbyPlayers.isEmpty()) {
            source.sendFeedback(() -> ColorUtil.colorize("&cNo players nearby!"), false);
            return 1;
        }

        for (ServerPlayerEntity p : nearbyPlayers) {
            double distance = player.distanceTo(p);
            int x = (int) p.getX();
            int y = (int) p.getY();
            int z = (int) p.getZ();

            source.sendFeedback(() -> ColorUtil.colorize(String.format(
                    "&6%s &7(%d blocks away) &8at &e%d, %d, %d",
                    p.getName().getString(), (int) distance, x, y, z
            )), false);
        }

        return 1;
    }
}
