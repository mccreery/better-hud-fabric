package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.render.Color;
import mccreery.betterhud.api.render.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class LayoutScreen extends Screen {
    protected LayoutScreen() {
        super(new TranslatableText("betterhud.layout"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Point screenSize = new Point(width, height);
        Point screenCenter = Anchor.getAnchorPoint(screenSize, Anchor.CENTER);
        Rectangle dialogBounds = Anchor.getAlignedRectangle(screenCenter, Anchor.CENTER, new Point(50, 12));

        RenderHelper.fill(matrices, dialogBounds, new Color(255, 255, 255, 63));

        // TODO localization
        String text = "Right click to add";
        Rectangle textBounds = Anchor.getAlignedRectangle(
                Anchor.getAnchorPoint(dialogBounds, Anchor.CENTER),
                Anchor.CENTER, RenderHelper.getTextSize(textRenderer, text));

        textRenderer.draw(matrices, text, textBounds.getX(), textBounds.getY(),
                new Color(255, 255, 255, 255).toPackedArgb());
    }
}
