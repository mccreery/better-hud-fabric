package mccreery.betterhud.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface ScreenRenderContext {
    MinecraftClient getClient();
    MatrixStack getMatrixStack();
    float getTickDelta();
    TextRenderer getTextRenderer();
}
