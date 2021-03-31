package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;

public class Saturation extends TextElement {
    @Override
    protected List<Text> getText(HudRenderContext context) {
        if (context.getClient().interactionManager.getCurrentGameMode().isSurvivalLike()) {
            double saturation = context.getClient().player.getHungerManager().getSaturationLevel();
            return Collections.singletonList(new TranslatableText("betterhud.saturation", saturation));
        } else {
            return Collections.emptyList();
        }
    }
}
