package mccreery.betterhud.api;

/**
 * Fabric entrypoint called during Better HUD initialization. Registering HUD elements is allowed here.
 */
public interface BetterHudInitializer {
    void onBetterHudInitialize();
}
