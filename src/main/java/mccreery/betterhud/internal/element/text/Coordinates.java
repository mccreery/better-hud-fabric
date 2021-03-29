package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jobicade.betterhud.element.settings.Legend;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.DoubleProperty;
import jobicade.betterhud.geom.Direction;
import mccreery.betterhud.api.geometry.Point;
import jobicade.betterhud.geom.Size;
import jobicade.betterhud.render.Grid;
import jobicade.betterhud.render.Label;
import net.minecraft.client.resources.I18n;

public class Coordinates extends TextElement {
    private BooleanProperty spaced;
    private DoubleProperty decimalPlaces;

    public Coordinates() {
        super("coordinates");

        addSetting(new Legend("misc"));
        spaced = new BooleanProperty("spaced");
        addSetting(spaced);
        decimalPlaces = new DoubleProperty("precision", 0, 5);
        decimalPlaces.setInterval(1);
        decimalPlaces.setUnlocalizedValue("betterHud.value.places");
        addSetting(decimalPlaces);
    }

    @Override
    public Rectangle render(HudRenderContext context, List<String> text) {
        if(!spaced.get() || !position.isDirection(Direction.NORTH) && !position.isDirection(Direction.SOUTH)) {
            return super.render(context, text);
        }

        Grid<Label> grid = new Grid<>(new Point(3, 1), text.stream().map(Label::new).collect(Collectors.toList()))
            .setCellAlignment(position.getDirection()).setGutter(new Point(5, 5));

        Size size = grid.getPreferredSize();
        if(size.getX() < 150) size = size.withX(150);
        Rectangle bounds = MANAGER.position(position.getDirection(), new Rectangle(size));

        grid.setBounds(bounds).render();
        return bounds;
    }

    @Override
    protected List<String> getText() {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits((int)decimalPlaces.getValue());

        String x = format.format(MC.player.getPosX());
        String y = format.format(MC.player.getPosY());
        String z = format.format(MC.player.getPosZ());

        if(spaced.get()) {
            x = I18n.format("betterHud.hud.x", x);
            y = I18n.format("betterHud.hud.y", y);
            z = I18n.format("betterHud.hud.z", z);
            return Arrays.asList(x, y, z);
        } else {
            return Arrays.asList(I18n.format("betterHud.hud.xyz", x, y, z));
        }
    }
}