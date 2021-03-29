package mccreery.betterhud.api;

import net.minecraft.client.util.math.MatrixStack;

public interface ScreenRenderContext {
    MatrixStack getMatrixStack();
    float getTickDelta();
}
