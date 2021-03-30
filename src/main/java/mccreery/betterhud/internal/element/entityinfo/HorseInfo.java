package mccreery.betterhud.internal.element.entityinfo;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.layout.Label;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.internal.BetterHud;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class HorseInfo extends HudElement {
    private final BooleanProperty jump;
    private final BooleanProperty speed;

    public HorseInfo() {
        setRenderPhase(HudRenderContext.Phase.LIVING_ENTITY_BILLBOARD);

        jump = new BooleanProperty("jump", true);
        addProperty(jump);
        speed = new BooleanProperty("speed", true);
        addProperty(speed);
    }

    @Override
    public boolean shouldRender(BillboardContext context) {
        return context.getPointedEntity() instanceof HorseEntity;
    }

    @Override
    public Rectangle render(BillboardContext context) {
        ArrayList<Label> infoParts = new ArrayList<Label>();
        HorseEntity entity = (HorseEntity)context.getPointedEntity();

        if(jump.get()) {
            infoParts.add(new Label(jump.getLocalizedName() + ": " + MathUtil.formatToPlaces(getJumpHeight(entity), 3) + "m"));
        }
        if(speed.get()) {
            infoParts.add(new Label(speed.getLocalizedName() + ": " + MathUtil.formatToPlaces(getSpeed(entity), 3) + "m/s"));
        }

        Grid<Label> grid = new Grid<Label>(new Point(1, infoParts.size()), infoParts).setGutter(new Point(2, 2));
        Rectangle bounds = new Rectangle(grid.getPreferredSize().add(10, 10));
        bounds = BetterHud.MANAGER.position(Direction.SOUTH, bounds);

        GlUtil.drawRect(bounds, Color.TRANSLUCENT);
        grid.setBounds(new Rectangle(grid.getPreferredSize()).anchor(bounds, Direction.CENTER)).render();
        return null;
    }

    /** Calculates horse jump height using a derived polynomial
     * @see <a href=https://minecraft.gamepedia.com/Horse#Jump_strength>Minecraft Wiki</a> */
    public double getJumpHeight(HorseEntity horse) {
        double jumpStrength = horse.getHorseJumpStrength();
        return jumpStrength * (jumpStrength * (jumpStrength * -0.1817584952 + 3.689713992) + 2.128599134) - 0.343930367;
    }

    /** Calculates horse speed using an approximate coefficient
     * @see <a href=https://minecraft.gamepedia.com/Horse#Movement_speed>Minecraft Wiki</a> */
    public double getSpeed(HorseEntity horse) {
        return horse.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 43.17037;
    }
}
