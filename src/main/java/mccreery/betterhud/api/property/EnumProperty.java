package mccreery.betterhud.api.property;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class EnumProperty<T extends Enum<T>> extends Property {
    private T value;
    private Set<T> allowedValues;

    public EnumProperty(String name, T value) {
        super(name);
        this.value = value;
        allowedValues = EnumSet.allOf(value.getDeclaringClass());
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        if (!allowedValues.contains(value)) {
            throw new IllegalArgumentException("Invalid value " + value);
        }
        this.value = value;
    }

    public Set<T> getAllowedValues() {
        return Collections.unmodifiableSet(allowedValues);
    }

    @SafeVarargs
    public final void setAllowedValues(T... allowedValues) {
        setAllowedValues(Arrays.asList(allowedValues));
    }

    public void setAllowedValues(Collection<T> allowedValues) {
        if (!allowedValues.contains(value)) {
            throw new IllegalArgumentException("Current value is not in allowed values");
        }
        this.allowedValues = EnumSet.copyOf(allowedValues);
    }

    @Override
    public JsonElement saveJson(Gson gson) {
        return gson.toJsonTree(get());
    }

    @Override
    public void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException {
        T value = gson.fromJson(element, this.value.getDeclaringClass());

        try {
            set(value);
        } catch (IllegalArgumentException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public PropertyWidget getWidget() {
        return null; // TODO choose box widget
    }
}
