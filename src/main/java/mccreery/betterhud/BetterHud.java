package mccreery.betterhud;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;

public class BetterHud implements ModInitializer {
    public static final String ID = "betterhud";

    @Override
    public void onInitialize() {
        EntrypointUtils.invoke(ID, BetterHudInitializer.class, BetterHudInitializer::onBetterHudInitialize);
    }
}
