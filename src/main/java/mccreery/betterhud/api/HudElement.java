package mccreery.betterhud.api;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.BetterHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public abstract class HudElement {
    /**
     * Registry for HUD elements by class. Should not be called before the {@code betterhud} entrypoint.
     * @throws IllegalStateException If Better HUD is not yet initialized
     */
    @NotNull
    public static Registry<Class<? extends HudElement>> getRegistry() {
        return BetterHud.getInstance().getElementRegistry();
    }

    public MutableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    private transient String translationKey;

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("hudElement", getRegistry().getId(getClass()));
        }

        return translationKey;
    }

    private transient boolean fixed;

    /**
     * Indicates that the element has a fixed position and cannot be parented or positioned relatively.
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Indicates that the element has a fixed position and cannot be parented or positioned relatively.
     */
    protected void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    private HudRenderContext.Phase renderPhase = HudRenderContext.Phase.OVERLAY;

    public HudRenderContext.Phase getRenderPhase() {
        return renderPhase;
    }

    /**
     * {@link #render(HudRenderContext)} will only be called if the context's phase matches.
     */
    protected void setRenderPhase(HudRenderContext.Phase renderPhase) {
        this.renderPhase = renderPhase;
    }

    /**
     * Renders the element if conditions defined by the implementation are met. If conditions are not met but
     * {@link HudRenderContext#isLayoutMode() context.isLayoutMode()} returns {@code true} the element must render
     * a placeholder.
     *
     * <p>Elements which do not have fixed position should render within the bounds returned by
     * {@link HudRenderContext#calculateBounds(Point) context.calculateBounds(size)}.
     *
     * @return The bounds within which the element rendered or {@code null} if the element did not render.
     */
    public abstract Rectangle render(HudRenderContext context);
}
