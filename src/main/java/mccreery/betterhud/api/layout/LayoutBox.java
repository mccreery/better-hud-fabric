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
     * Sets bounding boxes for this box (the parent) and its children. If the size of the bounding box is not the size
     * returned by {@link #getPreferredSize()}, it must be accepted by {@link #negotiateSize(Point)}.
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

    /**
     * Returns the preferred size of the box. It is guaranteed to be accepted by {@link #negotiateSize(Point)}.
     * @return The preferred size of the box.
     */
    public abstract Point getPreferredSize();

    /**
     * Accepts or rejects a size for this box. If the size is rejected, the response is a different, acceptable size.
     *
     * @param size The proposed size.
     * @return An acceptable size. Equal to the proposed size if it is acceptable.
     */
    public Point negotiateSize(Point size) {
        return getPreferredSize();
    }

    /**
     * Renders the content inside the bounding box. Must not be called before {@link #applyLayout(Rectangle)}.
     */
    public abstract void render();
}
