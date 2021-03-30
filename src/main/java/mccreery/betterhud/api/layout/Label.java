package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.ScreenRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Label extends LayoutBox {
    private final Text text;
    private LabelOptions options = LabelOptions.DEFAULT;

    /**
     * Creates a label.
     * @param context The current render context.
     * @param text The single line of text.
     * @see Text#of(String) 
     */
    public Label(ScreenRenderContext context, Text text) {
        super(context);
        this.text = text;
    }

    public LabelOptions getOptions() {
        return options;
    }

    public void setOptions(LabelOptions options) {
        this.options = options;
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
        VertexConsumerProvider provider = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        textRenderer.draw(text, x, y, options.getColor().toPackedArgb(), options.getShadow(),
                matrixStack.peek().getModel(), provider, options.getSeeThrough(),
                options.getBackgroundColor().toPackedArgb(), options.getLight());
    }
}
