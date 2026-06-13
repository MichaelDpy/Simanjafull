package com.simanja.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class untuk format mata uang Rupiah
 */
public class CurrencyFormatter {

    private static final NumberFormat formatter =
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public static String format(double amount) {
        return formatter.format(amount);
    }

    public static String formatShort(double amount) {
        if (amount >= 1_000_000) {
            return String.format("Rp %.1fJt", amount / 1_000_000);
        } else if (amount >= 1_000) {
            return String.format("Rp %.0fRb", amount / 1_000);
        }
        return format(amount);
    }
}
