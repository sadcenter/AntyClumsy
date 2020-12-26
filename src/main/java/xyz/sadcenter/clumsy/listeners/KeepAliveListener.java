package xyz.sadcenter.clumsy.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import xyz.sadcenter.clumsy.ClumsyPlugin;
import xyz.sadcenter.clumsy.basic.KeepAlive;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */

public final class KeepAliveListener extends PacketAdapter {

    private final ClumsyPlugin plugin;

    public KeepAliveListener(ClumsyPlugin plugin) {
        super(plugin, PacketType.Play.Client.KEEP_ALIVE);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        this.plugin.getKeepAliveMap().put(player.getUniqueId(), new KeepAlive(System.currentTimeMillis(), player.getLocation()));
        this.plugin.getWarnings().remove(player.getUniqueId());
    }
}
