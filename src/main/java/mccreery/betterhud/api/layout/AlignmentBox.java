package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

public class AlignmentBox extends LayoutBox {
    private final LayoutBox content;
    private final Point alignment;

    public AlignmentBox(LayoutBox content, Point t) {
        // Layout is dynamic
        super(Point.ZERO);
        this.content = content;
        this.alignment = t;
    }

    public AlignmentBox(LayoutBox content, Rectangle.Node node) {
        this(content, node.getT());
    }

    @Override
    protected Point getMinSize() {
        return content.getMinSize();
    }

    @Override
    public Point getDefaultSize() {
        return content.getDefaultSize();
    }

    @Override
    protected Point getMaxSize() {
        return null;
    }

    @Override
    public void render() {
        content.setBounds(new Rectangle(Point.ZERO, content.getDefaultSize()).align(getBounds(), alignment));
        content.render();
    }
}
