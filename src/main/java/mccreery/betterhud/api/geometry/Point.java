package mccreery.betterhud.api.geometry;

import java.util.Objects;

/**
 * Immutable 2D point of doubles.
 */
public final class Point {
    public static final Point ZERO = new Point(0, 0);

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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
    public double distanceSquared(Point point) {
        double dx = x - point.x;
        double dy = y - point.y;
        return dx * dx + dy * dy;
    }

    /**
     * Returns the Euclidean distance between this and another point.
     * @see #distanceSquared(Point)
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    public Point round() {
        return new Point(Math.round(x), Math.round(y));
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
        return Objects.hash(x, y);
    }
}
