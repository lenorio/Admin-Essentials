package net.russianphonks.adminmod.util;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ColorUtil {
    public static Text colorize(String text) {
        // Replace & codes with § codes for Minecraft formatting
        String result = text;
        
        // Replace color codes
        result = result.replace("&0", "§0");
        result = result.replace("&1", "§1");
        result = result.replace("&2", "§2");
        result = result.replace("&3", "§3");
        result = result.replace("&4", "§4");
        result = result.replace("&5", "§5");
        result = result.replace("&6", "§6");
        result = result.replace("&7", "§7");
        result = result.replace("&8", "§8");
        result = result.replace("&9", "§9");
        result = result.replace("&a", "§a");
        result = result.replace("&b", "§b");
        result = result.replace("&c", "§c");
        result = result.replace("&d", "§d");
        result = result.replace("&e", "§e");
        result = result.replace("&f", "§f");

        // Replace formatting codes
        result = result.replace("&l", "§l");
        result = result.replace("&n", "§n");
        result = result.replace("&o", "§o");
        result = result.replace("&r", "§r");

        return Text.literal(result);
    }

    public static String getProgressBar(double current, double max, int length, String fullChar, String emptyChar, String color) {
        if (max <= 0) max = 1;
        double percentage = Math.min(current / max, 1.0);
        int filled = (int) (length * percentage);

        StringBuilder sb = new StringBuilder();
        sb.append(color);
        for (int i = 0; i < filled; i++) {
            sb.append(fullChar);
        }
        sb.append("&7");
        for (int i = filled; i < length; i++) {
            sb.append(emptyChar);
        }
        return sb.toString();
    }

    public static String getRamBar(long used, long max) {
        double percentage = (double) used / max;
        String color;

        if (percentage < 0.5) {
            color = "&a"; // Green
        } else if (percentage < 0.8) {
            color = "&e"; // Yellow
        } else {
            color = "&c"; // Red
        }

        return getProgressBar(used, max, 30, "█", "░", color);
    }

    public static String getCpuBar(double percentage) {
        String color;
        if (percentage < 50) {
            color = "&a"; // Green
        } else if (percentage < 80) {
            color = "&e"; // Yellow
        } else {
            color = "&c"; // Red
        }

        return getProgressBar(percentage, 100, 30, "█", "░", color);
    }
}
