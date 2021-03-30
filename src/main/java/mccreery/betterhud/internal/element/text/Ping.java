package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class Ping extends TextElement {
    @Override
    protected List<Text> getText(HudRenderContext context) {
        ClientPlayNetworkHandler networkHandler = context.getClient().getNetworkHandler();
        if (networkHandler != null) {
            PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(context.getClient().player.getUuid());

            if (playerListEntry != null) {
                int latency = playerListEntry.getLatency();
                return Collections.singletonList(Text.of(latency + " ms"));
            }
        }
        return Collections.emptyList();
    }
}
