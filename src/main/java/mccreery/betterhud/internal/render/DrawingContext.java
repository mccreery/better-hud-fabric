package mccreery.betterhud.internal.render;

import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class DrawingContext {
    private static final Anchor[] COUNTERCLOCKWISE_WINDING = {
        Anchor.BOTTOM_LEFT,
        Anchor.BOTTOM_RIGHT,
        Anchor.TOP_RIGHT,
        Anchor.TOP_LEFT
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

        for (Anchor anchor : COUNTERCLOCKWISE_WINDING) {
            Point vertex = rectangle.getAnchorPoint(anchor);

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

        for (Anchor anchor : COUNTERCLOCKWISE_WINDING) {
            Point vertex = rectangle.getAnchorPoint(anchor);
            Point textureVertex = normalizedTexture.getAnchorPoint(anchor);

            bufferBuilder.vertex(modelMatrix, (float)vertex.getX(), (float)vertex.getY(), 0.0f)
                    .texture((float)textureVertex.getX(), (float)textureVertex.getY())
                    .next();
        }

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}
