package mccreery.betterhud.api.property;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class BooleanProperty extends Property {
    private boolean value;
    private String falseTranslationKey;
    private String trueTranslationKey;

    public BooleanProperty(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public String getTrueTranslationKey() {
        return trueTranslationKey;
    }

    public void setTrueTranslationKey(String trueTranslationKey) {
        this.trueTranslationKey = trueTranslationKey;
    }

    public String getFalseTranslationKey() {
        return falseTranslationKey;
    }

    public void setFalseTranslationKey(String falseTranslationKey) {
        this.falseTranslationKey = falseTranslationKey;
    }

    @Override
    public JsonElement saveJson(Gson gson) {
        return new JsonPrimitive(value);
    }

    @Override
    public void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            value = element.getAsJsonPrimitive().getAsBoolean();
        } else {
            throw new JsonSyntaxException("Not a boolean");
        }
    }

    @Override
    public PropertyWidget getWidget() {
        return null; // TODO toggle button widget
    }
}
