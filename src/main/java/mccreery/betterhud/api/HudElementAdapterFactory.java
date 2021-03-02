package mccreery.betterhud.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class HudElementAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!HudElement.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                HudElement hudElement = (HudElement)value;

                // Elements with fixed position should not serialize their position as it is meaningless
                if (hudElement.getFixedPosition()) {
                    JsonElement tree = delegate.toJsonTree(value);

                    if (tree instanceof JsonObject) {
                        ((JsonObject)tree).remove("position");
                    }
                    gson.toJson(tree, out);
                } else {
                    delegate.write(out, value);
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T value = delegate.read(in);
                HudElement hudElement = (HudElement)value;

                // parent and children must remain synchronized
                for (HudElement child : hudElement.children) {
                    child.parent = hudElement;
                }
                return value;
            }
        };
    }
}
