package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

/**
 * An object which can be rendered within a bounding box.
 */
public abstract class LayoutBox {
    private Rectangle bounds;

    private Point defaultSize;
    private Point minSize;
    private Point maxSize;

    public LayoutBox(Point fixedSize) {
        setSizeFixed(fixedSize);
    }

    public LayoutBox(Point defaultSize, Point minSize, Point maxSize) {
        setSizeFlexible(defaultSize, minSize, maxSize);
    }

    /**
     * Returns the preferred size of the object. By default, this is
     * the size negotiated from zero, which usually means the minimum
     * size. This method should return a value which will be accepted
     * under negotiation.
     *
     * @return The preferred size of the object.
     */
    public Point getDefaultSize() {
        return defaultSize;
    }

    protected Point getMinSize() {
        return minSize;
    }

    protected Point getMaxSize() {
        return maxSize;
    }

    /**
     * Sets a fixed size for default, min and max.
     * @param fixedSize The new size.
     */
    protected final void setSizeFixed(Point fixedSize) {
        defaultSize = fixedSize;
        minSize = fixedSize;
        maxSize = fixedSize;
    }

    /**
     * Sets a flexible range of sizes.
     * @param defaultSize The new default size.
     * @param minSize The new minimum size or {@code null} if there is no minimum.
     * @param maxSize The new maximum size or {@code null} if there is no maximum.
     */
    protected final void setSizeFlexible(Point defaultSize, Point minSize, Point maxSize) {
        if (defaultSize.getX() < minSize.getX() || defaultSize.getX() > maxSize.getX()
                || defaultSize.getY() < minSize.getY() || defaultSize.getY() > maxSize.getY()) {
            throw new IllegalArgumentException("Default size is not between min and max sizes");
        }

        this.defaultSize = defaultSize;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * Set the current bounds to a given value.
     * @param bounds The new bounding box.
     * @see #getBounds()
     */
    public final void setBounds(Rectangle bounds) {
        Point size = bounds.getSize();
        if (!negotiateSize(size).equals(size)) {
            throw new IllegalArgumentException("Invalid size");
        }

        this.bounds = bounds;
    }

    /**
     * Gets the current bounds. The initial bounds should be a valid size, and
     * its top-left corner should be the origin.
     *
     * @return The current bounds.
     * @see #setBounds(Rectangle)
     */
    public final Rectangle getBounds() {
        return bounds;
    }

    /**
     * Renders the object inside the current bounds.
     *
     * <p>If the size of the current bounds is not equal to
     * the result of {@code getPreferredSize(bounds.getSize())},
     * this method does not guarantee that the object
     * will be rendered correctly. Implementations may throw an
     * exception if the given bounds are undesirable.
     */
    public abstract void render();

    /**
     * Responds to a size offer with the offer itself or a counter offer.
     * Flexible objects should return the argument, indicating that
     * the offer has been accepted. Fixed size objects should return
     * their fixed size instead. Callers can decide whether to honor
     * counter offers.
     *
     * <p>This operation should be idempotent, so that applying it
     * once is identical to applying it twice.
     *
     * @param size The proposed size to render with.
     * @return The argument or a counter offer size to render with.
     */
    public Point negotiateSize(Point size) {
        Point minSize = getMinSize();
        Point maxSize = getMaxSize();

        if (minSize != null) {
            size = Point.max(size, minSize);
        }
        if (maxSize != null) {
            size = Point.min(size, maxSize);
        }

        return size;
    }
}
