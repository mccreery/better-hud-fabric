package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MC;

import java.util.Arrays;
import java.util.List;

import mccreery.betterhud.api.property.BooleanProperty;

public class FpsCount extends TextElement {
    private BooleanProperty numberOnly;

    public FpsCount() {
        super("fpsCount");

        numberOnly = new BooleanProperty("numberOnly");
        addSetting(numberOnly);
    }

    @Override
    protected List<String> getText() {
        String fps = MC.debug.substring(0, MC.debug.indexOf(' '));

        if(!numberOnly.get()) {
            fps = getLocalizedName() + ": " + fps;
        }
        return Arrays.asList(fps);
    }
}
