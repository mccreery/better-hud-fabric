package mccreery.betterhud.mixin;

import net.minecraft.client.util.math.MatrixStack;

/**
 * Mixin interface for {@link net.minecraft.client.gui.hud.InGameHud}.
 */
public interface HudRenderer {
    /**
     * Replacement for {@link net.minecraft.client.gui.hud.InGameHud#render(MatrixStack, float)}.
     */
    void renderOverlay(MatrixStack matrices, float tickDelta);
}
