package mccreery.betterhud.api;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public interface HudRenderContext {
    Phase getPhase();

    enum Phase {
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

    MatrixStack getMatrixStack();

    float getTickDelta();

    /**
     * @return The entity carrying the billboard for phase {@link mccreery.betterhud.internal.HudRenderContext.Phase#LIVING_ENTITY_BILLBOARD}, otherwise the player.
     */
    LivingEntity getTargetEntity();

    Rectangle calculateBounds(Point size);
}
