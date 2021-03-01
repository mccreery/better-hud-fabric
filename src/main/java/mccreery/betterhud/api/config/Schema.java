package mccreery.betterhud.api.config;

import java.util.Map;

/**
 * Data object describing the properties attached to a {@link mccreery.betterhud.api.HudElement HudElement}.
 *
 * <p>This data is used by the config GUI to create controls for each property.
 */
public class Schema {
    Schema() {}

    int schemaVersion;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    Map<String, SchemaProperty> properties;

    public Map<String, SchemaProperty> getProperties() {
        return properties;
    }
}
