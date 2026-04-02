package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.VanishState;

import java.util.EnumSet;
import java.util.List;

public class VanishCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("vanish")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> handleVanish(context.getSource()))
        );

        dispatcher.register(
            CommandManager.literal("v")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> handleVanish(context.getSource()))
        );
    }

    private static int handleVanish(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity targetPlayer)) {
            source.sendError(Text.literal("§cOnly players can use this command!"));
            return 0;
        }

        VanishState vanishState = (VanishState) targetPlayer;
        boolean isVanished = vanishState.adminmod$isVanished();

        if (isVanished) {
            vanishState.adminmod$setVanished(false);
            targetPlayer.removeStatusEffect(StatusEffects.INVISIBILITY);
            targetPlayer.removeStatusEffect(StatusEffects.NIGHT_VISION);

            source.getServer().getPlayerManager().sendToAll(
                    new PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER), List.of(targetPlayer))
            );

            source.sendFeedback(() -> ColorUtil.colorize("&aYou are now &bvisible&a!"), false);
        } else {
            vanishState.adminmod$setVanished(true);
            targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, true, false));
            targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, true, false));

            source.getServer().getPlayerManager().sendToAll(
                    new PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.UPDATE_LISTED), List.of(targetPlayer))
            );

            source.sendFeedback(() -> ColorUtil.colorize("&aYou are now &bvanished&a!"), false);
        }

        return 1;
    }
}
