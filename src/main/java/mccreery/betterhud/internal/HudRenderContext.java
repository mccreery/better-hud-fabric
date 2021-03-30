package mccreery.betterhud.internal;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.layout.RelativePosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public final class HudRenderContext implements mccreery.betterhud.api.HudRenderContext {
    public HudRenderContext() {
    }

    public HudRenderContext(HudRenderContext context) {
        this.client = context.client;
        this.phase = context.phase;
        this.matrixStack = context.matrixStack;
        this.tickDelta = context.tickDelta;
        this.textRenderer = context.textRenderer;
        this.targetEntity = context.targetEntity;
        this.parentBounds = context.parentBounds;
        this.position = context.position;
        this.layoutMode = context.layoutMode;
    }

    private MinecraftClient client;

    @Override
    public MinecraftClient getClient() {
        return client;
    }

    public void setClient(MinecraftClient client) {
        this.client = client;
    }

    private Phase phase;

    @Override
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    private MatrixStack matrixStack;

    @Override
    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    private float tickDelta;

    @Override
    public float getTickDelta() {
        return tickDelta;
    }

    public void setTickDelta(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    private TextRenderer textRenderer;

    @Override
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    private LivingEntity targetEntity;

    /**
     * @return The entity carrying the billboard for phase {@link Phase#LIVING_ENTITY_BILLBOARD}, otherwise the player.
     */
    @Override
    public LivingEntity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(LivingEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    private Rectangle parentBounds;

    public void setParentBounds(Rectangle parentBounds) {
        this.parentBounds = parentBounds;
    }

    private RelativePosition position;

    public void setPosition(RelativePosition position) {
        this.position = position;
    }

    /**
     * Calculates bounds for the currently rendering element based on the current HUD layout.
     *
     * @param size The size of the element
     * @return A bounds rectangle with the same size
     */
    @Override
    public Rectangle calculateBounds(Point size) {
        return position.apply(parentBounds, size);
    }

    private boolean layoutMode;

    @Override
    public boolean isLayoutMode() {
        return layoutMode;
    }

    public void setLayoutMode(boolean layoutMode) {
        this.layoutMode = layoutMode;
    }
}
