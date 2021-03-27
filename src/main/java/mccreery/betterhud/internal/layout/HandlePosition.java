package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

/**
 * Limited set of interpolation parameters for handles.
 */
public enum HandlePosition {
    TOP_LEFT(Rectangle.TOP_LEFT),
    TOP_CENTER(Rectangle.TOP_CENTER),
    TOP_RIGHT(Rectangle.TOP_RIGHT),
    CENTER_LEFT(Rectangle.CENTER_LEFT),
    CENTER(Rectangle.CENTER),
    CENTER_RIGHT(Rectangle.CENTER_RIGHT),
    BOTTOM_LEFT(Rectangle.BOTTOM_LEFT),
    BOTTOM_CENTER(Rectangle.BOTTOM_CENTER),
    BOTTOM_RIGHT(Rectangle.BOTTOM_RIGHT);

    private final Point t;

    HandlePosition(Point t) {
        this.t = t;
    }

    public Point getT() {
        return t;
    }
}
