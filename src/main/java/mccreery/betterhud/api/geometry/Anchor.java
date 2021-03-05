package mccreery.betterhud.api.geometry;

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
     *
     * <p>This method assumes "left" refers to the minimum X coordinate and "top" refers to the minimum Y coordinate.
     */
    public static Point getAnchorPoint(Rectangle rectangle, Anchor anchor) {
        return rectangle.getPosition().add(getAnchorPoint(rectangle.getSize(), anchor));
    }

    /**
     * Returns an anchor point in a rectangle positioned at the origin.
     * <p>This method assumes "left" refers to the minimum X coordinate and "top" refers to the minimum Y coordinate.
     *
     * @see #getAnchorPoint(Rectangle, Anchor)
     */
    public static Point getAnchorPoint(Point size, Anchor anchor) {
        int x;
        switch (anchor.ordinal() % 3) {
            case 0: x = 0; break;
            case 1: x = size.getX() / 2; break;
            case 2: x = size.getX(); break;
            default: throw new IllegalArgumentException("Anchor value " + anchor);
        }

        int y;
        switch (anchor.ordinal() / 3) {
            case 0: y = 0; break;
            case 1: y = size.getY() / 2; break;
            case 2: y = size.getY(); break;
            default: throw new IllegalArgumentException("Anchor value " + anchor);
        }

        return new Point(x, y);
    }

    /**
     * Returns a rectangle with one of its anchors aligned with an anchor point.
     * <p>This method assumes "left" refers to the minimum X coordinate and "top" refers to the minimum Y coordinate.
     *
     * @param anchorPoint The fixed anchor point
     * @param anchor The anchor within the new rectangle to align to the anchor point
     * @param size The size of the new rectangle
     */
    public static Rectangle getAlignedRectangle(Point anchorPoint, Anchor anchor, Point size) {
        return new Rectangle(anchorPoint.subtract(getAnchorPoint(size, anchor)), size);
    }
}
