package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MC;

import java.util.Arrays;
import java.util.List;

import jobicade.betterhud.element.settings.Legend;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.property.BooleanProperty;
import net.minecraft.client.resources.I18n;

public class FullInvIndicator extends TextElement {
    private BooleanProperty offHand;

    public FullInvIndicator() {
        super("fullInvIndicator");

        addSetting(new Legend("misc"));
        offHand = new BooleanProperty("offhand");
        addSetting(offHand);
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
