package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.ScreenRenderContext;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class AlignmentBox extends LayoutBox {
    private final LayoutBox content;
    private final Point alignment;

    public AlignmentBox(ScreenRenderContext context, LayoutBox content, Point t) {
        super(context);
        this.content = content;
        this.alignment = t;
    }

    public AlignmentBox(ScreenRenderContext context, LayoutBox content, Anchor anchor) {
        this(context, content, anchor.getT());
    }

    @Override
    public Point getPreferredSize() {
        return content.getPreferredSize();
    }

    @Override
    public Point negotiateSize(Point size) {
        return Point.max(getPreferredSize(), size);
    }

    @Override
    public void applyLayout(Rectangle bounds) {
        super.applyLayout(bounds);
        content.applyLayout(new Rectangle(Point.ZERO, content.getPreferredSize()).align(getBounds(), alignment));
    }

    @Override
    public void render() {
        content.render();
    }
}
