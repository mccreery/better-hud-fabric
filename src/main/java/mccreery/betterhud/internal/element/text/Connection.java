package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.property.BooleanProperty;

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
    protected List<String> getText() {
        List<String> toRender = new ArrayList<String>(3);

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
