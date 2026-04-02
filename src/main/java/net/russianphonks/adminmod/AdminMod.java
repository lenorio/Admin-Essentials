package net.russianphonks.adminmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.russianphonks.adminmod.commands.*;
import net.russianphonks.adminmod.config.ConfigManager;
import net.russianphonks.adminmod.util.DurationParser;
import net.russianphonks.adminmod.util.LocalizationManager;
import net.russianphonks.adminmod.util.PermissionManager;
import net.russianphonks.adminmod.util.PunishmentManager;
import net.russianphonks.adminmod.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("AdminMod");
    public static PermissionManager permissionManager;
    public static PunishmentManager punishmentManager;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Admin Mod...");

        permissionManager = new PermissionManager();
        punishmentManager = new PunishmentManager();
        
        ConfigManager.loadConfig();
        LocalizationManager.initialize();
        ServerLifecycleEvents.SERVER_STARTED.register(permissionManager::load);
        ServerLifecycleEvents.SERVER_STARTED.register(punishmentManager::load);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!punishmentManager.isBanned(handler.player)) {
                return;
            }

            String reason = punishmentManager.getBanReason(handler.player);
            long expiry = punishmentManager.getBanExpiry(handler.player);
            if (expiry > 0) {
                long remaining = Math.max(0, expiry - System.currentTimeMillis());
                String humanDuration = DurationParser.formatRemaining(remaining);
                handler.disconnect(ColorUtil.colorize(LocalizationManager.tr("punish.tempban.message", reason, humanDuration)));
            } else {
                handler.disconnect(ColorUtil.colorize(LocalizationManager.tr("punish.ban.message", reason)));
            }
        });

        // Register CommandManager
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BroadcastCommand.register(dispatcher);
            TpsCommand.register(dispatcher);
            InvSeeCommand.register(dispatcher);
            NearCommand.register(dispatcher);
            VanishCommand.register(dispatcher);
            RenameCommand.register(dispatcher);
            LoreCommand.register(dispatcher);
            AllowCommandCommand.register(dispatcher);
            DenyCommandCommand.register(dispatcher);
            LocaleCommand.register(dispatcher);
            HelpCommand.register(dispatcher);
            BanCommand.register(dispatcher);
            MuteCommand.register(dispatcher);
            TempBanCommand.register(dispatcher);
            TempMuteCommand.register(dispatcher);
            UnbanCommand.register(dispatcher);
            UnmuteCommand.register(dispatcher);
            UnTempBanCommand.register(dispatcher);
            UnTempMuteCommand.register(dispatcher);
        });

        LOGGER.info("Admin Mod initialized successfully!");
    }
}
