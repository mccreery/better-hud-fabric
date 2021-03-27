package mccreery.betterhud.internal;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.mojang.serialization.Lifecycle;
import mccreery.betterhud.api.BetterHudInitializer;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.internal.layout.HudLayout;
import mccreery.betterhud.internal.schema.SchemaProperty;
import mccreery.betterhud.internal.schema.ToggleSchemaProperty;
import mccreery.betterhud.internal.typeadapter.EnumTypeAdapterFactory;
import mccreery.betterhud.internal.typeadapter.HudElementTreeTypeAdapterFactory;
import mccreery.betterhud.internal.typeadapter.SchemaTypeAdapterFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Mod initializer calls entrypoint {@code betterhud} ({@link BetterHudInitializer}) when Better HUD is ready for
 * elements from other mods to be registered.
 */
public class BetterHud implements ModInitializer {
    public static final String ID = "betterhud";

    private static BetterHud instance;

    /**
     * @throws IllegalStateException if Better HUD is not yet initialized
     */
    public static BetterHud getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Better HUD is not yet initialized");
        }
        return instance;
    }

    private Registry<Class<? extends HudElement>> elementRegistry;

    public Registry<Class<? extends HudElement>> getElementRegistry() {
        return elementRegistry;
    }

    @Override
    public void onInitialize() {
        layoutFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve(Paths.get("config", "betterhud-layout.json"));

        // Cannot use FabricRegistryBuilder because it doesn't support generics
        elementRegistry = new SimpleRegistry<>(
                RegistryKey.ofRegistry(new Identifier(BetterHud.ID, "element")),
                Lifecycle.stable());

        instance = this;
        EntrypointUtils.invoke(ID, BetterHudInitializer.class, callback ->
                callback.onBetterHudInitialize(elementRegistry));

        ScreenEvents.AFTER_INIT.register(new LayoutButtonHandler());
        initializeGson();
        loadLayout();
    }

    private Gson gson;

    private void initializeGson() {
        // Use subtypes for HudElement
        RuntimeTypeAdapterFactory<HudElement> elementTypeFactory = RuntimeTypeAdapterFactory.of(HudElement.class);

        for (Identifier id : HudElement.getRegistry().getIds()) {
            Class<? extends HudElement> clazz = HudElement.getRegistry().get(id);
            elementTypeFactory.registerSubtype(clazz, id.toString());
        }

        // Use subtypes for SchemaProperty
        TypeAdapterFactory propertyTypeFactory = RuntimeTypeAdapterFactory.of(SchemaProperty.class)
                .registerSubtype(ToggleSchemaProperty.class, "toggle");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(elementTypeFactory)
                .registerTypeAdapterFactory(propertyTypeFactory)
                // Use camelCase for enums such as Anchor (topLeft, center etc.)
                .registerTypeAdapterFactory(new EnumTypeAdapterFactory(
                        CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)))
                // Populate parent (not present in JSON) and don't serialize positions where not needed
                .registerTypeAdapterFactory(new HudElementTreeTypeAdapterFactory())
                // Translate JSON properties to schema properties
                .registerTypeAdapterFactory(new SchemaTypeAdapterFactory())
                .create();
    }

    private Path layoutFilePath;

    public void loadLayout() {
        try (Reader reader = Files.newBufferedReader(layoutFilePath)) {
            layout = gson.fromJson(reader, HudLayout.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HudLayout layout;

    public HudLayout getLayout() {
        return layout;
    }
}
