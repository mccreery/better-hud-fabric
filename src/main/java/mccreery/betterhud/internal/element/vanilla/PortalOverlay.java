package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.lang.annotation.ElementType;

public class PortalOverlay extends OverlayElement {
    public PortalOverlay() {
        super("portal");
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderPortal
            && !MC.player.isPotionActive(Effects.NAUSEA)
            && !OverlayHook.pre(context.getEvent(), ElementType.PORTAL)
            && getTimeInPortal(context.getPartialTicks()) > 0;
    }

    private float getTimeInPortal(float partialTicks) {
        return MathUtil.lerp(
            MC.player.prevTimeInPortal,
            MC.player.timeInPortal, partialTicks);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        float timeInPortal = getTimeInPortal(context.getPartialTicks());

        if(timeInPortal < 1) {
            timeInPortal *= timeInPortal;
            timeInPortal *= timeInPortal;

            timeInPortal = timeInPortal * 0.8f + 0.2f;
        }

        Color.WHITE.withAlpha(Math.round(timeInPortal * 255)).apply();
        MC.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

        TextureAtlasSprite texture = MC.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.NETHER_PORTAL.getDefaultState(), MC.world, BlockPos.ZERO);
        Rectangle screen = MANAGER.getScreen();
        AbstractGui.blit(0, 0, 0, screen.getWidth(), screen.getHeight(), texture);

        OverlayHook.post(context.getEvent(), ElementType.PORTAL);
        return null;
    }
}
