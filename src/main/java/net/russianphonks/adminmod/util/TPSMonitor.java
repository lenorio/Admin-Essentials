package net.russianphonks.adminmod.util;

import net.minecraft.server.MinecraftServer;

public final class TPSMonitor {
    private static final int[] AVERAGES = new int[]{5 * 20, 60 * 20, 5 * 60 * 20, 15 * 60 * 20};

    private TPSMonitor() {
    }

    public static double[] getAverageTPS(MinecraftServer server) {
        long[] times = server.getTickTimes();
        double[] tps = new double[4];

        for (int i = 0; i < AVERAGES.length; i++) {
            long sum = 0;
            int count = Math.min(AVERAGES[i], times.length);
            if (count == 0) {
                tps[i] = 20.0;
                continue;
            }

            for (int j = 0; j < count; j++) {
                sum += times[j];
            }

            double avgNs = (double) sum / count;
            double tpsValue = Math.min(20.0, 20.0 / (avgNs / 50_000_000.0));
            tps[i] = tpsValue;
        }

        return tps;
    }

    public static double[] getMSPT(MinecraftServer server) {
        long[] times = server.getTickTimes();
        if (times.length == 0) return new double[]{0, 0, 0};

        // 5s (100 ticks)
        long sum5s = 0;
        int count5s = Math.min(100, times.length);
        long min5s = Long.MAX_VALUE;
        long max5s = 0;
        for (int i = 0; i < count5s; i++) {
            sum5s += times[i];
            min5s = Math.min(min5s, times[i]);
            max5s = Math.max(max5s, times[i]);
        }

        // 10s (200 ticks)
        long sum10s = 0;
        int count10s = Math.min(200, times.length);
        long min10s = Long.MAX_VALUE;
        long max10s = 0;
        for (int i = 0; i < count10s; i++) {
            sum10s += times[i];
            min10s = Math.min(min10s, times[i]);
            max10s = Math.max(max10s, times[i]);
        }

        // 60s (1200 ticks)
        long sum60s = 0;
        int count60s = Math.min(1200, times.length);
        long min60s = Long.MAX_VALUE;
        long max60s = 0;
        for (int i = 0; i < count60s; i++) {
            sum60s += times[i];
            min60s = Math.min(min60s, times[i]);
            max60s = Math.max(max60s, times[i]);
        }

        return new double[]{
                (double) sum5s / count5s / 1_000_000.0,
                (double) sum10s / count10s / 1_000_000.0,
                (double) sum60s / count60s / 1_000_000.0,
                min5s / 1_000_000.0,
                max5s / 1_000_000.0,
                min10s / 1_000_000.0,
                max10s / 1_000_000.0,
                min60s / 1_000_000.0,
                max60s / 1_000_000.0
        };
    }
}
