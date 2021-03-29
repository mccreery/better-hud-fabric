package mccreery.betterhud.internal.element.vanilla;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;

import jobicade.betterhud.element.OverlayElement;
import jobicade.betterhud.element.settings.DirectionOptions;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.EnumProperty;
import jobicade.betterhud.element.settings.SettingPosition;
import jobicade.betterhud.geom.Direction;
import jobicade.betterhud.util.bars.StatBar;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;

public abstract class Bar extends OverlayElement {
    protected SettingPosition position;
    protected EnumProperty side;

    private StatBar<? super ClientPlayerEntity> bar;

    public Bar(String name, StatBar<? super ClientPlayerEntity> bar) {
        super(name);
        this.bar = bar;

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.BAR);
        position.setContentOptions(DirectionOptions.CORNERS);
        addSetting(position);

        side = new EnumProperty("side", "west", "east");
        side.setEnableOn(() -> position.isDirection(Direction.SOUTH));
        addSetting(side);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        bar.setHost(MC.player);
        return bar.shouldRender();
    }

    /** @return {@link Direction#WEST} or {@link Direction#EAST} */
    protected Direction getContentAlignment() {
        if(position.isDirection(Direction.SOUTH)) {
            return side.getIndex() == 1 ? Direction.SOUTH_EAST : Direction.SOUTH_WEST;
        } else {
            return position.getContentAlignment();
        }
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        Direction contentAlignment = getContentAlignment();

        Rectangle bounds = new Rectangle(bar.getPreferredSize());

        if(position.isDirection(Direction.SOUTH)) {
            bounds = MANAGER.positionBar(bounds, contentAlignment.withRow(1), 1);
        } else {
            bounds = position.applyTo(bounds);
        }

        bar.setContentAlignment(contentAlignment).setBounds(bounds).render();
        return bounds;
    }
}
