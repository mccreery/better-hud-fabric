package mccreery.betterhud.api.geometry;

/**
 * Immutable 2D integer point.
 */
public final class Point {
    public static final Point ZERO = new Point(0, 0);

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private final int x;

    public int getX() {
        return x;
    }

    private final int y;

    public int getY() {
        return y;
    }

    public Point add(Point point) {
        return new Point(x + point.x, y + point.y);
    }

    public Point subtract(Point point) {
        return new Point(x - point.x, y - point.y);
    }

    /**
     * Returns the squared Euclidean distance between this and another point.
     * <p>This is more efficient than {@link #distance(Point)} and retains the order of distances, so it can be used
     * in place of distance in cases such as range checking.
     */
    public int distanceSquared(Point point) {
        int dx = x - point.getX();
        int dy = y - point.getY();
        return dx * dx + dy * dy;
    }

    /**
     * Returns the Euclidean distance between this and another point.
     * @see #distanceSquared(Point)
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object instanceof Point) {
            Point point = (Point)object;
            return point.x == x && point.y == y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * (31 + x) + y;
    }
}
