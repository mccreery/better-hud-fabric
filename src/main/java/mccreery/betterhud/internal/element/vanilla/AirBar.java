package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

public class AirBar extends Bar {
    public AirBar() {
        super("airBar", new StatBarAir());
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return OverlayHook.shouldRenderBars()
            && ForgeIngameGui.renderAir
            && !OverlayHook.pre(context.getEvent(), ElementType.AIR)
            && super.shouldRender(context);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle rect = super.render(context);
        OverlayHook.post(context.getEvent(), ElementType.AIR);
        return rect;
    }
}
