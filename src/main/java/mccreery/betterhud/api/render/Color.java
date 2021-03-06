package mccreery.betterhud.api.render;

/**
 * Color with integer RGBA components in the range 0-255. The alpha component is opacity.
 */
public final class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * The alpha component defaults to 255.
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    /**
     * Converts from Minecraft's internal ARGB packed format (with alpha in the highest bits).
     */
    public static Color fromPackedArgb(int packedArgb) {
        int alpha = packedArgb >> 24;
        int red = packedArgb >> 16 & 0xff;
        int green = packedArgb >> 8 & 0xff;
        int blue = packedArgb & 0xff;

        return new Color(red, green, blue, alpha);
    }

    /**
     * Converts to Minecraft's internal ARGB packed format (with alpha in the highest bits).
     */
    public int toPackedArgb() {
        return alpha << 24 | red << 16 | red << 8 | blue;
    }
}
