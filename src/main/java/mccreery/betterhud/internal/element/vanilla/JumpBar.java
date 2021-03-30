package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.util.math.Direction;

import java.lang.annotation.ElementType;

public class JumpBar extends OverlayElement {
    private SettingPosition position;

    public JumpBar() {
        super("jumpBar");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.BAR);
        position.setContentOptions(DirectionOptions.NORTH_SOUTH);
        addSetting(position);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderJumpBar
            && !OverlayHook.pre(context.getEvent(), ElementType.JUMPBAR);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

        Rectangle bounds = new Rectangle(182, 5);
        if(!position.isCustom() && position.getDirection() == Direction.SOUTH) {
            bounds = MANAGER.position(Direction.SOUTH, bounds, false, 1);
        } else {
            bounds = position.applyTo(bounds);
        }

        float charge = MC.player.getHorseJumpPower();
        int filled = (int)(charge * bounds.getWidth());

        GlUtil.drawRect(bounds, bounds.move(0, 84));

        if(filled > 0) {
            GlUtil.drawRect(bounds.withWidth(filled), new Rectangle(0, 89, filled, bounds.getHeight()));
        }

        OverlayHook.post(context.getEvent(), ElementType.JUMPBAR);
        return bounds;
    }
}
