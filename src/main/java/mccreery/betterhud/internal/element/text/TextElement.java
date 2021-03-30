package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.ColorProperty;
import mccreery.betterhud.internal.BetterHud;
import mccreery.betterhud.internal.render.Color;

import java.util.List;

public abstract class TextElement extends HudElement {
    private final ColorProperty color;

    protected boolean border = false;

    public TextElement() {
        color = new ColorProperty("color", Color.WHITE);
        addProperty(color);
    }

    public Color getColor() {
        return color.get();
    }

    protected Rectangle getPadding() {
        return border ? Rectangle.createPadding(BetterHud.SPACER) : Rectangle.empty();
    }

    protected Rectangle getMargin() {
        return Rectangle.empty();
    }

    protected Rectangle moveRect(Rectangle bounds) {
        return position.applyTo(bounds);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        List<String> text = getText();
        return text == null || text.isEmpty() ? null : render(context, text);
    }

    protected Rectangle render(HudRenderContext event, List<String> text) {
        Grid<Label> grid = new Grid<Label>(new Point(1, text.size()))
            .setGutter(new Point(2, 2));

        Direction contentAlignment = position.getContentAlignment();
        if(contentAlignment != null) grid.setCellAlignment(contentAlignment);

        for(int i = 0; i < text.size(); i++) {
            grid.setCell(new Point(0, i), new Label(text.get(i)).setColor(color.get()));
        }

        Rectangle padding = getPadding();
        Rectangle margin = getMargin();

        Rectangle bounds = moveRect(new Rectangle(grid.getPreferredSize().add(padding.getSize()).add(margin.getSize())));

        drawBorder(bounds, padding, margin);
        grid.setBounds(bounds.grow(margin.grow(padding).invert())).render();
        drawExtras(bounds);

        return bounds;
    }

    protected void drawBorder(Rectangle bounds, Rectangle padding, Rectangle margin) {
        if(border) GlUtil.drawRect(bounds.grow(margin.invert()), Color.TRANSLUCENT);
    }

    protected abstract List<String> getText();
    protected void drawExtras(Rectangle bounds) {}
}
