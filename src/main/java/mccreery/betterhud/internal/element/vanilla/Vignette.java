package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.border.WorldBorder;

import java.lang.annotation.ElementType;

public class Vignette extends HudElement {
    private static final Identifier VIGNETTE_TEX_PATH = new Identifier("textures/misc/vignette.png");

    private final BooleanProperty warnings;
    private float brightness = 1;

    public Vignette() {
        warnings = new BooleanProperty("warnings", true);
        addProperty(warnings);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderVignette
            && Minecraft.isFancyGraphicsEnabled()
            && !OverlayHook.pre(context.getEvent(), ElementType.VIGNETTE);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        WorldBorder border = MC.world.getWorldBorder();

        float f = 0;
        if (warnings.get()) {
            float distance = (float)border.getClosestDistance(MC.player);
            float warningDistance = (float)getWarningDistance(border);

            if(distance < warningDistance) {
                f = 1 - distance / warningDistance;
            }
        }

        // Animate brightness
        brightness = brightness + (MathHelper.clamp(1 - MC.player.getBrightness(), 0, 1) - brightness) / 100;

        Color color;
        if(f > 0) {
            int shade = Math.round(f * 255.0f);
            color = new Color(0, shade, shade);
        } else {
            int value = Math.round(brightness * 255.0f);
            color = new Color(value, value, value);
        }

        GlUtil.blendFuncSafe(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
        MC.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);

        GlUtil.drawRect(MANAGER.getScreen(), new Rectangle(256, 256), color);

        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        GlUtil.blendFuncSafe(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ZERO, DestFactor.ONE);

        OverlayHook.post(context.getEvent(), ElementType.VIGNETTE);
        return null;
    }

    /**
     * @return The distance from the world border at which a player will start
     * to see a warning.
     */
    private double getWarningDistance(WorldBorder worldBorder) {
        // The distance the border will move within the warning time
        double warningTimeDistance = worldBorder.getResizeSpeed() // meters/millis
            * worldBorder.getWarningTime() * 1000; // millis

        // Border cannot move further than the target size
        double remainingResize = Math.abs(worldBorder.getTargetSize() - worldBorder.getDiameter());
        warningTimeDistance = Math.min(warningTimeDistance, remainingResize);

        // Warn by distance and time
        // The larger distance triggers a warning first
        return Math.max(
            worldBorder.getWarningDistance(),
            warningTimeDistance
        );
    }
}
