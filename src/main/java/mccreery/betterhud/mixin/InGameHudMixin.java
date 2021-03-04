package mccreery.betterhud.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.BetterHud;
import mccreery.betterhud.internal.HudRenderContext;
import mccreery.betterhud.internal.HudRenderer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.w3c.dom.css.Rect;

/**
 * Injections are used on various methods to eliminate side effects in the OpenGL state machine and maintain a sensible
 * OpenGL state. The following state variables are defined as the "normal" state:
 *
 * <pre><code>glEnable(GL_BLEND)
 * glEnable(GL_DEPTH_TEST)
 * glEnable(GL_TEXTURE_2D)
 * glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)
 * glBlendColor(0.0f, 0.0f, 0.0f, 0.0f)
 * glDepthFunc(GL_LEQUAL)
 * glDepthMask(GL_TRUE)
 * glColor4f(1.0f, 1.0f, 1.0f, 1.0f)</code></pre>
 *
 * <p>Texture bindings are deliberately not included. Code using textures must bind its own textures while it has
 * control.
 *
 * @see #applyNormalGlState()
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper implements HudRenderer {
    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderVignetteOverlay(Entity entity);

    @Shadow
    protected abstract void renderPumpkinOverlay();

    @Shadow
    protected abstract void renderPortalOverlay(float nauseaStrength);

    @Override
    public void renderOverlay(MatrixStack matrices, float tickDelta) {
        applyNormalGlState();

        scaledWidth = client.getWindow().getScaledWidth();
        scaledHeight = client.getWindow().getScaledHeight();
        Rectangle screenBounds = new Rectangle(0, 0, scaledWidth, scaledHeight);

        // Fullscreen overlays, cannot be laid out
        tryRenderVignetteOverlay();
        tryRenderPumpkinOverlay();
        tryRenderPortalOverlay(tickDelta);

        HudRenderContext context = new HudRenderContext();
        context.setPhase(mccreery.betterhud.api.HudRenderContext.Phase.OVERLAY);
        context.setMatrixStack(matrices);
        context.setTickDelta(tickDelta);
        context.setTargetEntity(client.player);
        context.setParentBounds(screenBounds);
        // position is populated by individual element tree nodes

        BetterHud.getInstance().getLayout().render(context);
    }

    private void tryRenderVignetteOverlay() {
        if (MinecraftClient.isFancyGraphicsOrBetter()) {
            renderVignetteOverlay(client.getCameraEntity());
        }
    }

    private void tryRenderPumpkinOverlay() {
        ItemStack itemStack = client.player.inventory.getArmorStack(3);

        if (this.client.options.getPerspective().isFirstPerson() && itemStack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            renderPumpkinOverlay();
        }
    }

    private void tryRenderPortalOverlay(float tickDelta) {
        float f = MathHelper.lerp(tickDelta, client.player.lastNauseaStrength, client.player.nextNauseaStrength);

        if (f > 0.0F && !client.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            renderPortalOverlay(f);
        }
    }

    // Mojang's deprecated methods are not really deprecated
    @SuppressWarnings("deprecation")
    private static void applyNormalGlState() {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendColor(0.0f, 0.0f, 0.0f, 0.0f);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
