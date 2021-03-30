package mccreery.betterhud.mixin;

import mccreery.betterhud.internal.FpsProvider;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements FpsProvider {
    @Shadow
    private static int currentFps;

    public int getCurrentFps() {
        return currentFps;
    }
}
