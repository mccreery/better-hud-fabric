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
    public static void draw(MatrixStack matrixStack, Rectangle rectangle, Color color) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Matrix4f matrix = matrixStack.peek().getModel();

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        bufferBuilder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getMaxY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getMaxY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX(), rectangle.getY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getX(), rectangle.getY(), 0.0f).color(r, g, b, a).next();
        bufferBuilder.end();

        RenderSystem.disableTexture();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
    }

    public static void drawDashed(MatrixStack matrixStack, Rectangle rectangle, Color color, int time, int scale) {
        GL11.glLineStipple(scale, getDashPattern(time));

        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        draw(matrixStack, rectangle, color);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
    }

    private static short getDashPattern(int time) {
        return rotate((short)0xff00, time);
    }

    private static short rotate(short x, int bits) {
        bits = bits & 0xf;
        return (short)((x & 0xffff) >>> bits | (x & 0xffff) << (16 - bits));
    }
}
