package mccreery.betterhud.api.property;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class DoubleProperty extends Property {
    private double value;
    private final double minValue;
    private final double maxValue;
    private double step;
    private String valueTranslationKey;

    public DoubleProperty(String name, double value, double minValue, double maxValue) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        set(value);
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Value out of range");
        }
        this.value = value;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public String getValueTranslationKey() {
        return valueTranslationKey;
    }

    public void setValueTranslationKey(String valueTranslationKey) {
        this.valueTranslationKey = valueTranslationKey;
    }
    @Override
    public JsonElement saveJson(Gson gson) {
        return new JsonPrimitive(value);
    }

    @Override
    public void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException {
        set(gson.fromJson(element, Double.class));
    }

    @Override
    public PropertyWidget getWidget() {
        return null; // TODO slider widget
    }
}
