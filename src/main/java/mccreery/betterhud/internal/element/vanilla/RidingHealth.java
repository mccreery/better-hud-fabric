package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

public class RidingHealth extends Bar {
    public RidingHealth() {
        super("mountHealth", new StatBarMount());
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return OverlayHook.shouldRenderBars()
            && ForgeIngameGui.renderHealthMount
            && !OverlayHook.pre(context.getEvent(), ElementType.HEALTHMOUNT)
            && super.shouldRender(context);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle rect = super.render(context);
        OverlayHook.post(context.getEvent(), ElementType.HEALTHMOUNT);
        return rect;
    }
}
