package net.russianphonks.adminmod.util;

import net.minecraft.text.Text;

public class ColorFormatter {
    public static String formatColors(String text) {
        return text
            .replace("&0", "§0")
            .replace("&1", "§1")
            .replace("&2", "§2")
            .replace("&3", "§3")
            .replace("&4", "§4")
            .replace("&5", "§5")
            .replace("&6", "§6")
            .replace("&7", "§7")
            .replace("&8", "§8")
            .replace("&9", "§9")
            .replace("&a", "§a")
            .replace("&b", "§b")
            .replace("&c", "§c")
            .replace("&d", "§d")
            .replace("&e", "§e")
            .replace("&f", "§f")
            .replace("&l", "§l")
            .replace("&m", "§m")
            .replace("&n", "§n")
            .replace("&o", "§o")
            .replace("&r", "§r");
    }

    public static Text coloredText(String text) {
        return Text.literal(formatColors(text));
    }

    public static String createProgressBar(int filled, int total, String filledChar, String emptyChar) {
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < total; i++) {
            bar.append(i < filled ? filledChar : emptyChar);
        }
        bar.append("]");
        return bar.toString();
    }

    public static String createColoredProgressBar(double percentage) {
        int filled = (int) (percentage / 5);
        int empty = 20 - filled;
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < filled; i++) {
            bar.append("§2█");
        }
        for (int i = 0; i < empty; i++) {
            bar.append("§7█");
        }
        bar.append("§r]");
        
        return bar.toString();
    }
}
