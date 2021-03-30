package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.property.BooleanProperty;
import net.minecraft.client.resource.language.I18n;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Connection extends TextElement {
    private final BooleanProperty playerCount, showIp, latency;

    private String ip = "localServer";

    public Connection() {
        playerCount = new BooleanProperty("playerCount", true);
        addProperty(playerCount);

        showIp = new BooleanProperty("showIp", true);
        addProperty(showIp);
        latency = new BooleanProperty("latency", true);
        addProperty(latency);
    }

    public void setLocal() {
        ip = "localServer";
    }

    public void setRemote(SocketAddress address) {
        ip = address.toString();
    }

    @Override
    protected List<String> getText(HudRenderContext context) {
        List<String> toRender = new ArrayList<>(3);

        if(playerCount.get()) {
            int players = MC.getConnection().getPlayerInfoMap().size();
            String conn = I18n.format(players != 1 ? "betterHud.hud.players" : "betterHud.hud.player", players);
            toRender.add(conn);
        }

        if(showIp.get()) {
            toRender.add(I18n.format(ip.equals("localServer") ? "betterHud.hud.localServer" : "betterHud.hud.ip", ip));
        }

        if(latency.get() && MC.getCurrentServerData() != null) {
            NetworkPlayerInfo info = MC.getConnection().getPlayerInfo(MC.player.getUniqueID());

            if(info != null) {
                int ping = info.getResponseTime();
                toRender.add(I18n.format("betterHud.hud.ping", ping));
            }
        }
        return toRender;
    }
}
