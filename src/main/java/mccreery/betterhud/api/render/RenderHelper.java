package mccreery.betterhud.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Rectangle;
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

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getMaxY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getMaxY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.end();

        RenderSystem.disableTexture();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
    }

    /**
     * Draws a rectangular border with the specified color. Textures are temporarily disabled and enabled again before
     * returning.
     */
    public static void draw(MatrixStack matrixStack, Rectangle rectangle, Color color, int lineWidth) {
        // Left
        fill(matrixStack, new Rectangle(
                rectangle.getX(), rectangle.getY(),
                lineWidth, rectangle.getHeight()), color);
        // Top
        fill(matrixStack, new Rectangle(
                rectangle.getX(), rectangle.getY(),
                rectangle.getWidth(), lineWidth), color);
        // Right
        fill(matrixStack, new Rectangle(
                rectangle.getX() + rectangle.getWidth() - lineWidth, rectangle.getY(),
                lineWidth, rectangle.getHeight()), color);
        // Bottom
        fill(matrixStack, new Rectangle(
                rectangle.getX(), rectangle.getY() + rectangle.getHeight() - lineWidth,
                rectangle.getWidth(), lineWidth), color);
    }
}
