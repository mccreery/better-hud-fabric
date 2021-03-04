package mccreery.betterhud.api.config;

public class ToggleSchemaProperty extends SchemaProperty {
    private ToggleSchemaProperty() {}

    private ToggleType toggleType = ToggleType.ENABLED;

    public ToggleType getToggleType() {
        return toggleType;
    }

    public enum ToggleType {
        ENABLED,
        VISIBILITY;
    }
}
