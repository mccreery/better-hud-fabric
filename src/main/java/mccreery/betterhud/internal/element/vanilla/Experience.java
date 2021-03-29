package mccreery.betterhud.internal.element.vanilla;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;

import jobicade.betterhud.element.OverlayElement;
import jobicade.betterhud.element.settings.DirectionOptions;
import jobicade.betterhud.element.settings.SettingPosition;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import jobicade.betterhud.geom.Direction;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.render.Color;
import jobicade.betterhud.util.GlUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class Experience extends OverlayElement {
    private SettingPosition position;

    public Experience() {
        super("experience");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.BAR);
        position.setContentOptions(DirectionOptions.NORTH_SOUTH);
        addSetting(position);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderExperiance
            && !ForgeIngameGui.renderJumpBar
            && !OverlayHook.pre(context.getEvent(), ElementType.EXPERIENCE)
            && MC.playerController.gameIsSurvivalOrAdventure();
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle bgTexture = new Rectangle(0, 64, 182, 5);
        Rectangle fgTexture = new Rectangle(0, 69, 182, 5);

        Rectangle barRect = new Rectangle(bgTexture);

        if(!position.isCustom() && position.getDirection() == Direction.SOUTH) {
            barRect = MANAGER.position(Direction.SOUTH, barRect, false, 1);
        } else {
            barRect = position.applyTo(barRect);
        }
        GlUtil.drawTexturedProgressBar(barRect.getPosition(), bgTexture, fgTexture, MC.player.experience, Direction.EAST);

        if(MC.player.experienceLevel > 0) {
            String numberText = String.valueOf(MC.player.experienceLevel);
            Point numberPosition = new Rectangle(GlUtil.getStringSize(numberText))
                .anchor(barRect.grow(6), position.getContentAlignment().mirrorRow()).getPosition();

            GlUtil.drawBorderedString(numberText, numberPosition.getX(), numberPosition.getY(), new Color(128, 255, 32));
        }

        OverlayHook.post(context.getEvent(), ElementType.EXPERIENCE);
        return barRect;
    }
}
