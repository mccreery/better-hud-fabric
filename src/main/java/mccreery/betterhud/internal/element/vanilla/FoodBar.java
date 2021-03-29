package mccreery.betterhud.internal.element.vanilla;

import static jobicade.betterhud.BetterHud.MC;

import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.bars.StatBarFood;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class FoodBar extends Bar {
    private BooleanProperty hideMount;

    public FoodBar() {
        super("food", new StatBarFood());

        hideMount = new BooleanProperty("hideMount");
        addSetting(hideMount);
    }

    public boolean shouldRenderPrecheck() {
        return !(hideMount.get() && MC.player.isPassenger());
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return OverlayHook.shouldRenderBars()
            && ForgeIngameGui.renderFood
            && !OverlayHook.pre(context.getEvent(), ElementType.FOOD)
            && super.shouldRender(context);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle rect = super.render(context);
        OverlayHook.post(context.getEvent(), ElementType.FOOD);
        return rect;
    }
}
