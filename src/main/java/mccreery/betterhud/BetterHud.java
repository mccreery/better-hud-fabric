package mccreery.betterhud;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BetterHud implements ModInitializer {
    public static final String ID = "betterhud";

    public static final Registry<HudElement> REGISTRY = FabricRegistryBuilder
            .createSimple(HudElement.class, new Identifier(ID, "element"))
            .buildAndRegister();

    @Override
    public void onInitialize() {
        EntrypointUtils.invoke(ID, BetterHudInitializer.class, BetterHudInitializer::onBetterHudInitialize);
    }
}
