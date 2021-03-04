package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;

import java.util.HashSet;
import java.util.Set;

public class HudLayout {
    private final Set<HudElement> rootElements = new HashSet<>();

    public Set<HudElement> getRootElements() {
        return rootElements;
    }

    public void render(HudRenderContext context) {
        for (HudElement element : rootElements) {
            renderTree(element, context);
        }
    }

    private void renderTree(HudElement element, HudRenderContext context) {
        element.render(context);

        for (HudElement child : element.getChildren()) {
            renderTree(child, context);
        }
    }
}
