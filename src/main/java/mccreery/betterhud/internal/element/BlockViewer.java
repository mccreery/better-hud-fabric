package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.gui.DrawableHelper;

public class BlockViewer extends HudElement {
    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle bounds = context.calculateBounds(new Point(100, 20));

        DrawableHelper.fill(context.getMatrixStack(),
                bounds.getX(), bounds.getY(),
                bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(),
                0xffffffff);

        return bounds;
    }
}
