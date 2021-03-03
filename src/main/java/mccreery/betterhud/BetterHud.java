package mccreery.betterhud;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mccreery.betterhud.api.HudElementAdapterFactory;
import mccreery.betterhud.api.config.SchemaAdapterFactory;
import mccreery.betterhud.api.layout.EnumAdapterFactory;
import mccreery.betterhud.api.layout.HudLayout;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;
import net.minecraft.client.MinecraftClient;
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
     * @throws IllegalStateException
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
    }

    private Path layoutFilePath;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new EnumAdapterFactory(
                    CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)))
            .registerTypeAdapterFactory(new HudElementAdapterFactory())
            .registerTypeAdapterFactory(new SchemaAdapterFactory())
            .create();

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
