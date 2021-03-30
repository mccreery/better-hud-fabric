package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class Offhand extends OverlayElement {
    private SettingPosition position;

    public Offhand() {
        super("offhand");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.BAR);
        position.setContentOptions(DirectionOptions.NONE);
        addSetting(position);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return !MC.player.getHeldItemOffhand().isEmpty()
            && !MC.playerController.isSpectatorMode();
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        ItemStack offhandStack = MC.player.getHeldItemOffhand();
        HandSide offhandSide = MC.player.getPrimaryHand().opposite();
        Direction offhand = offhandSide == HandSide.RIGHT ? Direction.EAST : Direction.WEST;

        Rectangle bounds = new Rectangle(22, 22);
        Rectangle texture = new Rectangle(24, 23, 22, 22);

        if(position.isDirection(Direction.SOUTH)) {
            bounds = bounds.align(OverlayElements.HOTBAR.getLastBounds().grow(SPACER).getAnchor(offhand), offhand.mirrorCol());
        } else {
            bounds = position.applyTo(bounds);
        }

        MC.getTextureManager().bindTexture(Textures.WIDGETS);
        GlUtil.drawRect(bounds, texture);

        GlUtil.renderHotbarItem(bounds.translate(3, 3), offhandStack, context.getPartialTicks());
        return bounds;
    }
}
