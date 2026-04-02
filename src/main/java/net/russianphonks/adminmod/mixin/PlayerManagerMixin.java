package net.russianphonks.adminmod.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.russianphonks.adminmod.util.VanishState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "broadcast", at = @At("HEAD"), cancellable = true)
    private void adminmod$hideVanishedJoinLeave(Text message, boolean overlay, CallbackInfo ci) {
        String plainMessage = message.getString().toLowerCase(Locale.ROOT);
        if (!plainMessage.contains("joined the game") && !plainMessage.contains("left the game")) {
            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player instanceof VanishState vanishState && vanishState.adminmod$isVanished()) {
                String playerName = player.getName().getString().toLowerCase(Locale.ROOT);
                if (plainMessage.contains(playerName)) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
}