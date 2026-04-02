package net.russianphonks.adminmod.util;

import net.russianphonks.adminmod.AdminMod;
import net.russianphonks.adminmod.config.ConfigManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LocalizationManager {
    private static final Path LOCALES_DIR = Paths.get("config/locales");
    private static final Map<String, String> EN_FALLBACK = new HashMap<>();
    private static final Map<String, String> ACTIVE = new HashMap<>();
    private static String activeLocale = "en";

    private LocalizationManager() {
    }

    public static void initialize() {
        try {
            Files.createDirectories(LOCALES_DIR);
            Path englishFile = LOCALES_DIR.resolve("locale_en.loc");
            if (!Files.exists(englishFile)) {
                Files.write(englishFile, defaultEnglishLocale(), StandardCharsets.UTF_8);
            }

            EN_FALLBACK.clear();
            EN_FALLBACK.putAll(loadLocaleFile(englishFile));
            setLocale(ConfigManager.getConfig().locale);
        } catch (Exception e) {
            AdminMod.LOGGER.error("Failed to initialize localization", e);
        }
    }

    public static void setLocale(String localeCode) {
        String normalized = localeCode == null || localeCode.isBlank() ? "en" : localeCode.toLowerCase(Locale.ROOT);
        Path localeFile = LOCALES_DIR.resolve("locale_" + normalized + ".loc");

        ACTIVE.clear();
        ACTIVE.putAll(EN_FALLBACK);

        if (Files.exists(localeFile)) {
            try {
                ACTIVE.putAll(loadLocaleFile(localeFile));
                activeLocale = normalized;
                return;
            } catch (Exception e) {
                AdminMod.LOGGER.error("Failed to load locale {}", normalized, e);
            }
        }

        activeLocale = "en";
    }

    public static String getActiveLocale() {
        return activeLocale;
    }

    public static String tr(String key, Object... args) {
        String pattern = ACTIVE.getOrDefault(key, key);
        try {
            return MessageFormat.format(pattern, args);
        } catch (IllegalArgumentException e) {
            return pattern;
        }
    }

    private static Map<String, String> loadLocaleFile(Path path) throws IOException {
        Map<String, String> entries = new HashMap<>();
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            int separator = trimmed.indexOf('=');
            if (separator <= 0) {
                continue;
            }

            String key = trimmed.substring(0, separator).trim();
            String value = trimmed.substring(separator + 1).trim();
            entries.put(key, value);
        }
        return entries;
    }

    private static List<String> defaultEnglishLocale() {
        return List.of(
                "# AdminMod default English locale",
                "help.title=&8---=== &fAdminMod Help &8===-",
                "help.line=/bc <message> &7or &f/bc <tag> <message> &8- &7adminmod.broadcast",
                "help.line.tps=/tps &8- &7adminmod.tps",
                "help.line.invsee=/invsee <player> &8- &7adminmod.invsee",
                "help.line.near=/near [radius] &8- &7adminmod.near",
                "help.line.vanish=/vanish &8- &7adminmod.vanish",
                "help.line.rename=/rename <name> &8- &7adminmod.rename",
                "help.line.lore=/lore <text> &8- &7adminmod.lore",
                "help.line.allow=/allowcommand <player> <permission> &8- &7adminmod.allowcommand",
                "help.line.deny=/denycommand <player> <permission> &8- &7adminmod.allowcommand",
                "help.line.locale=/locale set <code> &8- &7adminmod.locale",
                "help.line.ban=/ban <player> [reason] &8- &7adminmod.ban",
                "help.line.mute=/mute <player> [reason] &8- &7adminmod.mute",
                "help.line.tempban=/tempban <player> <duration> [reason] &8- &7adminmod.tempban",
                "help.line.tempmute=/tempmute <player> <duration> [reason] &8- &7adminmod.tempmute",
                "help.line.unban=/unban <player> &8- &7adminmod.unban",
                "help.line.unmute=/unmute <player> &8- &7adminmod.unmute",
                "help.line.untempban=/untempban <player> &8- &7adminmod.untempban",
                "help.line.untempmute=/untempmute <player> &8- &7adminmod.untempmute",
                "locale.set.success=&aLocale changed to &e{0}",
                "locale.set.missing=&cLocale file not found: &elocale_{0}.loc",
                "punish.default.reason=No reason specified",
                "punish.ban.message=&cYou are banned! &7Reason: &f{0}",
                "punish.tempban.message=&cYou are temporarily banned! &7Reason: &f{0}&7, expires in &e{1}",
                "punish.mute.message=&cYou are muted and cannot use chat or messaging commands.",
                "punish.mute.reason.line=&7Reason: &f{0}",
                "punish.temp.mute.reason.line=&7Reason: &f{0}&7, expires in &e{1}",
                "punish.success.ban=&aBanned &e{0}&a. Reason: &f{1}",
                "punish.success.mute=&aMuted &e{0}&a. Reason: &f{1}",
                "punish.success.tempban=&aTemp-banned &e{0}&a for &e{1}&a. Reason: &f{2}",
                "punish.success.tempmute=&aTemp-muted &e{0}&a for &e{1}&a. Reason: &f{2}",
                "punish.duration.invalid=&cInvalid duration format. Example: 10m, 2h, 1d12h"
        );
    }
}