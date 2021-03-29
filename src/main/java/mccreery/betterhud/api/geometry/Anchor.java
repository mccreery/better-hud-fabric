package mccreery.betterhud.api.geometry;

/**
 * Points within a rectangle on a 3x3 grid. These can usually be used where an interpolation parameter is expected.
 */
public enum Anchor {
    TOP_LEFT(new Point(0.0, 0.0)),
    TOP_CENTER(new Point(0.5, 0.0)),
    TOP_RIGHT(new Point(1.0, 0.0)),
    CENTER_LEFT(new Point(0.0, 0.5)),
    CENTER(new Point(0.5, 0.5)),
    CENTER_RIGHT(new Point(1.0, 0.5)),
    BOTTOM_LEFT(new Point(0.0, 1.0)),
    BOTTOM_CENTER(new Point(0.5, 1.0)),
    BOTTOM_RIGHT(new Point(1.0, 1.0));

    private final Point t;

    Anchor(Point t) {
        this.t = t;
    }

    /**
     * Returns the corresponding interpolation parameter.
     * @return The corresponding interpolation parameter.
     */
    public Point getT() {
        return t;
    }
}
