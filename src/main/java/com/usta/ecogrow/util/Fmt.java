package com.usta.ecogrow.util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Fmt {
    private static final Locale ES_CO = Locale.of("es", "CO");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd MMM yyyy", ES_CO);

    private Fmt() {}

    public static String date(LocalDate d) {
        return d == null ? "—" : DATE.format(d);
    }

    public static String number(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(ES_CO);
        nf.setMaximumFractionDigits(2);
        return nf.format(v);
    }

    public static String integer(long v) {
        return NumberFormat.getIntegerInstance(ES_CO).format(v);
    }

    public static String percent(double v) {
        return number(v) + " %";
    }

    public static String orDash(String s) {
        return s == null || s.isBlank() ? "—" : s;
    }
}
