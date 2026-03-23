package com.dy.colony.app.utils;

import java.text.DecimalFormat;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/17
 */
public class DecimalFormatUtils {

    public static String twoDecimal(double format) {
        return new DecimalFormat("##0.00").format(format);
    }

    public static String threeDecimal(double format) {
        return new DecimalFormat("##0.000").format(format);
    }
}
