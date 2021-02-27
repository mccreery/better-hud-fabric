package mccreery.betterhud;

/**
 * Fabric entrypoint called during Better HUD initialization. Registering HUD elements is allowed here.
 */
@FunctionalInterface
public interface BetterHudInitializer {
    void onBetterHudInitialize();
}
