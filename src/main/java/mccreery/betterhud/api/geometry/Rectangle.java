package mccreery.betterhud.api.geometry;

import java.util.Objects;

/**
 * Immutable 2D integer rectangle.
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
     * More intuitive alias for {@link Anchor#getAnchorPoint(Rectangle, Anchor)}.
     */
    public Point getAnchorPoint(Anchor anchor) {
        return Anchor.getAnchorPoint(this, anchor);
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
