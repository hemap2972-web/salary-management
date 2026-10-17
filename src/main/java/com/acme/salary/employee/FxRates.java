package com.acme.salary.employee;

import com.acme.salary.common.BadRequestException;
import java.util.Map;
import java.util.Set;

/** Static FX table (units of currency per 1 USD). Deliberate simplification, see docs/tradeoffs.md. */
public final class FxRates {
    private static final Map<String, Double> PER_USD = Map.of(
            "USD", 1.0, "INR", 83.0, "GBP", 0.79, "EUR", 0.92,
            "CAD", 1.36, "AUD", 1.52, "SGD", 1.34, "JPY", 150.0);

    private FxRates() {}

    public static Set<String> supported() { return PER_USD.keySet(); }

    public static long toUsd(long amount, String currency) {
        return Math.round(amount / rate(currency));
    }

    public static long fromUsd(double usd, String currency) {
        return Math.round(usd * rate(currency));
    }

    private static double rate(String currency) {
        Double r = currency == null ? null : PER_USD.get(currency.toUpperCase());
        if (r == null) throw new BadRequestException("Unsupported currency: " + currency);
        return r;
    }
}
