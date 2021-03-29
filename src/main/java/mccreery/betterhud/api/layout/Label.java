package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.ScreenRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Label extends LayoutBox {
    private final String text;
    private final int color;
    private final boolean shadow;

    public Label(ScreenRenderContext context, String text, int color, boolean shadow) {
        super(context);
        this.text = text;
        this.color = color;
        this.shadow = shadow;
    }

    @Override
    public Point getPreferredSize() {
        TextRenderer textRenderer = getContext().getTextRenderer();
        return new Point(textRenderer.getWidth(text), textRenderer.fontHeight);
    }

    @Override
    public void render() {
        TextRenderer textRenderer = getContext().getTextRenderer();
        MatrixStack matrixStack = getContext().getMatrixStack();
        Rectangle bounds = getBounds();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();

        if (shadow) {
            textRenderer.drawWithShadow(matrixStack, text, x, y, color);
        } else {
            textRenderer.draw(matrixStack, text, x, y, color);
        }
    }
}
