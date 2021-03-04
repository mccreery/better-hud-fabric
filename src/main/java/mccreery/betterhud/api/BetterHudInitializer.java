package mccreery.betterhud.api;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Fabric entrypoint called during Better HUD initialization. Registering HUD elements is allowed here.
 */
public interface BetterHudInitializer {
    /**
     * Registers Better HUD element classes using the registry.
     * @see Registry#register(Registry, Identifier, Object)
     */
    void onBetterHudInitialize(Registry<Class<? extends HudElement>> registry);
}
