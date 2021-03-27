package mccreery.betterhud.api.geometry;

import java.util.Objects;

/**
 * Immutable 2D rectangle.
 */
public final class Rectangle {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Point position, Point size) {
        this(position.getX(), position.getY(), size.getX(), size.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getMaxX() {
        return x + width;
    }

    public double getMaxY() {
        return y + height;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Point getSize() {
        return new Point(width, height);
    }

    /**
     * Tests whether a point lies between the minimum (inclusive) and maximum (exclusive) corners of this rectangle.
     * @return {@code true} if the point is inside.
     */
    public boolean contains(Point point) {
        return point.getX() >= x && point.getY() >= y && point.getX() < getMaxX() && point.getY() < getMaxY();
    }

    /**
     * Tests whether this rectangle intersects at any point with another.
     * @return {@code true} if any point is inside both rectangles.
     */
    public boolean overlaps(Rectangle rectangle) {
        return x < rectangle.getMaxX() && getMaxX() > rectangle.x &&
                y < rectangle.getMaxY() && getMaxY() > rectangle.y;
    }

    /**
     * Interpolation parameter.
     */
    public static final Point
            TOP_LEFT = new Point(0.0, 0.0),
            TOP_CENTER = new Point(0.5, 0.0),
            TOP_RIGHT = new Point(1.0, 0.0),
            CENTER_LEFT = new Point(0.0, 0.5),
            CENTER = new Point(0.5, 0.5),
            CENTER_RIGHT = new Point(1.0, 0.5),
            BOTTOM_LEFT = new Point(0.0, 1.0),
            BOTTOM_CENTER = new Point(0.5, 1.0),
            BOTTOM_RIGHT = new Point(1.0, 1.0);

    /**
     * Interpolates between the minimum and maximum points.
     * @param t The linear interpolation parameter.
     */
    public Point interpolate(Point t) {
        return getPosition().add(getSize().scale(t));
    }

    /**
     * Aligns a point inside the rectangle with an anchor point.
     * @param anchor The fixed anchor point.
     * @param t The linear interpolation parameter.
     * @return A new rectangle.
     */
    public Rectangle align(Point anchor, Point t) {
        return new Rectangle(anchor.subtract(getSize().scale(t)), getSize());
    }

    /**
     * Aligns a point inside the rectangle with the corresponding point in a container.
     * @param container The container.
     * @param t The linear interpolation parameter.
     * @return A new rectangle.
     */
    public Rectangle align(Rectangle container, Point t) {
        return align(container.interpolate(t), t);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)object;
            return rectangle.x == x && rectangle.y == y && rectangle.width == width && rectangle.height == height;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }
}
