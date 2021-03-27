package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.render.Color;
import mccreery.betterhud.internal.render.DrawingContext;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Tessellator;

public class BlockViewer extends HudElement {
    private static final Color WHITE = new Color(255, 255, 255);

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle bounds = context.calculateBounds(new Point(100, 20));

        DrawingContext drawingContext = new DrawingContext(
                context.getMatrixStack(), Tessellator.getInstance().getBuffer());

        drawingContext.drawFilledRectangle(bounds, WHITE);

        return bounds;
    }
}
