package mccreery.betterhud.internal.element.text;

import java.util.List;

import jobicade.betterhud.BetterHud;
import jobicade.betterhud.element.OverlayElement;
import jobicade.betterhud.element.settings.DirectionOptions;
import jobicade.betterhud.element.settings.SettingColor;
import jobicade.betterhud.element.settings.SettingPosition;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.geom.Direction;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.render.Color;
import jobicade.betterhud.render.Grid;
import jobicade.betterhud.render.Label;
import jobicade.betterhud.util.GlUtil;

public abstract class TextElement extends OverlayElement {
    protected SettingPosition position;
    private SettingColor color;

    protected boolean border = false;

    public TextElement(String name) {
        super(name);

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.TOP_BOTTOM);
        position.setContentOptions(DirectionOptions.CORNERS);
        addSetting(position);

        color = new SettingColor("color");
        addSetting(color);
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
