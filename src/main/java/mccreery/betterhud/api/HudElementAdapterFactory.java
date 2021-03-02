package mccreery.betterhud.api;

import com.google.gson.Gson;
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
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T value = delegate.read(in);

                // parent and children must remain synchronized
                HudElement hudElement = (HudElement)value;
                for (HudElement child : hudElement.children) {
                    child.parent = hudElement;
                }
                return value;
            }
        };
    }
}
