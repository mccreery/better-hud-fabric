package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

/**
 * An object which can be rendered within a bounding box.
 */
public abstract class LayoutBox {
    private Rectangle bounds;

    /**
     * Set the current bounds to a given value.
     * @param bounds The new bounding box.
     * @see #getBounds()
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Gets the current bounds. The initial bounds should be a valid size, and
     * its top-left corner should be the origin.
     *
     * @return The current bounds.
     * @see #setBounds(Rectangle)
     */
    public Rectangle getBounds() {
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
        return size;
    }

    /**
     * Returns the preferred size of the object. By default, this is
     * the size negotiated from zero, which usually means the minimum
     * size. This method should return a value which will be accepted
     * under negotiation.
     *
     * @return The preferred size of the object.
     */
    public Point getPreferredSize() {
        return negotiateSize(Point.ZERO);
    }
}
