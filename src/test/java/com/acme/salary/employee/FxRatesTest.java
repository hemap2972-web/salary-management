package com.acme.salary.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.acme.salary.common.BadRequestException;
import org.junit.jupiter.api.Test;

class FxRatesTest {

    @Test
    void shouldConvertInrToUsd() {
        assertEquals(100_000, FxRates.toUsd(8_300_000, "INR"));
    }

    @Test
    void shouldTreatUsdAsIdentity() {
        assertEquals(75_000, FxRates.toUsd(75_000, "usd"));
    }

    @Test
    void shouldRejectUnknownCurrency() {
        assertThrows(BadRequestException.class, () -> FxRates.toUsd(1000, "XXX"));
    }
}
