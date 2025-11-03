package com.bleudev.ppl_utils.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LangUtils {
    private static double precision(@NotNull BigDecimal d, int p) {
        return d.setScale(p, RoundingMode.DOWN).doubleValue();
    }

    public static double round(double x, int p) {
        return precision(new BigDecimal(x), p);
    }
}
