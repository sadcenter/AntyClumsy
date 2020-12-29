package xyz.sadcenter.clumsy.runnable;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sadcenter.clumsy.ClumsyPlugin;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */
@RequiredArgsConstructor
public final class PPSCheckerRunnable implements Runnable {

    private final ClumsyPlugin plugin;

    @Override
    public void run() {
        this.plugin.getPlayerMovePPS().forEach((uuid, packets) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (packets.get() < plugin.getInteger("maxPlayerMovePPS"))
                return;

            Bukkit.broadcast(
                    StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                            this.plugin.getString("message")), "{PLAYER}", player.getName()),
                    this.plugin.getString("message-perm"));

            String command = this.plugin.getString("command");
            if (!command.equalsIgnoreCase("null")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(command, "{PLAYER}", player.getName()));
            }
            packets.set(0);

        });


    }
}