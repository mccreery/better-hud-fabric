package mccreery.betterhud.internal.schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Data object describing the properties attached to a {@link mccreery.betterhud.api.HudElement HudElement}.
 *
 * <p>This data is used by the config GUI to create controls for each property.
 */
public class Schema {
    private int schemaVersion;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    private Map<String, SchemaProperty> properties = new HashMap<>();

    public Map<String, SchemaProperty> getProperties() {
        return properties;
    }
}
