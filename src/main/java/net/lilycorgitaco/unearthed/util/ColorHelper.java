package net.lilycorgitaco.unearthed.util;

import net.minecraft.util.math.MathHelper;

public class ColorHelper {
    private static float getRed(final int hex) {
        return ((hex >> 16) & 0xFF) / 255f;
    }

    private static float getGreen(final int hex) {
        return ((hex >> 8) & 0xFF) / 255f;
    }

    private static float getBlue(final int hex) {
        return ((hex) & 0xFF) / 255f;
    }

    private static float getAlpha(final int hex) {
        return ((hex >> 24) & 0xff) / 255f;
    }

    public static float[] getARGB(final int hex) {
        return new float[]{getAlpha(hex), getRed(hex), getGreen(hex), getBlue(hex)};
    }

    public static int toInt(final float[] argb) {
        final int r = (int) Math.floor(argb[1] * 255) & 0xFF;
        final int g = (int) Math.floor(argb[2] * 255) & 0xFF;
        final int b = (int) Math.floor(argb[3] * 255) & 0xFF;
        final int a = (int) Math.floor(argb[0] * 255) & 0xFF;
        return (a << 24) + (r << 16) + (g << 8) + (b);
    }

    private static float[] getAHSL(final int hex) {
        return ARGBtoAHSL(getARGB(hex));
    }

    private static float[] getAHSV(final int hex) {
        return ARGBtoAHSV(getARGB(hex));
    }

    private static float[] AHSLtoARGB(final float[] ahsl) {
        return new float[]{ahsl[0], convFunction(ahsl, 0), convFunction(ahsl, 8), convFunction(ahsl, 4)};
    }

    private static float convFunction(final float[] ahsl, int n) {
        float k = ((n + ahsl[1] / 30) % 12);
        float a = ahsl[2] * Math.min(ahsl[3], 1 - ahsl[3]);
        return ahsl[3] - a * Math.max(-1, Math.min(k - 3, Math.min(9 - k, 1)));
    }


    private static float[] AHSVtoARGB(final float[] ahsv) {
        return new float[]{ahsv[0], convFunction2(ahsv, 5), convFunction2(ahsv, 3), convFunction2(ahsv, 1)};
    }

    private static float convFunction2(final float[] ahsv, int n) {
        float k = ((n + ahsv[1] / 60) % 6);
        return ahsv[3] - ahsv[3] * ahsv[2] * Math.max(0, Math.min(k, Math.min(4 - k, 1)));
    }

    private static float[] ARGBtoAHSL(final float[] argb) {
        float r = argb[1];
        float g = argb[2];
        float b = argb[3];
        float value = Math.max(r, Math.max(g, b));
        float minX = Math.min(r, Math.min(g, b));
        float chroma = value - minX;
        float lightness = (value + minX) / 2;
        float hue = getHue(r, g, b, value, chroma);
        float saturationL = getSaturationL(value, lightness);
        return new float[]{argb[0], hue, saturationL, lightness};
    }

    private static float[] ARGBtoAHSV(final float[] argb) {
        float r = argb[1];
        float g = argb[2];
        float b = argb[3];
        float value = Math.max(r, Math.max(g, b));
        float minX = Math.min(r, Math.min(g, b));
        float chroma = value - minX;
        float hue = getHue(r, g, b, value, chroma);
        float saturationV = getSaturationV(value, chroma);
        return new float[]{argb[0], hue, saturationV, value};
    }

    private static float getHue(float r, float g, float b, float v, float c) {
        if (c == 0) {
            return 0;
        } else if (v == r) {
            return 60 * (g - b) / c;
        } else if (v == g) {
            return 60 * (2 + (b - r) / c);
        } else {
            return 60 * (4 + (r - g) / c);
        }
    }

    private static float getSaturationL(float value, float lightness) {
        if (lightness == 0 || lightness == 1) {
            return 0;
        } else {
            return (value - lightness) / Math.min(lightness, 1 - lightness);
        }
    }

    private static float getSaturationV(float value, float chroma) {
        if (value == 0) {
            return 0;
        } else {
            return chroma / value;
        }
    }

    public static int blend(final int color1, final int color2, final float ratio) {
        final float ir = 1.0f - ratio;

        final float[] rgb1 = getARGB(color2);
        final float[] rgb2 = getARGB(color1);

        return toInt(new float[]{rgb1[0] * ratio + rgb2[0] * ir, rgb1[1] * ratio + rgb2[1] * ir, rgb1[2] * ratio + rgb2[2] * ir, rgb1[3] * ratio + rgb2[3] * ir});
    }

    public static int shiftColorHSL(final int color, float hueChange, float saturationChange, float luminanceChange) {
        final float[] hsl = getAHSL(color);
        return toInt(AHSLtoARGB(new float[]{hsl[0], (hsl[1] + hueChange) % 360, MathHelper.clamp(hsl[2] + saturationChange, 0, 1), MathHelper.clamp(hsl[3] + luminanceChange, 0, 1)}));
    }

    public static int shiftColorHSV(final int color, float hueChange, float saturationChange, float valueChange) {
        final float[] hsv = getAHSV(color);
        return toInt(AHSVtoARGB(new float[]{hsv[0], (hsv[1] + hueChange) % 360, MathHelper.clamp(hsv[2] + saturationChange, 0, 1), MathHelper.clamp(hsv[3] + valueChange, 0, 1)}));
    }
}
