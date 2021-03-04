package mccreery.betterhud.internal.schema;

public class ToggleSchemaProperty extends SchemaProperty {
    private ToggleType toggleType = ToggleType.ENABLED;

    public ToggleType getToggleType() {
        return toggleType;
    }

    public void setToggleType(ToggleType toggleType) {
        this.toggleType = toggleType;
    }

    public enum ToggleType {
        ENABLED,
        VISIBILITY
    }
}
