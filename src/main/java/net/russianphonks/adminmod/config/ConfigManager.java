package net.russianphonks.adminmod.config;

import com.google.gson.*;
import net.russianphonks.adminmod.AdminMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigManager {
    private static final Path CONFIG_DIR = Paths.get("config/adminmod");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("config.json");
    
    public static class Config {
        public String locale = "en";
        public String console_prefix = "[CONSOLE]";
        public Map<String, String> player_prefixes = new HashMap<>();
        public Map<String, String> broadcast_tags = new HashMap<>();
        public int near_default_radius = 50;
        public Map<String, List<String>> allowed_commands = new HashMap<>();
    }

    private static Config config;

    public static void loadConfig() {
        try {
            Files.createDirectories(CONFIG_DIR);
            
            if (!Files.exists(CONFIG_FILE)) {
                createDefaultConfig();
            }

            String json = new String(Files.readAllBytes(CONFIG_FILE));
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            config = new Config();
            config.locale = obj.has("locale") ? obj.get("locale").getAsString() : "en";
            
            config.console_prefix = obj.has("console_prefix") ? obj.get("console_prefix").getAsString() : "[CONSOLE]";
            config.near_default_radius = obj.has("near_default_radius") ? obj.get("near_default_radius").getAsInt() : 50;
            
            if (obj.has("player_prefixes")) {
                JsonObject prefixes = obj.getAsJsonObject("player_prefixes");
                prefixes.entrySet().forEach(e -> config.player_prefixes.put(e.getKey(), e.getValue().getAsString()));
            }

            if (obj.has("broadcast_tags")) {
                JsonObject tags = obj.getAsJsonObject("broadcast_tags");
                tags.entrySet().forEach(e -> config.broadcast_tags.put(e.getKey(), e.getValue().getAsString()));
            }

            if (config.broadcast_tags.isEmpty()) {
                config.broadcast_tags.put("broadcast", "BROADCAST");
                config.broadcast_tags.put("info", "INFO");
                config.broadcast_tags.put("warn", "WARNING");
                config.broadcast_tags.put("event", "EVENT");
            }
            
            if (obj.has("allowed_commands")) {
                JsonObject commands = obj.getAsJsonObject("allowed_commands");
                commands.entrySet().forEach(e -> {
                    List<String> cmds = new ArrayList<>();
                    e.getValue().getAsJsonArray().forEach(cmd -> cmds.add(cmd.getAsString()));
                    config.allowed_commands.put(e.getKey(), cmds);
                });
            }

            AdminMod.LOGGER.info("Config loaded successfully");
        } catch (Exception e) {
            AdminMod.LOGGER.error("Failed to load config", e);
            config = new Config();
        }
    }

    private static void createDefaultConfig() throws IOException {
        Config defaultConfig = new Config();
        defaultConfig.player_prefixes.put("russianphonks", "[Admin]");
        defaultConfig.broadcast_tags.put("broadcast", "BROADCAST");
        defaultConfig.broadcast_tags.put("info", "INFO");
        defaultConfig.broadcast_tags.put("warn", "WARNING");
        defaultConfig.broadcast_tags.put("event", "EVENT");
        defaultConfig.allowed_commands.put("russianphonks", Arrays.asList(
            "adminmod.broadcast",
            "adminmod.tps",
            "adminmod.invsee",
            "adminmod.near",
            "adminmod.vanish",
            "adminmod.rename",
            "adminmod.lore",
            "adminmod.allowcommand",
            "adminmod.help",
            "adminmod.locale",
            "adminmod.ban",
            "adminmod.mute",
            "adminmod.tempban",
            "adminmod.tempmute",
            "adminmod.unban",
            "adminmod.unmute",
            "adminmod.untempban",
            "adminmod.untempmute"
        ));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(defaultConfig);
        Files.write(CONFIG_FILE, json.getBytes());
    }

    public static Config getConfig() {
        return config;
    }

    public static boolean playerHasCommand(String playerName, String command) {
        List<String> commands = config.allowed_commands.getOrDefault(playerName, new ArrayList<>());
        return commands.contains(command);
    }

    public static void grantCommand(String playerName, String command) {
        config.allowed_commands.computeIfAbsent(playerName, k -> new ArrayList<>()).add(command);
        saveConfig();
    }

    public static void revokeCommand(String playerName, String command) {
        List<String> commands = config.allowed_commands.get(playerName);
        if (commands != null) {
            commands.remove(command);
            saveConfig();
        }
    }

    public static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(config);
            Files.write(CONFIG_FILE, json.getBytes());
        } catch (IOException e) {
            AdminMod.LOGGER.error("Failed to save config", e);
        }
    }
}
