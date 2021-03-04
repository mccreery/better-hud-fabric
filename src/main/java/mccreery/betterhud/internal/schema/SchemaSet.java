package mccreery.betterhud.internal.schema;

import java.util.HashSet;
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

    public void setFormatVersion(int formatVersion) {
        this.formatVersion = formatVersion;
    }

    private final Set<Schema> schemas = new HashSet<>();

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
