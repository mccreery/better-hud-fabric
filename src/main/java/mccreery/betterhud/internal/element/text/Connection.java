package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MC;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import jobicade.betterhud.element.settings.Legend;
import mccreery.betterhud.api.property.BooleanProperty;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;

public class Connection extends TextElement {
    private BooleanProperty playerCount, showIp, latency;

    private String ip = "localServer";

    public Connection() {
        super("connection");

        addSetting(new Legend("misc"));

        playerCount = new BooleanProperty("playerCount");
        playerCount.setValuePrefix(BooleanProperty.VISIBLE);
        addSetting(playerCount);

        showIp = new BooleanProperty("showIp");
        addSetting(showIp);
        latency = new BooleanProperty("latency");
        addSetting(latency);
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
