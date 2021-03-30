package mccreery.betterhud.internal.element.text;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class LightLevel extends TextElement {
    @Override
    protected List<String> getText() {
        BlockPos position = new BlockPos(MC.player.getPosX(), MC.player.getPosY(), MC.player.getPosZ());
        int light = MC.world.getChunkProvider().getLightManager().getLightSubtracted(position, 0);

        /*int light = 0;
        if(MC.world != null && MC.world.isBlockLoaded(position)) {
            light = MC.world.getLightFor(EnumSkyBlock.SKY, position) - MC.world.getSkylightSubtracted();
            light = Math.max(light, MC.world.getLightFor(EnumSkyBlock.BLOCK, position));
        }*/
        return Arrays.asList(getLocalizedName() + ": " + light);
    }
}
