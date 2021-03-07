package mccreery.betterhud.internal.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
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

        fill(matrices, dialogBounds.getX(), dialogBounds.getY(), dialogBounds.getMaxX(), dialogBounds.getMaxY(), 0x3f000000);

        Text leftText = new TranslatableText("hudLayout.prompt.left", new TranslatableText("key.mouse.left"));
        Text rightText = new TranslatableText("hudLayout.prompt.right", new TranslatableText("key.mouse.right"));

        Point anchorPoint = Anchor.getAnchorPoint(dialogBounds, Anchor.TOP_CENTER);

        drawCenteredText(matrices, textRenderer, leftText, anchorPoint.getX(), anchorPoint.getY() + 2, 0xffffffff);
        drawCenteredText(matrices, textRenderer, rightText, anchorPoint.getX(), anchorPoint.getY() + textRenderer.fontHeight + 4, 0xffffffff);
        RenderSystem.enableBlend();

        drawSelection(matrices, layout.getRoots().iterator().next());
    }

    private static final Identifier LAYOUT_WIDGETS = new Identifier(BetterHud.ID, "textures/layout_widgets.png");

    private void drawSelection(MatrixStack matrices, HudElementTree tree) {
        client.getTextureManager().bindTexture(LAYOUT_WIDGETS);

        // Draw "marching ants"
        Rectangle bounds = layout.getBoundsLastFrame().get(tree.getElement());
        drawDashedRectangle(matrices.peek().getModel(), bounds, 0x7f0071bc);

        // Draw selection handles
        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);
            drawTexture(matrices, anchorPoint.getX() - 2, anchorPoint.getY() - 2, 8, 1, 4, 4, 16, 16);
        }
    }

    private void drawDashedRectangle(Matrix4f matrix, Rectangle rectangle, int color) {
        // UV moves left with time = texture appears to move right (clockwise)
        int scale = (int)client.getWindow().getScaleFactor();
        int textureOffset = (int)(System.currentTimeMillis() % 2000) * 16 / 2000;

        GL11.glLineStipple(scale, rotateRight((short)0xf0f0, textureOffset));

        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        drawRectangle(matrix, rectangle, color, scale, -0.5f);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
    }

    private static short rotateRight(short x, int bits) {
        bits = bits & 0xf;
        return (short)((x & 0xffff) >>> bits | (x & 0xffff) << (16 - bits));
    }

    /**
     * Draws a rectangular border with the specified color. Textures are temporarily disabled and enabled again before
     * returning. Line width is restored to 1.0.
     */
    private static void drawRectangle(Matrix4f matrix, Rectangle rectangle, int color, float lineWidth, float offset) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        int a = color >>> 24;
        int r = color >>> 16 & 0xff;
        int g = color >>> 8 & 0xff;
        int b = color & 0xff;

        bufferBuilder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, rectangle.getX() - offset, rectangle.getMaxY() + offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX() + offset, rectangle.getMaxY() + offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX() + offset, rectangle.getY() - offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getX() - offset, rectangle.getY() - offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.end();

        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableTexture();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.lineWidth(1.0f);
    }
}
