package mccreery.betterhud.internal.element.text;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class BiomeName extends TextElement {
    @Override
    protected List<String> getText() {
        BlockPos pos = new BlockPos((int)MC.player.getPosX(), 0, (int)MC.player.getPosZ());

        return Arrays.asList(getLocalizedName() + ": " + MC.world.getBiome(pos).getDisplayName().getFormattedText());
    }
}
