package mccreery.betterhud.api.property;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.function.BooleanSupplier;

/**
 * A getter-setter property with a GUI widget for changing its value.
 *
 * <p>Concrete properties should have a {@code get} method and a {@code set} method. These methods are not provided
 * because the property values are not needed for serialization or GUI construction.
 */
public abstract class Property {
    private final String name;
    private BooleanSupplier enableCondition;

    /**
     * Creates a property.
     * @param name The name used as the key for this property. It is also used in the default translation key.
     */
    public Property(String name) {
        this.name = name;
    }

    /**
     * Returns the name used as the key for this property. It is also used in the default translation key.
     * @return The name used as the key for this property.
     */
    public String getName() {
        return name;
    }

    /**
     * Saves the current setting value to a JSON element.
     *
     * @param gson The GSON instance for serializing JSON.
     * @return A JSON subtree representing the current value.
     */
    public abstract JsonElement saveJson(Gson gson);

    /**
     * Loads a new setting value from a JSON element. If the JSON is invalid or
     * incomplete, the value may partially change.
     *
     * @param gson The GSON instance for deserializing JSON.
     * @param element The JSON representing the new value.
     * @throws JsonSyntaxException if the JSON is invalid.
     */
    public abstract void loadJson(Gson gson, JsonElement element) throws JsonSyntaxException;

    /**
     * Sets a condition for the property to be enabled, usually based on the values of other properties. The condition
     * must return {@code true} for the property to be enabled. The widget will not be interactable if the property is
     * disabled.
     * @param enableCondition The new enable condition. Replaces any existing condition.
     */
    public void setEnableCondition(BooleanSupplier enableCondition) {
        this.enableCondition = enableCondition;
    }

    /**
     * Returns whether or not the property is enabled. The widget will not be interactable if the property is disabled.
     * @return {@code true} if the property is enabled.
     */
    public boolean isEnabled() {
        return enableCondition == null || enableCondition.getAsBoolean();
    }

    /**
     * Returns a widget to change the value of this property.
     * @return A widget with {@code this} as its property.
     */
    public abstract PropertyWidget getWidget();
}
