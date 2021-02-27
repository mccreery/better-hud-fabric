package mccreery.betterhud.api;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public final class HudRenderContext {
    HudRenderContext(Phase phase, MatrixStack matrixStack, float tickDelta, LivingEntity targetEntity) {
        this.phase = phase;
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
        this.targetEntity = targetEntity;
    }

    public enum Phase {
        /**
         * Elements accepting this phase render as an overlay, like the vanilla HUD.
         */
        OVERLAY,
        /**
         * Elements accepting this phase render above the heads of living entities, like vanilla player usernames.
         * Elements are rendered in 2D with a transform matrix applied so that they face the camera ("billboarding").
         */
        LIVING_ENTITY_BILLBOARD
    }

    private final Phase phase;

    public Phase getPhase() {
        return phase;
    }

    private final MatrixStack matrixStack;

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    private final float tickDelta;

    public float getTickDelta() {
        return tickDelta;
    }

    private final LivingEntity targetEntity;

    /**
     * @return The entity carrying the billboard for phase {@link Phase#LIVING_ENTITY_BILLBOARD}, otherwise the player.
     */
    public LivingEntity getTargetEntity() {
        return targetEntity;
    }
}
