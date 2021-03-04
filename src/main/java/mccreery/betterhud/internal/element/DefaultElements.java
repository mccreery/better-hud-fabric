package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.BetterHudInitializer;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.internal.BetterHud;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DefaultElements implements BetterHudInitializer {
    @Override
    public void onBetterHudInitialize() {
        Registry<Class<? extends HudElement>> registry = HudElement.getRegistry();

        Registry.register(registry, new Identifier(BetterHud.ID, "block_viewer"), BlockViewer.class);
    }
}
