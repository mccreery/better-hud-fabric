package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class RelativePosition {
    public RelativePosition() {
    }

    public RelativePosition(Rectangle.Node handle, Rectangle.Node parentHandle, Point offset) {
        this.handle = handle;
        this.parentHandle = parentHandle;
        this.offset = offset;
    }

    private Rectangle.Node handle = Rectangle.Node.TOP_LEFT;

    public Rectangle.Node getHandle() {
        return handle;
    }

    public void setHandle(Rectangle.Node handle) {
        this.handle = handle;
    }

    private Rectangle.Node parentHandle = Rectangle.Node.TOP_LEFT;

    public Rectangle.Node getParentHandle() {
        return parentHandle;
    }

    public void setParentHandle(Rectangle.Node parentHandle) {
        this.parentHandle = parentHandle;
    }

    private Point offset = Point.ZERO;

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public Rectangle apply(Rectangle parentBounds, Point size) {
        Point anchor = parentBounds.interpolate(parentHandle.getT());
        return new Rectangle(Point.ZERO, size).align(anchor.add(offset), handle.getT());
    }
}
