package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

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
