package mccreery.betterhud.internal.element.vanilla;

import static jobicade.betterhud.BetterHud.MC;

import jobicade.betterhud.element.OverlayElement;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class BossHealth extends OverlayElement {
    public BossHealth() {
        super("bossHealth");
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderBossHealth
            && !OverlayHook.pre(context.getEvent(), ElementType.BOSSHEALTH);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        // Vanilla stores current boss bars in a private map so the size cannot
        // be determined and the bars cannot be moved
        MC.ingameGUI.getBossOverlay().render();

        OverlayHook.post(context.getEvent(), ElementType.BOSSHEALTH);
        return null;
    }
}
