package mccreery.betterhud.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GameRenderer.class)
public abstract class RenderHudMixin {
    /**
     * Redirected from {@link InGameHud#render(MatrixStack, float)}. Injections on the original render method will
     * succeed, but will have no effect at runtime since the method is no longer being called. Invokes the
     * HudRenderCallback event from Fabric API to render extra elements that don't know about Better HUD.
     * TODO config option to disable Fabric event callback in case of incompatibilities
     */
    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public void onRender(InGameHud thiz, MatrixStack matrices, float tickDelta) {
        thiz.getFontRenderer().drawWithShadow(matrices, "Better HUD is running!", 5, 5, -1);
    }
}
