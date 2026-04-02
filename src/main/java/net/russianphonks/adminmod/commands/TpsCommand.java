package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.russianphonks.adminmod.util.ColorUtil;
import net.russianphonks.adminmod.util.PermissionChecker;
import net.russianphonks.adminmod.util.TPSMonitor;

public class TpsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("tps")
                .requires(source -> PermissionChecker.hasPermission(source, "adminmod.tps"))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();

                    long[] tickTimes = source.getServer().getTickTimes();
                    double[] tps = TPSMonitor.getAverageTPS(source.getServer());

                    source.sendFeedback(() -> ColorUtil.colorize("&8---=== &fServer Tick Information &8===-"), false);
                    source.sendFeedback(() -> ColorUtil.colorize(String.format("&fTPS: &e%.2f&f (%s), &e%.2f&f (1m), &e%.2f&f (5m), &e%.2f&f (15m)",
                            tps[0], "5s", tps[1], tps[2], tps[3])), false);

                    double mspt5s = 0, mspt10s = 0, mspt60s = 0;
                    long min5s = Long.MAX_VALUE, max5s = 0;
                    long min10s = Long.MAX_VALUE, max10s = 0;
                    long min60s = Long.MAX_VALUE, max60s = 0;

                    for (int i = 0; i < Math.min(100, tickTimes.length); i++) {
                        mspt5s += tickTimes[i];
                        min5s = Math.min(min5s, tickTimes[i]);
                        max5s = Math.max(max5s, tickTimes[i]);
                    }
                    mspt5s = (mspt5s / Math.min(100, tickTimes.length)) / 1_000_000.0;
                    min5s = min5s / 1_000_000;
                    max5s = max5s / 1_000_000;

                    for (int i = 0; i < Math.min(200, tickTimes.length); i++) {
                        mspt10s += tickTimes[i];
                        min10s = Math.min(min10s, tickTimes[i]);
                        max10s = Math.max(max10s, tickTimes[i]);
                    }
                    mspt10s = (mspt10s / Math.min(200, tickTimes.length)) / 1_000_000.0;
                    min10s = min10s / 1_000_000;
                    max10s = max10s / 1_000_000;

                    for (int i = 0; i < Math.min(1200, tickTimes.length); i++) {
                        mspt60s += tickTimes[i];
                        min60s = Math.min(min60s, tickTimes[i]);
                        max60s = Math.max(max60s, tickTimes[i]);
                    }
                    mspt60s = (mspt60s / Math.min(1200, tickTimes.length)) / 1_000_000.0;
                    min60s = min60s / 1_000_000;
                    max60s = max60s / 1_000_000;

                    final String mspt5sLine = String.format("&a5s &8— &e%.2f&f, &a%d&f, &c%d", mspt5s, min5s, max5s);
                    final String mspt10sLine = String.format("&a10s &8— &e%.2f&f, &a%d&f, &c%d", mspt10s, min10s, max10s);
                    final String mspt60sLine = String.format("&a60s &8— &e%.2f&f, &a%d&f, &c%d", mspt60s, min60s, max60s);
                    source.sendFeedback(() -> ColorUtil.colorize("&fMSPT - Average, Minimum, Maximum"), false);
                    source.sendFeedback(() -> ColorUtil.colorize(mspt5sLine), false);
                    source.sendFeedback(() -> ColorUtil.colorize(mspt10sLine), false);
                    source.sendFeedback(() -> ColorUtil.colorize(mspt60sLine), false);

                    Runtime runtime = Runtime.getRuntime();
                    long maxMemory = runtime.maxMemory() / 1024 / 1024;
                    long totalMemory = runtime.totalMemory() / 1024 / 1024;
                    long freeMemory = runtime.freeMemory() / 1024 / 1024;
                    long usedMemory = totalMemory - freeMemory;

                    double processCpuLoad = com.sun.management.OperatingSystemMXBean.class.isInstance(
                            java.lang.management.ManagementFactory.getOperatingSystemMXBean()) ?
                            ((com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad() * 100 : 0;
                    double systemCpuLoad = java.lang.management.ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 100;

                    source.sendFeedback(() -> ColorUtil.colorize(String.format("&fCPU: &e%.2f%%&f, &e%.2f%%&f (sys, proc.)", systemCpuLoad, processCpuLoad)), false);
                    source.sendFeedback(() -> ColorUtil.colorize(String.format("&fRAM: &e%dM&f/&e%dM&f (max %dM)", usedMemory, totalMemory, maxMemory)), false);

                    String ramBar = ColorUtil.getRamBar(usedMemory, maxMemory);
                    source.sendFeedback(() -> ColorUtil.colorize(ramBar), false);

                    return 1;
                })
        );
    }
}
