package xyz.sadcenter.clumsy;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.sadcenter.clumsy.basic.KeepAlive;
import xyz.sadcenter.clumsy.listeners.KeepAliveListener;
import xyz.sadcenter.clumsy.listeners.PlayerMoveListener;
import xyz.sadcenter.clumsy.listeners.PlayerQuitListener;
import xyz.sadcenter.clumsy.runnable.KeepAliveCheckerRunnable;
import xyz.sadcenter.clumsy.runnable.PPSCheckerRunnable;
import xyz.sadcenter.clumsy.runnable.PingCheckerRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sadcenter on 26.12.2020
 * @project KeepAlive
 */

public final class ClumsyPlugin extends JavaPlugin {

    private final Map<UUID, KeepAlive> lastKeepAliveMap = new ConcurrentHashMap<>();
    private final Map<UUID, AtomicInteger> playerMovePacketsPerSecond = new ConcurrentHashMap<>();
    private final Set<UUID> warnings = new HashSet<>();
    private final Properties properties = new Properties();

    @Override
    public void onLoad() {
        File file = new File(super.getDataFolder(), "config.properties");

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (!file.exists()) {
            this.properties.setProperty("keepAliveSeconds", "3");
            this.properties.setProperty("keepAliveMiliseconds", "0");
            this.properties.setProperty("maxPlayerMovePPS", "35");
            this.properties.setProperty("maxAcceptablePing", "130");
            this.properties.setProperty("command", "ban {PLAYER}");
            this.properties.setProperty("message", "&f{PLAYER} &6uzyl clumsy&f!");
            this.properties.setProperty("message-perm", "keepalive.messages");

            try (FileWriter fileWriter = new FileWriter(file)) {
                file.createNewFile();
                this.properties.store(fileWriter, "Optymalne wartosci: maxAcceptablePing 130 & keepAliveSeconds 3 & keepAliveMiliseconds 0 \nJezeli nie chcesz wykonywac komendy ustaw komende na 'null'");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } else {

            try (FileReader fileReader = new FileReader(file)) {
                this.properties.load(fileReader);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        }
    }

    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new KeepAliveListener(this));

        BukkitScheduler scheduler = super.getServer().getScheduler();
        scheduler.runTaskTimer(this, new KeepAliveCheckerRunnable(this), 10L, 10L);
        scheduler.runTaskTimer(this, new PPSCheckerRunnable(this), 20L, 20L);
        scheduler.runTaskTimer(this, new PingCheckerRunnable(this), 20L, 20L); // nie jestem pewien czy tutaj async potrzebny ale huj

        PluginManager pluginManager = super.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);
    }

    public String getString(String key) {
        String property = this.properties.getProperty(key);
        if (property == null)
            throw new NullPointerException("Nie znaleziono property z kluczem " + key + "! Prawdopodobnie usunales zawartosc configu!");
        return property;
    }

    public int getInteger(String key) {
        return Integer.parseInt(this.getString(key));
    }

    public Map<UUID, KeepAlive> getKeepAliveMap() {
        return this.lastKeepAliveMap;
    }

    public Map<UUID, AtomicInteger> getPlayerMovePPS() {
        return playerMovePacketsPerSecond;
    }

    public Set<UUID> getWarnings() {
        return warnings;
    }
}
