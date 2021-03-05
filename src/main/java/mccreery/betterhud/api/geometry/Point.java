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
