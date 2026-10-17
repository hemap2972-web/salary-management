package com.acme.salary.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class StatsTest {

    @Test
    void shouldReturnMiddleValueForOddCount() {
        assertEquals(20, Stats.median(List.of(10L, 20L, 30L)));
    }

    @Test
    void shouldReturnAverageOfMiddleTwoForEvenCount() {
        assertEquals(25, Stats.median(List.of(10L, 20L, 30L, 40L)));
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        assertEquals(0, Stats.median(List.of()));
    }
}
