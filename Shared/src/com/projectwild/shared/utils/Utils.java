package com.projectwild.shared.utils;

public class Utils {

    public static int clamp(int max, int min, int value) {
        return (int) clamp((double) max, (double) min, (double) value);
    }

    public static double clamp(double max, double min, double value) {
        return Math.max(min, Math.min(max, value));
    }

    public static double lerp(double a, double b, double f)
    {
        return a + f * (b - a);
    }

}
