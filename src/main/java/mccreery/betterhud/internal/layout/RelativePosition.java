package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class RelativePosition {
    public RelativePosition() {
    }

    public RelativePosition(HandlePosition handle, HandlePosition parentHandle, Point offset) {
        this.handle = handle;
        this.parentHandle = parentHandle;
        this.offset = offset;
    }

    private HandlePosition handle = HandlePosition.TOP_LEFT;

    public HandlePosition getHandle() {
        return handle;
    }

    public void setHandle(HandlePosition handle) {
        this.handle = handle;
    }

    private HandlePosition parentHandle = HandlePosition.TOP_LEFT;

    public HandlePosition getParentHandle() {
        return parentHandle;
    }

    public void setParentHandle(HandlePosition parentHandle) {
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
