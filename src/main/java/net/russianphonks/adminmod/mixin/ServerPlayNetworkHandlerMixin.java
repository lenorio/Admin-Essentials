package net.russianphonks.adminmod.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.DurationParser;
import net.russianphonks.adminmod.util.LocalizationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void adminmod$blockMutedChat(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (AdminMod.punishmentManager == null || !AdminMod.punishmentManager.isMuted(player)) {
            return;
        }

        sendMuteMessage();
        ci.cancel();
    }

    @Inject(method = "onCommandExecution", at = @At("HEAD"), cancellable = true)
    private void onCommandExecution(CommandExecutionC2SPacket packet, CallbackInfo ci) {
        if (AdminMod.punishmentManager == null || !AdminMod.punishmentManager.isMuted(player)) {
            return;
        }

        String command = packet.command().toLowerCase();
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        if (command.startsWith("msg ") || command.startsWith("tell ") || command.startsWith("w ")
                || command.startsWith("me ") || command.startsWith("r ") || command.startsWith("reply ")
                || command.startsWith("whisper ")) {
            sendMuteMessage();
            ci.cancel();
        }
    }

    private void sendMuteMessage() {
        String reason = AdminMod.punishmentManager.getMuteReason(player);
        long expiry = AdminMod.punishmentManager.getMuteExpiry(player);
        player.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.mute.message")));
        if (expiry > 0) {
            long remaining = Math.max(0, expiry - System.currentTimeMillis());
            player.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.temp.mute.reason.line", reason, DurationParser.formatRemaining(remaining))));
        } else {
            player.sendMessage(ColorUtil.colorize(LocalizationManager.tr("punish.mute.reason.line", reason)));
        }
    }
}
