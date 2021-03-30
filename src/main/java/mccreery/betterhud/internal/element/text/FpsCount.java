package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.property.BooleanProperty;

import java.util.Arrays;
import java.util.List;

public class FpsCount extends TextElement {
    private final BooleanProperty numberOnly;

    public FpsCount() {
        numberOnly = new BooleanProperty("numberOnly", true);
        addProperty(numberOnly);
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
