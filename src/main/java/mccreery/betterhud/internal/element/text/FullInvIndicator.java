package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.property.BooleanProperty;

import java.util.Arrays;
import java.util.List;

public class FullInvIndicator extends TextElement {
    private final BooleanProperty offHand;

    public FullInvIndicator() {
        offHand = new BooleanProperty("offhand", false);
        addProperty(offHand);
    }

    @Override
    protected List<String> getText() {
        return Arrays.asList(I18n.format("betterHud.hud.fullInv"));
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return MC.player.inventory.getFirstEmptyStack() == -1 &&
            (!offHand.get() || !MC.player.inventory.offHandInventory.get(0).isEmpty());
    }
}
