package mccreery.betterhud.api.layout;

/**
 * Values corresponding to points in a regular 3x3 grid for rectangle and point alignment.
 */
public enum Anchor {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    CENTER_LEFT,
    CENTER,
    CENTER_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT;

    /**
     * Returns an anchor point in a rectangle. The anchor points lie on the minimum, center and maximum points of the
     * rectangle on each axis, forming a 3x3 grid. The center point is truncated.
     */
    public static Point getAnchorPoint(Rectangle rectangle, Anchor anchor) {
        int x;
        switch (anchor.ordinal() % 3) {
            case 0: x = rectangle.getX(); break;
            case 1: x = rectangle.getX() + rectangle.getWidth() / 2; break;
            case 2: x = rectangle.getX() + rectangle.getWidth(); break;
            default: throw new IllegalArgumentException("Anchor value " + anchor);
        }

        int y;
        switch (anchor.ordinal() / 3) {
            case 0: y = rectangle.getY(); break;
            case 1: y = rectangle.getY() + rectangle.getHeight() / 2; break;
            case 2: y = rectangle.getY() + rectangle.getHeight(); break;
            default: throw new IllegalArgumentException("Anchor value " + anchor);
        }

        return new Point(x, y);
    }
}
