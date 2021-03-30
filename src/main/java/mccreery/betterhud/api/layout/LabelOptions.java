package mccreery.betterhud.api.layout;

import mccreery.betterhud.internal.render.Color;

/**
 * Reusable options for {@link Label}, which are passed to the vanilla text renderer.
 */
public final class LabelOptions {
    public static final LabelOptions DEFAULT = new LabelOptions(
            Color.WHITE, Color.TRANSPARENT, true, false, packLight(240, 240));

    private Color color;
    private Color backgroundColor;

    private boolean shadow;
    private boolean seeThrough;
    private int light;

    private LabelOptions(Color color, Color backgroundColor, boolean shadow, boolean seeThrough, int light) {
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.shadow = shadow;
        this.seeThrough = seeThrough;
        this.light = light;
    }

    private LabelOptions(LabelOptions options) {
        this(options.color, options.backgroundColor, options.shadow, options.seeThrough, options.light);
    }

    public Color getColor() {
        return color;
    }

    public LabelOptions withColor(Color color) {
        LabelOptions options = new LabelOptions(this);
        options.color = color;
        return options;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public LabelOptions withBackgroundColor(Color backgroundColor) {
        LabelOptions options = new LabelOptions(this);
        options.backgroundColor = backgroundColor;
        return options;
    }

    public boolean getShadow() {
        return shadow;
    }

    public LabelOptions withShadow(boolean shadow) {
        LabelOptions options = new LabelOptions(this);
        options.shadow = shadow;
        return options;
    }

    public boolean getSeeThrough() {
        return seeThrough;
    }

    public LabelOptions withSeeThrough(boolean seeThrough) {
        LabelOptions options = new LabelOptions(this);
        options.seeThrough = seeThrough;
        return options;
    }

    public int getLight() {
        return light;
    }

    public LabelOptions withLight(int light) {
        LabelOptions options = new LabelOptions(this);
        options.light = light;
        return options;
    }

    public static int packLight(int u, int v) {
        return u & 0xffff | v << 16;
    }
}
