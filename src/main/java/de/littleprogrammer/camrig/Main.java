package de.littleprogrammer.camrig;

import de.littleprogrammer.camrig.commands.Pos1Command;
import de.littleprogrammer.camrig.commands.Pos2Command;
import de.littleprogrammer.camrig.commands.StartCommand;
import de.littleprogrammer.camrig.commands.VectorCommand;
import de.littleprogrammer.camrig.listeners.PlayerSpectatingEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Plugin plugin = this;
    private Location pos1;
    private Location pos2;

    private int duration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getCommand("startCam").setExecutor(new StartCommand());
        getCommand("pos1").setExecutor(new Pos1Command());
        getCommand("pos2").setExecutor(new Pos2Command());
        getCommand("vector").setExecutor(new VectorCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerSpectatingEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
