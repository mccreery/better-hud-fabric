package mccreery.betterhud.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class RenderHelper {
    private RenderHelper() {
    }

    /**
     * Fills a rectangle with the specified color. Textures are temporarily disabled and enabled again before returning.
     */
    public static void fill(MatrixStack matrixStack, Rectangle rectangle, Color color) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Matrix4f matrix = matrixStack.peek().getModel();

        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.fixedColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getMaxY(), 0.0f).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getMaxY(), 0.0f).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getY(), 0.0f).next();
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getY(), 0.0f).next();
        bufferBuilder.end();

        RenderSystem.disableTexture();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
    }

    public static Point getTextSize(TextRenderer textRenderer, String text) {
        return new Point(textRenderer.getWidth(text), textRenderer.fontHeight);
    }
}
