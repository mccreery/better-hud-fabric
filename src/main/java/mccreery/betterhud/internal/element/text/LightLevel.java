package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class LightLevel extends TextElement {
    @Override
    protected List<Text> getText(HudRenderContext context) {
        BlockPos position = context.getClient().player.getBlockPos();
        int light = context.getClient().world.getChunkManager().getLightingProvider().getLight(position, 0);

        return Collections.singletonList(Text.of(String.valueOf(light)));
    }
}
