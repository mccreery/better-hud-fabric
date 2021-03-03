package mccreery.betterhud;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudElementAdapterFactory;
import mccreery.betterhud.api.config.SchemaAdapterFactory;
import mccreery.betterhud.api.config.SchemaProperty;
import mccreery.betterhud.api.config.ToggleSchemaProperty;
import mccreery.betterhud.api.layout.EnumAdapterFactory;
import mccreery.betterhud.api.layout.HudLayout;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

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
     * Should not be called before the {@code betterhud} entrypoint.
     * @throws IllegalStateException If Better HUD has not been initialized.
     */
    @NotNull
    public static BetterHud getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Better HUD not yet initialized");
        }
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;
        layoutFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve(Paths.get("config", "betterhud-layout.json"));

        EntrypointUtils.invoke(ID, BetterHudInitializer.class, BetterHudInitializer::onBetterHudInitialize);
        initializeGson();
    }

    private Gson gson;

    private void initializeGson() {
        // Use subtypes for HudElement
        RuntimeTypeAdapterFactory<HudElement> elementTypeFactory = RuntimeTypeAdapterFactory.of(HudElement.class);

        for (Identifier id : HudElement.REGISTRY.getIds()) {
            Class<? extends HudElement> clazz = HudElement.REGISTRY.get(id);
            elementTypeFactory.registerSubtype(clazz, id.toString());
        }

        // Use subtypes for SchemaProperty
        TypeAdapterFactory propertyTypeFactory = RuntimeTypeAdapterFactory.of(SchemaProperty.class)
                .registerSubtype(ToggleSchemaProperty.class, "toggle");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(elementTypeFactory)
                .registerTypeAdapterFactory(propertyTypeFactory)
                // Use camelCase for enums such as Anchor (topLeft, center etc.)
                .registerTypeAdapterFactory(new EnumAdapterFactory(
                        CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)))
                // Populate parent (not present in JSON) and don't serialize positions where not needed
                .registerTypeAdapterFactory(new HudElementAdapterFactory())
                // Translate JSON properties to schema properties
                .registerTypeAdapterFactory(new SchemaAdapterFactory())
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
