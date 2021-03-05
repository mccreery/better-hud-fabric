package mccreery.betterhud.api.geometry;

/**
 * Immutable 2D integer rectangle.
 */
public final class Rectangle {
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Point position, Point size) {
        this(position.getX(), position.getY(), size.getX(), size.getY());
    }

    private final int x;

    public int getX() {
        return x;
    }

    private final int y;

    public int getY() {
        return y;
    }

    private final int width;

    public int getWidth() {
        return width;
    }

    private final int height;

    public int getHeight() {
        return height;
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Point getSize() {
        return new Point(width, height);
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
        return 31 * (31 * (31 * (31 + x) + y) + width) + height;
    }
}
