package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

/**
 * A box with renderable content that can be moved and resized. It may also be responsible for moving, resizing and
 * rendering a number of children.
 */
public abstract class LayoutBox {
    private Rectangle bounds;

    /**
     * Returns the bounding box set by {@link #applyLayout(Rectangle)}. Its initial value is undefined.
     * @return The bounding box.
     */
    public final Rectangle getBounds() {
        return bounds;
    }

    /**
     * Called by {@link #applyLayout(Rectangle)} to set the bounding box.
     * @param bounds The new bounding box.
     */
    private void setBounds(Rectangle bounds) {
        Point size = bounds.getSize();
        if (!negotiateSize(size).equals(size)) {
            throw new IllegalArgumentException("Invalid size");
        }

        this.bounds = bounds;
    }

    /**
     * Sets bounding boxes for this box (the parent) and its children.
     *
     * <p>Implementations must call {@code super.applyLayout(bounds)} to set the parent bounds. The layout cascades to
     * the children by calling their own {@code applyLayout} methods. Child bounding boxes must not overflow the parent
     * bounding box.
     *
     * @param bounds The bounding box to set for the parent (this).
     */
    public void applyLayout(Rectangle bounds) {
        setBounds(bounds);
    }

    public abstract Point getPreferredSize();

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
        return getPreferredSize();
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
}
