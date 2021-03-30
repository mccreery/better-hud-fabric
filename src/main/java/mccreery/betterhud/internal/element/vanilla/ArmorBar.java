package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

public class ArmorBar extends Bar {
    public ArmorBar() {
        super("armor", new StatBarArmor());
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return OverlayHook.shouldRenderBars()
            && ForgeIngameGui.renderArmor
            && !OverlayHook.pre(context.getEvent(), ElementType.ARMOR)
            && super.shouldRender(context);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle rect = super.render(context);
        OverlayHook.post(context.getEvent(), ElementType.ARMOR);
        return rect;
    }
}
