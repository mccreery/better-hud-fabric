package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.bars.StatBarAir;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

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
