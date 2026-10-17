package com.acme.salary.common;

import java.util.List;

public final class Stats {
    private Stats() {}

    /** Median of an already-sorted list. Returns 0 for an empty list. */
    public static long median(List<Long> sorted) {
        int n = sorted.size();
        if (n == 0) return 0;
        if (n % 2 == 1) return sorted.get(n / 2);
        return Math.round((sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0);
    }
}
