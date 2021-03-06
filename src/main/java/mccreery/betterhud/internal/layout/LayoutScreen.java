package mccreery.betterhud.internal.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.render.Color;
import mccreery.betterhud.api.render.RenderHelper;
import mccreery.betterhud.internal.BetterHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class LayoutScreen extends Screen {
    private final HudLayout layout;

    public LayoutScreen(HudLayout layout) {
        super(new TranslatableText("options.hudLayout"));
        this.layout = layout;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();

        Point screenSize = new Point(width, height);
        Point screenCenter = Anchor.getAnchorPoint(screenSize, Anchor.CENTER);
        Rectangle dialogBounds = Anchor.getAlignedRectangle(screenCenter, Anchor.CENTER, new Point(200, textRenderer.fontHeight * 2 + 6));

        RenderHelper.fill(matrices, dialogBounds, new Color(0, 0, 0, 63));

        Text leftText = new TranslatableText("hudLayout.prompt.left", new TranslatableText("key.mouse.left"));
        Text rightText = new TranslatableText("hudLayout.prompt.right", new TranslatableText("key.mouse.right"));

        Point anchorPoint = Anchor.getAnchorPoint(dialogBounds, Anchor.TOP_CENTER);

        int color = new Color(255, 255, 255, 255).toPackedArgb();
        drawCenteredText(matrices, textRenderer, leftText, anchorPoint.getX(), anchorPoint.getY() + 2, color);
        drawCenteredText(matrices, textRenderer, rightText, anchorPoint.getX(), anchorPoint.getY() + textRenderer.fontHeight + 4, color);
        RenderSystem.enableBlend();

        drawSelection(matrices, layout.getRoots().iterator().next());
    }

    private static final Identifier LAYOUT_WIDGETS = new Identifier(BetterHud.ID, "textures/layout_widgets.png");

    private void drawSelection(MatrixStack matrices, HudElementTree tree) {
        client.getTextureManager().bindTexture(LAYOUT_WIDGETS);

        // Draw "marching ants"
        Rectangle bounds = layout.getBoundsLastFrame().get(tree.getElement());
        // UV moves left with time = texture appears to move right (clockwise)
        float textureOffset = (System.currentTimeMillis() % 2000) / -2000.0f;
        drawDashedRectangle(matrices, bounds, textureOffset);

        // Move bottom right edges in 1 pixel to fix handle placement
        //Rectangle smallBounds = new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth() - 1, bounds.getHeight() - 1);

        // Draw selection handles
        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);
            drawTexture(matrices, anchorPoint.getX() - 2, anchorPoint.getY() - 2, 8, 1, 4, 4, 16, 16);
        }
    }

    /**
     * Draws a pixel perfect outline using the 16x16 dash texture.
     */
    private void drawDashedRectangle(MatrixStack matrices, Rectangle rectangle, float textureOffset) {
        // Short aliases for vertex coordinates
        int minX = rectangle.getX();
        int minY = rectangle.getY();
        int maxX = rectangle.getMaxX();
        int maxY = rectangle.getMaxY();

        // Calculate texture offset at each corner (clockwise starting top left)
        float[] textureCoords = new float[5];
        textureCoords[0] = textureOffset;
        textureCoords[1] = textureCoords[0] + (rectangle.getWidth() - 1) / 16.0f;
        textureCoords[2] = textureCoords[1] + (rectangle.getHeight() - 1) / 16.0f;
        textureCoords[3] = textureCoords[2] + (rectangle.getWidth() - 1) / 16.0f;
        textureCoords[4] = textureCoords[3] + (rectangle.getHeight() - 1) / 16.0f;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        Matrix4f matrix = matrices.peek().getModel();

        // 1 pixel in our texture
        float step = 1.0f / 16.0f;

        // Create quads for each line
        // Lines have one pixel missing on their clockwise-later side (filled in by next line)

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
        // Normal texture direction
        addQuad(buffer, matrix, minX, minY, maxX - 1, minY + 1, textureCoords[0], 0, textureCoords[1], step);
        addQuad(buffer, matrix, maxX - 1, minY, maxX, maxY - 1, 0, textureCoords[1], step, textureCoords[2]);
        // Flipped texture direction
        addQuad(buffer, matrix, minX + 1, maxY - 1, maxX, maxY, textureCoords[3], 0, textureCoords[2], step);
        addQuad(buffer, matrix, minX, minY + 1, minX + 1, maxY, 0, textureCoords[4], step, textureCoords[3]);
        buffer.end();

        BufferRenderer.draw(buffer);
    }

    private static void addQuad(BufferBuilder buffer, Matrix4f matrix, int minX, int minY, int maxX, int maxY,
            float minU, float minV, float maxU, float maxV) {
        buffer.vertex(matrix, minX, maxY, 0).texture(minU, maxV).next();
        buffer.vertex(matrix, maxX, maxY, 0).texture(maxU, maxV).next();
        buffer.vertex(matrix, maxX, minY, 0).texture(maxU, minV).next();
        buffer.vertex(matrix, minX, minY, 0).texture(minU, minV).next();
    }
}
