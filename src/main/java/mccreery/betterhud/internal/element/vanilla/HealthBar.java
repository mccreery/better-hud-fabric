package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

public class HealthBar extends Bar {
    public HealthBar() {
        super("health", new StatBarHealth());
    }

    /** Used by {@link HealIndicator} */
    public Direction getIndicatorSide() {
        if(!position.isCustom() && DirectionOptions.CORNERS.isValid(position.getDirection())) {
            return getContentAlignment().mirrorCol();
        } else {
            return getContentAlignment();
        }
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return OverlayHook.shouldRenderBars()
            && ForgeIngameGui.renderHealth
            && !OverlayHook.pre(context.getEvent(), ElementType.HEALTH)
            && super.shouldRender(context);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle rect = super.render(context);
        OverlayHook.post(context.getEvent(), ElementType.HEALTH);
        return rect;
    }
}
