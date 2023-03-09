package de.littleprogrammer.camrig.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSpectatingEvent implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && !(event.getPlayer().getSpectatorTarget() == null)) {
            if (event.getPlayer().getSpectatorTarget().getType().equals(EntityType.ARMOR_STAND)) {
                event.getPlayer().getSpectatorTarget().remove();
                player.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
