package xyz.sadcenter.clumsy.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sadcenter.clumsy.ClumsyPlugin;

import java.util.UUID;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */
@RequiredArgsConstructor
public final class PlayerQuitListener implements Listener {

    private final ClumsyPlugin plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();

        this.plugin.getKeepAliveMap().remove(uniqueId);
        this.plugin.getPlayerMovePPS().remove(uniqueId);
    }

}
