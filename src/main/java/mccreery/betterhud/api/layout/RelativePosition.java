package mccreery.betterhud.api.layout;

public class RelativePosition {
    private Anchor anchor;

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    private Anchor parentAnchor;

    public Anchor getParentAnchor() {
        return parentAnchor;
    }

    public void setParentAnchor(Anchor parentAnchor) {
        this.parentAnchor = parentAnchor;
    }

    private Point offset;

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
