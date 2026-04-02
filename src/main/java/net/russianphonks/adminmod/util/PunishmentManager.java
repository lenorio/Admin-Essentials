package net.russianphonks.adminmod.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.russianphonks.adminmod.AdminMod;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PunishmentManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, PunishmentEntry>>(){}.getType();

    private final Path punishmentsDir = Paths.get("config/adminmod/punishments");
    private final Path bansFile = punishmentsDir.resolve("bans.json");
    private final Path mutesFile = punishmentsDir.resolve("mutes.json");
    private final Path tempBansFile = punishmentsDir.resolve("tempbans.json");
    private final Path tempMutesFile = punishmentsDir.resolve("tempmutes.json");

    private final Map<String, PunishmentEntry> bans = new HashMap<>();
    private final Map<String, PunishmentEntry> mutes = new HashMap<>();
    private final Map<String, PunishmentEntry> tempBans = new HashMap<>();
    private final Map<String, PunishmentEntry> tempMutes = new HashMap<>();

    public void load(MinecraftServer server) {
        try {
            Files.createDirectories(punishmentsDir);
            loadInto(bansFile, bans);
            loadInto(mutesFile, mutes);
            loadInto(tempBansFile, tempBans);
            loadInto(tempMutesFile, tempMutes);
            cleanupExpired();
            saveAll();
        } catch (Exception e) {
            AdminMod.LOGGER.error("Failed to load punishments", e);
        }
    }

    public void ban(ServerPlayerEntity target, String reason, String actor) {
        String key = key(target.getUuid());
        bans.put(key, PunishmentEntry.permanent(target.getName().getString(), reason, actor));
        saveAll();
    }

    public void mute(ServerPlayerEntity target, String reason, String actor) {
        String key = key(target.getUuid());
        mutes.put(key, PunishmentEntry.permanent(target.getName().getString(), reason, actor));
        saveAll();
    }

    public void tempBan(ServerPlayerEntity target, long expiresAtEpochMs, String reason, String actor) {
        String key = key(target.getUuid());
        tempBans.put(key, PunishmentEntry.temporary(target.getName().getString(), reason, actor, expiresAtEpochMs));
        saveAll();
    }

    public void tempMute(ServerPlayerEntity target, long expiresAtEpochMs, String reason, String actor) {
        String key = key(target.getUuid());
        tempMutes.put(key, PunishmentEntry.temporary(target.getName().getString(), reason, actor, expiresAtEpochMs));
        saveAll();
    }

    public boolean unbanByName(String playerName) {
        boolean removed = removeByName(bans, playerName) | removeByName(tempBans, playerName);
        if (removed) {
            saveAll();
        }
        return removed;
    }

    public boolean unmuteByName(String playerName) {
        boolean removed = removeByName(mutes, playerName) | removeByName(tempMutes, playerName);
        if (removed) {
            saveAll();
        }
        return removed;
    }

    public boolean unTempBanByName(String playerName) {
        boolean removed = removeByName(tempBans, playerName);
        if (removed) {
            saveAll();
        }
        return removed;
    }

    public boolean unTempMuteByName(String playerName) {
        boolean removed = removeByName(tempMutes, playerName);
        if (removed) {
            saveAll();
        }
        return removed;
    }

    public Set<String> getPunishedPlayerNames() {
        cleanupExpired();
        Set<String> names = new HashSet<>();
        collectNames(names, bans);
        collectNames(names, mutes);
        collectNames(names, tempBans);
        collectNames(names, tempMutes);
        return names;
    }

    public boolean isBanned(ServerPlayerEntity player) {
        cleanupExpired();
        return bans.containsKey(key(player.getUuid())) || tempBans.containsKey(key(player.getUuid()));
    }

    public boolean isMuted(ServerPlayerEntity player) {
        cleanupExpired();
        return mutes.containsKey(key(player.getUuid())) || tempMutes.containsKey(key(player.getUuid()));
    }

    public String getBanReason(ServerPlayerEntity player) {
        String key = key(player.getUuid());
        PunishmentEntry entry = bans.getOrDefault(key, tempBans.get(key));
        return entry == null ? "" : entry.reason;
    }

    public String getMuteReason(ServerPlayerEntity player) {
        String key = key(player.getUuid());
        PunishmentEntry entry = mutes.getOrDefault(key, tempMutes.get(key));
        return entry == null ? "" : entry.reason;
    }

    public long getBanExpiry(ServerPlayerEntity player) {
        PunishmentEntry entry = tempBans.get(key(player.getUuid()));
        return entry == null ? -1 : entry.expiresAt;
    }

    public long getMuteExpiry(ServerPlayerEntity player) {
        PunishmentEntry entry = tempMutes.get(key(player.getUuid()));
        return entry == null ? -1 : entry.expiresAt;
    }

    private void cleanupExpired() {
        long now = Instant.now().toEpochMilli();
        tempBans.entrySet().removeIf(entry -> entry.getValue().expiresAt > 0 && entry.getValue().expiresAt <= now);
        tempMutes.entrySet().removeIf(entry -> entry.getValue().expiresAt > 0 && entry.getValue().expiresAt <= now);
    }

    private void loadInto(Path file, Map<String, PunishmentEntry> target) throws IOException {
        target.clear();
        if (!Files.exists(file)) {
            return;
        }

        String json = Files.readString(file, StandardCharsets.UTF_8);
        Map<String, PunishmentEntry> loaded = GSON.fromJson(json, MAP_TYPE);
        if (loaded != null) {
            target.putAll(loaded);
        }
    }

    private void saveAll() {
        try {
            Files.createDirectories(punishmentsDir);
            Files.writeString(bansFile, GSON.toJson(bans), StandardCharsets.UTF_8);
            Files.writeString(mutesFile, GSON.toJson(mutes), StandardCharsets.UTF_8);
            Files.writeString(tempBansFile, GSON.toJson(tempBans), StandardCharsets.UTF_8);
            Files.writeString(tempMutesFile, GSON.toJson(tempMutes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            AdminMod.LOGGER.error("Failed to save punishments", e);
        }
    }

    private String key(UUID uuid) {
        return uuid.toString();
    }

    private boolean removeByName(Map<String, PunishmentEntry> map, String playerName) {
        return map.entrySet().removeIf(entry -> entry.getValue().playerName != null
                && entry.getValue().playerName.equalsIgnoreCase(playerName));
    }

    private void collectNames(Set<String> names, Map<String, PunishmentEntry> map) {
        for (PunishmentEntry entry : map.values()) {
            if (entry.playerName != null && !entry.playerName.isBlank()) {
                names.add(entry.playerName);
            }
        }
    }

    public static class PunishmentEntry {
        public String playerName;
        public String reason;
        public String actor;
        public long createdAt;
        public long expiresAt;

        public static PunishmentEntry permanent(String playerName, String reason, String actor) {
            PunishmentEntry entry = new PunishmentEntry();
            entry.playerName = playerName;
            entry.reason = reason;
            entry.actor = actor;
            entry.createdAt = Instant.now().toEpochMilli();
            entry.expiresAt = -1;
            return entry;
        }

        public static PunishmentEntry temporary(String playerName, String reason, String actor, long expiresAtEpochMs) {
            PunishmentEntry entry = permanent(playerName, reason, actor);
            entry.expiresAt = expiresAtEpochMs;
            return entry;
        }
    }
}