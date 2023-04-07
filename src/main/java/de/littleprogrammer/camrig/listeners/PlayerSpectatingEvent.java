package de.littleprogrammer.camrig.listeners;

import de.littleprogrammer.camrig.CamPath;
import de.littleprogrammer.camrig.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSpectatingEvent implements Listener {

    private CamPath camPath;

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && Main.getInstance().getPlayersInCam().contains(player.getUniqueId())) {

            Main.getInstance().getPlayersInCam().remove(player.getUniqueId());
        }
    }
}
