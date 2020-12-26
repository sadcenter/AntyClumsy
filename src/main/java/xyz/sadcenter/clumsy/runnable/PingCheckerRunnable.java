package xyz.sadcenter.clumsy.runnable;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import xyz.sadcenter.clumsy.ClumsyPlugin;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */
@RequiredArgsConstructor
public final class PingCheckerRunnable implements Runnable {

    private final ClumsyPlugin plugin;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            int ping = ((CraftPlayer) player).getHandle().ping;
            if (ping > plugin.getInteger("maxAcceptablePing")) {
                Bukkit.broadcast(
                        StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                                this.plugin.getString("message")), "{PLAYER}", player.getName()),
                        this.plugin.getString("message-perm"));

                String command = this.plugin.getString("command");
                if (!command.equalsIgnoreCase("null")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(command, "{PLAYER}", player.getName()));
                }
            }
        });
    }

}
