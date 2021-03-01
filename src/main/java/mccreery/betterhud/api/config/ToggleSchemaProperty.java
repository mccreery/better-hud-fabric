package mccreery.betterhud.api.config;

import com.google.gson.annotations.SerializedName;

public class ToggleSchemaProperty extends SchemaProperty {
    private ToggleSchemaProperty() {}

    private ToggleType toggleType = ToggleType.ENABLED;

    public ToggleType getToggleType() {
        return toggleType;
    }

    public enum ToggleType {
        @SerializedName("enabled")
        ENABLED,
        @SerializedName("visibility")
        VISIBILITY;
    }
}
