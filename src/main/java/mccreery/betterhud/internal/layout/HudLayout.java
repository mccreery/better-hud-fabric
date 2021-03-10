package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.HudRenderContext;

import java.util.HashSet;
import java.util.Set;

public class HudLayout {
    private final Set<HudElementTree> roots = new HashSet<>();

    public Set<HudElementTree> getRoots() {
        return roots;
    }

    public void render(HudRenderContext context) {
        for (HudElementTree root : roots) {
            // Defensive copy, renderTree modifies element-specific properties of HudRenderContext
            renderTree(root, new HudRenderContext(context));
        }
    }

    private void renderTree(HudElementTree tree, HudRenderContext context) {
        context.setPosition(tree.getPosition());

        Rectangle bounds = tree.getElement().render(context);
        tree.setBoundsLastFrame(bounds);

        // Ignore non rendering elements, while still parenting as close as possible up the tree
        if (bounds != null) {
            context.setParentBounds(bounds);
        }

        for (HudElementTree child : tree.getChildren()) {
            renderTree(child, context);
        }
    }
}
