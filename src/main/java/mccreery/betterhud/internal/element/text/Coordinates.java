package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.layout.Grid;
import mccreery.betterhud.api.layout.Label;
import mccreery.betterhud.api.layout.LayoutBox;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.DoubleProperty;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Coordinates extends HudElement {
    private final BooleanProperty spaced;
    private final DoubleProperty decimalPlaces;

    public Coordinates() {
        spaced = new BooleanProperty("spaced", true);
        addProperty(spaced);
        decimalPlaces = new DoubleProperty("precision", 1, 0, 5);
//        decimalPlaces.setInterval(1);
//        decimalPlaces.setUnlocalizedValue("betterHud.value.places");
        addProperty(decimalPlaces);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Entity player = context.getClient().player;

        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits((int)decimalPlaces.get());

        String xPart = "X: " + format.format(player.getX());
        String yPart = "Y: " + format.format(player.getY());
        String zPart = "Z: " + format.format(player.getZ());

        LayoutBox rootBox;

        if (spaced.get()) {
            List<Label> labels = Arrays.asList(
                new Label(context, Text.of(xPart)),
                new Label(context, Text.of(yPart)),
                new Label(context, Text.of(zPart))
            );
            // Min width 50
            Point cellSize = Point.max(new Point(50, 0), LayoutBox.getMaxSize(labels));

            Grid grid = new Grid(context, 1, 3, cellSize);
            grid.setColumnGutter(10);
            rootBox = grid;
        } else {
            String text = String.join(", ", xPart, yPart, zPart);
            rootBox = new Label(context, Text.of(text));
        }

        Rectangle bounds = context.calculateBounds(rootBox.getPreferredSize());
        rootBox.applyLayout(bounds);
        rootBox.render();
        return bounds;
    }
}
