package mccreery.betterhud.internal.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.render.Color;
import mccreery.betterhud.api.render.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

        GL11.glLineWidth(client.options.guiScale);
        int time = (int)(System.currentTimeMillis() * 16 / 1000);

        for (Map.Entry<HudElement, Rectangle> bounds : layout.getBoundsLastFrame().entrySet()) {
            RenderHelper.drawDashed(matrices, bounds.getValue(), new Color(255, 0, 0), time, client.options.guiScale / 2);
        }
        GL11.glLineWidth(1);
    }
}
