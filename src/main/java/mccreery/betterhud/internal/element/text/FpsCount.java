package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.internal.FpsProvider;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class FpsCount extends TextElement {
    @Override
    protected List<Text> getText(HudRenderContext context) {
        int fps = ((FpsProvider)context.getClient()).getCurrentFps();
        return Collections.singletonList(Text.of(String.valueOf(fps)));
    }
}
