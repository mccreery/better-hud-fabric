package mccreery.betterhud.internal.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mccreery.betterhud.api.config.Schema;
import mccreery.betterhud.api.config.SchemaProperty;

import java.io.IOException;

/**
 * Creates type adapters only for {@link Schema} using the Gson instance's {@link SchemaProperty} adapter.
 * <p>Writing is not currently supported.
 */
public class SchemaTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != Schema.class) {
            return null;
        }

        TypeAdapter<SchemaProperty> schemaPropertyTypeAdapter = gson.getAdapter(SchemaProperty.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T read(JsonReader in) throws IOException {
                Schema schema = new Schema();

                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();

                    if (name.equals("schemaVersion")) {
                        schema.setSchemaVersion(in.nextInt());
                    } else {
                        SchemaProperty property = schemaPropertyTypeAdapter.read(in);
                        schema.getProperties().put(name, property);
                    }
                }

                // Return value must be type T, but T is known to be Schema
                @SuppressWarnings("unchecked")
                T t = (T)schema;
                return t;
            }
        }.nullSafe();
    }
}
