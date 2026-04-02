package net.russianphonks.adminmod.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.russianphonks.adminmod.AdminMod;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PermissionManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Set<String>> playerPermissions;
    private Path permissionsFile;

    public PermissionManager() {
        this.playerPermissions = new HashMap<>();
        this.permissionsFile = new File("config/adminmod/permissions.json").toPath();
    }

    public void load(MinecraftServer server) {
        try {
            if (Files.exists(permissionsFile)) {
                String content = Files.readString(permissionsFile, StandardCharsets.UTF_8);
                Map<String, ? extends Collection<String>> data = GSON.fromJson(content,
                        new TypeToken<Map<String, Collection<String>>>(){}.getType());
                playerPermissions.clear();
                for (Map.Entry<String, ? extends Collection<String>> entry : data.entrySet()) {
                    playerPermissions.put(entry.getKey(), new HashSet<>(entry.getValue()));
                }
                AdminMod.LOGGER.info("Loaded permissions for {} players", playerPermissions.size());
            } else {
                AdminMod.LOGGER.info("No permissions file found, starting fresh");
            }
        } catch (IOException e) {
            AdminMod.LOGGER.error("Failed to load permissions", e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(permissionsFile.getParent());
            String json = GSON.toJson(playerPermissions);
            Files.writeString(permissionsFile, json, StandardCharsets.UTF_8);
            AdminMod.LOGGER.info("Permissions saved");
        } catch (IOException e) {
            AdminMod.LOGGER.error("Failed to save permissions", e);
        }
    }

    public void grantPermission(String playerName, String permission) {
        playerPermissions.computeIfAbsent(playerName, k -> new HashSet<>()).add(permission);
        save();
    }

    public void revokePermission(String playerName, String permission) {
        Set<String> perms = playerPermissions.get(playerName);
        if (perms != null) {
            perms.remove(permission);
            if (perms.isEmpty()) {
                playerPermissions.remove(playerName);
            }
            save();
        }
    }

    public boolean hasPermission(String playerName, String permission) {
        Set<String> perms = playerPermissions.get(playerName);
        return perms != null && perms.contains(permission);
    }

    public Set<String> getPermissions(String playerName) {
        return playerPermissions.getOrDefault(playerName, Collections.emptySet());
    }
}
