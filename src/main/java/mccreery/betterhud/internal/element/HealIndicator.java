package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.world.GameRules;

public class HealIndicator extends HudElement {
    @Override
    public Rectangle render(HudRenderContext context) {
            String healIndicator = I18n.format("betterHud.hud.healIndicator");
            Rectangle bounds = mode.getIndex() == 0 ? new Rectangle(MC.fontRenderer.getStringWidth(healIndicator), MC.fontRenderer.FONT_HEIGHT) : new Rectangle(9, 9);

            if(position.isCustom()) {
                bounds = position.applyTo(bounds);
            } else {
                Direction side = OverlayElements.HEALTH.getIndicatorSide();
                bounds = bounds.align(OverlayElements.HEALTH.getLastBounds().grow(SPACER, 0, SPACER, 0).getAnchor(side), side.mirrorCol());
            }

            if(mode.getIndex() == 0) {
                GlUtil.drawString(healIndicator, bounds.getPosition(), Direction.NORTH_WEST, Color.GREEN);
            } else {
                MC.getTextureManager().bindTexture(Textures.HUD_ICONS);
                MC.ingameGUI.blit(bounds.getX(), bounds.getY(), 0, 80, 9, 9);
            }
            return bounds;
    }

    /** @see net.minecraft.util.FoodStats#onUpdate(net.minecraft.entity.player.EntityPlayer) */
    @Override
    public boolean shouldRender(HudRenderContext context) {
        return MC.playerController.gameIsSurvivalOrAdventure()
            && MC.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
            && MC.player.getFoodStats().getFoodLevel() >= 18
            && MC.player.shouldHeal();
    }
}
