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

    public Point scale(Point point) {
        return new Point(x * point.x, y * point.y);
    }

    public Point scale(double factor) {
        return new Point(x * factor, y * factor);
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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Returns the component-wise minimum between two points.
     *
     * @param a The first point.
     * @param b The second point.
     * @return The component-wise minimum between two points.
     */
    public static Point min(Point a, Point b) {
        return new Point(
            Math.min(a.getX(), b.getX()),
            Math.min(a.getY(), b.getY())
        );
    }

    /**
     * Returns the component-wise maximum between two points.
     *
     * @param a The first point.
     * @param b The second point.
     * @return The component-wise maximum between two points.
     */
    public static Point max(Point a, Point b) {
        return new Point(
            Math.max(a.getX(), b.getX()),
            Math.max(a.getY(), b.getY())
        );
    }

    /**
     * Individually clamps the X and Y components of a point between a minimum and a maximum.
     * @param min The lower bound.
     * @param max The upper bound.
     * @param point The point to clamp.
     * @return The point {@code point} clamped between {@code min} and {@code max}.
     * @throws IllegalArgumentException If min > max in either component.
     */
    public static Point clamp(Point min, Point max, Point point) {
        if (min.getX() > max.getX() || min.getY() > max.getY()) {
            throw new IllegalArgumentException("min > max");
        }

        return new Point(
            Math.max(min.getX(), Math.min(max.getX(), point.getX())),
            Math.max(min.getY(), Math.min(max.getY(), point.getY()))
        );
    }

    /**
     * Clamps the X and Y components of a point so it is inside a rectangle.
     * @param bounds The bounds.
     * @param point The point to clamp.
     * @return The point {@code point} clamped between the edges of {@code bounds}.
     */
    public static Point clamp(Rectangle bounds, Point point) {
        return clamp(bounds.getPosition(), bounds.getPosition().add(bounds.getSize()), point);
    }
}
