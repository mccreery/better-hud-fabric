package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.client.resource.language.I18n;

import java.util.Arrays;
import java.util.List;

public class Saturation extends TextElement {
    @Override
    public boolean shouldRender(HudRenderContext context) {
        return MC.playerController.gameIsSurvivalOrAdventure();
    }

    @Override
    protected List<String> getText() {
        return Arrays.asList(I18n.format("betterHud.hud.saturation", MathUtil.formatToPlaces(MC.player.getFoodStats().getSaturationLevel(), 1)));
    }
}
