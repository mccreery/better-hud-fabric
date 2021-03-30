package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.layout.Grid;
import mccreery.betterhud.api.layout.LabelOptions;
import mccreery.betterhud.api.property.AnchorProperty;
import mccreery.betterhud.api.property.ColorProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.text.Text;

import java.util.List;

public abstract class TextElement extends HudElement {
    private final ColorProperty color;
    private final AnchorProperty alignment;

    public TextElement() {
        color = new ColorProperty("color", Color.WHITE);
        addProperty(color);
        alignment = new AnchorProperty("alignment", Anchor.CENTER_LEFT);
        addProperty(alignment);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        List<Text> lines = getText(context);
        if (lines.isEmpty()) {
            return null;
        }

        Grid grid = Grid.ofLabels(context, alignment.get(), getLabelOptions(), lines);

        Rectangle bounds = context.calculateBounds(grid.getPreferredSize());
        grid.applyLayout(bounds);
        grid.render();

        return bounds;
    }

    protected LabelOptions getLabelOptions() {
        return LabelOptions.DEFAULT.withColor(color.get());
    }

    /**
     * Returns the lines of text to render in the element.
     * @param context The current render context.
     * @return The lines of text.
     */
    protected abstract List<Text> getText(HudRenderContext context);
}
