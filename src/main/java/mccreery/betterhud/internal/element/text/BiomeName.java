package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class BiomeName extends TextElement {
    @Override
    protected List<Text> getText(HudRenderContext context) {
        BlockPos blockPos = context.getClient().getCameraEntity().getBlockPos();
        World world = context.getClient().world;
        Identifier biome = world.getRegistryManager().get(Registry.BIOME_KEY).getId(world.getBiome(blockPos));

        return Collections.singletonList(getName().append(": " + biome));
    }
}
