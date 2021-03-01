package mccreery.betterhud.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import mccreery.betterhud.api.HudElement;

public class ConfigLoader {
    public static <T extends HudElement> void loadConfig(T element, Class<T> elementClass, JsonReader reader) {
        // TODO delegate old schema versions
        new GsonBuilder()
                .registerTypeAdapter(elementClass, (InstanceCreator<T>)type -> element)
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(reader, elementClass);
    }

    public static SchemaSet LoadSchemaSet(JsonReader jsonReader) {
        TypeAdapterFactory propertyAdapterFactory = RuntimeTypeAdapterFactory.of(SchemaProperty.class, "type")
                .registerSubtype(ToggleSchemaProperty.class, "toggle");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SchemaAdapterFactory())
                .registerTypeAdapterFactory(propertyAdapterFactory)
                .create();

        return gson.fromJson(jsonReader, SchemaSet.class);
    }
}
