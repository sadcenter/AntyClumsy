package xyz.sadcenter.clumsy.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.sadcenter.clumsy.ClumsyPlugin;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */
@RequiredArgsConstructor
public final class PlayerMoveListener implements Listener {

    private final ClumsyPlugin plugin;

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            Player player = event.getPlayer();
            AtomicInteger integer = this.plugin.getPlayerMovePPS().get(player.getUniqueId());

            if (integer == null) {
                this.plugin.getPlayerMovePPS().put(player.getUniqueId(), new AtomicInteger());
                return;
            }

            int increment = integer.incrementAndGet();
            this.plugin.getPlayerMovePPS().remove(player.getUniqueId());
            this.plugin.getPlayerMovePPS().put(player.getUniqueId(), new AtomicInteger(increment));


        });
    }

}
