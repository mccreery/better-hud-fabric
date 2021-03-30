package mccreery.betterhud.internal.render;

import java.util.Objects;

/**
 * Color with integer RGBA components in the range 0-255. The alpha component is opacity.
 */
public final class Color {
    public static final Color WHITE = new Color(255, 255, 255);

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public Color(int red, int green, int blue, int alpha) {
        if (red < 0 || red >= 256 ||
                green < 0 || green >= 256 ||
                blue < 0 || blue >= 256 ||
                alpha < 0 || alpha >= 256) {
            throw new IllegalArgumentException("Components must be 0-255");
        }

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
        int alpha = packedArgb >>> 24;
        int red = packedArgb >>> 16 & 0xff;
        int green = packedArgb >>> 8 & 0xff;
        int blue = packedArgb & 0xff;

        return new Color(red, green, blue, alpha);
    }

    /**
     * Converts to Minecraft's internal ARGB packed format (with alpha in the highest bits).
     */
    public int toPackedArgb() {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object instanceof Color) {
            Color color = (Color)object;
            return color.red == red && color.green == green && color.blue == blue && color.alpha == alpha;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }
}
