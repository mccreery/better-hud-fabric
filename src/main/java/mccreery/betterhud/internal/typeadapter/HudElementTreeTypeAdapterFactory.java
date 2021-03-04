package mccreery.betterhud.internal.typeadapter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.internal.layout.HudElementTree;
import mccreery.betterhud.internal.layout.RelativePosition;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class HudElementTreeTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != HudElementTree.class) {
            return null;
        }
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, type);

        TypeAdapter<HudElement> elementAdapter = gson.getAdapter(HudElement.class);
        TypeAdapter<Set<HudElementTree>> setAdapter = gson.getAdapter(new TypeToken<Set<HudElementTree>>() {});
        TypeAdapter<RelativePosition> positionAdapter = gson.getAdapter(RelativePosition.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegateAdapter.write(out, value);
            }

            @SuppressWarnings("unchecked")
            @Override
            public T read(JsonReader in) throws IOException {
                HudElement element = null;
                Set<HudElementTree> children = Collections.emptySet();
                RelativePosition position = null;

                in.beginObject();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                        case "element": element = elementAdapter.read(in); break;
                        case "children": children = setAdapter.read(in); break;
                        case "position": position = positionAdapter.read(in); break;
                    }
                }
                in.endObject();

                if (element == null) {
                    throw new JsonParseException("Element field is required");
                }

                HudElementTree tree = HudElementTree.create(element);
                for (HudElementTree child : children) {
                    child.setParent(tree);
                }
                if (position != null) {
                    tree.setPosition(position);
                }
                return (T)tree;
            }
        }.nullSafe();
    }
}
