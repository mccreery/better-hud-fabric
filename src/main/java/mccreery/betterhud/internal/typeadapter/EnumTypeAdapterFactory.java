package mccreery.betterhud.internal.typeadapter;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.function.Function;

/**
 * Formats enum field names through a converter before serializing.
 */
public class EnumTypeAdapterFactory implements TypeAdapterFactory {
    private final Function<String, String> nameFormatter;

    /**
     * @param nameFormatter Converts from enum field names to JSON names.
     */
    public EnumTypeAdapterFactory(Function<String, String> nameFormatter) {
        this.nameFormatter = nameFormatter;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!type.getRawType().isEnum()) {
            return null;
        }
        // T is an enum so it cannot be generic, therefore its raw type is itself
        @SuppressWarnings("unchecked")
        Class<T> enumClass = (Class<T>)type.getRawType();

        // Maps enum values to their formatted names and back
        BiMap<T, String> formattedNameMap = HashBiMap.create();
        for (T value : enumClass.getEnumConstants()) {
            formattedNameMap.put(value, nameFormatter.apply(value.toString()));
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                out.value(formattedNameMap.get(value));
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return formattedNameMap.inverse().get(in.nextString());
            }
        }.nullSafe();
    }
}
