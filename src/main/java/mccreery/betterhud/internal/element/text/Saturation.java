package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MC;

import java.util.Arrays;
import java.util.List;

import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.util.MathUtil;
import net.minecraft.client.resources.I18n;

public class Saturation extends TextElement {
    public Saturation() {
        super("saturation");
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return MC.playerController.gameIsSurvivalOrAdventure();
    }

    @Override
    protected List<String> getText() {
        return Arrays.asList(I18n.format("betterHud.hud.saturation", MathUtil.formatToPlaces(MC.player.getFoodStats().getSaturationLevel(), 1)));
    }
}
