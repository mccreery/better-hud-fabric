package mccreery.betterhud.api.geometry;

/**
 * Values corresponding to points in a regular 3x3 grid for rectangle and point alignment.
 */
public enum Anchor {
    TOP_LEFT(0.0, 0.0),
    TOP_CENTER(0.5, 0.0),
    TOP_RIGHT(1.0, 0.0),
    CENTER_LEFT(0.0, 0.5),
    CENTER(0.5, 0.5),
    CENTER_RIGHT(1.0, 0.5),
    BOTTOM_LEFT(0.0, 1.0),
    BOTTOM_CENTER(0.5, 1.0),
    BOTTOM_RIGHT(1.0, 1.0);

    private final double xt;
    private final double yt;

    /**
     * @param xt The horizontal linear interpolation parameter.
     * @param yt The vertical linear interpolation parameter.
     */
    Anchor(double xt, double yt) {
        this.xt = xt;
        this.yt = yt;
    }

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
        return new Point(size.getX() * anchor.xt, size.getY() * anchor.yt);
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
