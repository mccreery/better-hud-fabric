package mccreery.betterhud.api.property;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mccreery.betterhud.internal.render.Color;

public class ColorProperty extends Property {
    private Color value;

    public ColorProperty(String name, Color value) {
        super(name);
        this.value = value;
    }

    public Color get() {
        return value;
    }

    public void set(Color value) {
        this.value = value;
    }

    @Override
    public JsonElement saveJson(Gson gson) {
        return gson.toJsonTree(get());
    }

    @Override
    public void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException {
        set(gson.fromJson(element, Color.class));
    }

    @Override
    public PropertyWidget getWidget() {
        return null; // TODO color widget
    }
}
