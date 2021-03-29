package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.bars.StatBarArmor;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

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
