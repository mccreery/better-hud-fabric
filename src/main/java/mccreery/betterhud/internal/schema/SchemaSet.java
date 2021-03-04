package mccreery.betterhud.internal.schema;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data object containing a set of supported {@link Schema}s for a {@link mccreery.betterhud.api.HudElement HudElement}.
 */
public final class SchemaSet {
    private int formatVersion;

    public int getFormatVersion() {
        return formatVersion;
    }

    private Set<Schema> schemas;

    public Set<Schema> getSchemas() {
        return schemas;
    }

    /**
     * Converts the list of schemas to a map indexed by schema version.
     * @return A new map.
     */
    public Map<Integer, Schema> getSchemaMap() {
        return schemas.stream().collect(Collectors.toMap(Schema::getSchemaVersion, x -> x));
    }
}
