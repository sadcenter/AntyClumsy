package xyz.sadcenter.clumsy.runnable;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.sadcenter.clumsy.ClumsyPlugin;
import xyz.sadcenter.clumsy.basic.KeepAlive;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */
@RequiredArgsConstructor
public final class KeepAliveCheckerRunnable implements Runnable {

    private final ClumsyPlugin plugin;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uniqueId = player.getUniqueId();
            KeepAlive keepAlive = this.plugin.getKeepAliveMap().get(uniqueId);

            if (keepAlive == null) {
                return;
            }

            if (this.plugin.getWarnings().contains(uniqueId)) {
                return;
            }

            if (keepAlive.getTime() > System.currentTimeMillis() - (TimeUnit.SECONDS.toMillis(this.plugin.getInteger("keepAliveSeconds")) + (TimeUnit.MILLISECONDS.toMillis(this.plugin.getInteger("keepAliveMiliseconds")))))
                return;

            this.plugin.getWarnings().add(uniqueId);

            player.teleport(keepAlive.getLocation());

            Bukkit.broadcast(
                    StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                            this.plugin.getString("message")), "{PLAYER}", player.getName()),
                    this.plugin.getString("message-perm"));

            String command = this.plugin.getString("command");
            if (!command.equalsIgnoreCase("null")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(command, "{PLAYER}", player.getName()));
            }
        });

    }
}
