package mccreery.betterhud.api.property;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jobicade.betterhud.geom.Point;
import jobicade.betterhud.gui.GuiElementSettings.Populator;

/**
 * A setting which can have children.
 */
public class ParentSetting extends Property {
    private final List<Property> children = new ArrayList<>();

    public ParentSetting(String name) {
        super(name);
    }

    public void addChild(Property setting) {
        children.add(setting);
        setting.parent = this;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public JsonElement saveJson(Gson gson) {
        JsonObject element = new JsonObject();

        for (Property childSetting : children) {
            element.add(childSetting.getName(), childSetting.saveJson(gson));
        }
        return element;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation accepts incomplete objects and updates only those
     * children which are present. Children after the first invalid one are not
     * updated.
     *
     * @throws JsonSyntaxException if the JSON is not an object or any of the
     * children are invalid.
     */
    @Override
    public void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException {
        if (!element.isJsonObject()) {
            throw new JsonSyntaxException("not an object");
        }
        JsonObject object = element.getAsJsonObject();

        for (Property child : children) {
            JsonElement childElement = object.get(child.getName());

            if (childElement != null) {
                child.loadJson(gson, childElement);
            }
        }
    }

    @Override
    public Point getGuiParts(Populator populator, Point topAnchor) {
        for (Property setting : children) {
            if (!setting.getHidden()) {
                topAnchor = setting.getGuiParts(populator, topAnchor);
            }
        }
        return topAnchor;
    }

    @Override
    public void draw() {
        for (Property child : children) {
            child.draw();
        }
    }

    @Override
    public void updateGuiParts() {
        for (Property child : children) {
            child.updateGuiParts();
        }
    }
}
