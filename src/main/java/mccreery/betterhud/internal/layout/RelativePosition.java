package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class RelativePosition {
    private Anchor anchor = Anchor.TOP_LEFT;

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    private Anchor parentAnchor = Anchor.TOP_LEFT;

    public Anchor getParentAnchor() {
        return parentAnchor;
    }

    public void setParentAnchor(Anchor parentAnchor) {
        this.parentAnchor = parentAnchor;
    }

    private Point offset = Point.ZERO;

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public Rectangle apply(Rectangle parentBounds, Point size) {
        Rectangle atOrigin = new Rectangle(0, 0, size.getX(), size.getY());
        Point anchorPoint = Anchor.getAnchorPoint(atOrigin, anchor);
        Point parentAnchorPoint = Anchor.getAnchorPoint(parentBounds, parentAnchor);

        return new Rectangle(
                parentAnchorPoint.getX() + offset.getX() - anchorPoint.getX(),
                parentAnchorPoint.getY() + offset.getY() - anchorPoint.getY(),
                size.getX(),
                size.getY());
    }
}
