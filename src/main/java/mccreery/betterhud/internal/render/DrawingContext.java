package mccreery.betterhud.internal.render;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class DrawingContext {
    private static final Point[] COUNTERCLOCKWISE_WINDING = {
        Rectangle.Node.BOTTOM_LEFT.getT(),
        Rectangle.Node.BOTTOM_RIGHT.getT(),
        Rectangle.Node.TOP_RIGHT.getT(),
        Rectangle.Node.TOP_LEFT.getT()
    };

    private final MatrixStack matrixStack;
    private final BufferBuilder bufferBuilder;

    public DrawingContext(MatrixStack matrixStack, BufferBuilder bufferBuilder) {
        this.matrixStack = matrixStack;
        this.bufferBuilder = bufferBuilder;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public BufferBuilder getBufferBuilder() {
        return bufferBuilder;
    }

    public void drawFilledRectangle(Rectangle rectangle, Color color) {
        drawColoredRectangle(rectangle, color, GL11.GL_QUADS);
    }

    public void drawBorderRectangle(Rectangle rectangle, Color color) {
        drawColoredRectangle(rectangle, color, GL11.GL_LINE_LOOP);
    }

    private void drawColoredRectangle(Rectangle rectangle, Color color, int drawMode) {
        Matrix4f modelMatrix = matrixStack.peek().getModel();
        bufferBuilder.begin(drawMode, VertexFormats.POSITION_COLOR);

        for (Point t : COUNTERCLOCKWISE_WINDING) {
            Point vertex = rectangle.interpolate(t);

            bufferBuilder.vertex(modelMatrix, (float)vertex.getX(), (float)vertex.getY(), 0.0f)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .next();
        }

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }

    public void drawTexturedRectangle(Rectangle rectangle, Rectangle texture, Point textureSize) {
        drawTexturedRectangle(rectangle, new Rectangle(
                texture.getX() / textureSize.getX(), texture.getY() / textureSize.getY(),
                texture.getWidth() / textureSize.getX(), texture.getHeight() / textureSize.getY()));
    }

    public void drawTexturedRectangle(Rectangle rectangle, Rectangle normalizedTexture) {
        Matrix4f modelMatrix = matrixStack.peek().getModel();
        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        for (Point t : COUNTERCLOCKWISE_WINDING) {
            Point vertex = rectangle.interpolate(t);
            Point textureVertex = normalizedTexture.interpolate(t);

            bufferBuilder.vertex(modelMatrix, (float)vertex.getX(), (float)vertex.getY(), 0.0f)
                    .texture((float)textureVertex.getX(), (float)textureVertex.getY())
                    .next();
        }

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }

    public void drawLine(Point pointA, Point pointB, Color color) {
        Matrix4f modelMatrix = matrixStack.peek().getModel();
        bufferBuilder.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(modelMatrix, (float)pointA.getX(), (float)pointA.getY(), 0.0f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .next();

        bufferBuilder.vertex(modelMatrix, (float)pointB.getX(), (float)pointB.getY(), 0.0f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .next();

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}
