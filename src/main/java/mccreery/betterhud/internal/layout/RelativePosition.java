package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class RelativePosition {
    public RelativePosition() {
    }

    public RelativePosition(Anchor anchor, Anchor parentAnchor, Point offset) {
        this.anchor = anchor;
        this.parentAnchor = parentAnchor;
        this.offset = offset;
    }

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
        Point anchorPoint = Anchor.getAnchorPoint(size, anchor);
        Point parentAnchorPoint = parentBounds.getAnchorPoint(parentAnchor);

        return new Rectangle(parentAnchorPoint.add(offset).subtract(anchorPoint), size);
    }
}
