package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.internal.FpsProvider;

import java.util.Collections;
import java.util.List;

public class FpsCount extends TextElement {
    @Override
    protected List<String> getText(HudRenderContext context) {
        int fps = ((FpsProvider)context.getClient()).getCurrentFps();
        return Collections.singletonList(String.valueOf(fps));
    }
}
