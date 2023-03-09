package de.littleprogrammer.camrig;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCam {
    private Location pos1;
    private Location pos2;

    private int steps = 100;

    private Location step;

    public StartCam(Player player){

        player.sendMessage("ran StartCam method");

        if (Main.getInstance().getPos1() == null || Main.getInstance().getPos2() == null){
            player.sendMessage(ChatColor.RED + "Bitte setze erst den Start/End punkt mit /pos1 oder /pos2");
            return;
        }else {
            pos1 = Main.getInstance().getPos1();
            pos2 = Main.getInstance().getPos2();
        }
        player.sendMessage(player.getAddress().getAddress().toString());

        step = new Location(player.getWorld(), 1, 1, 1, 1, 1);

        step.setX((pos2.getX() - pos1.getX()) / steps);
        step.setY((pos2.getY() - pos1.getY()) / steps);
        step.setZ((pos2.getZ() - pos1.getZ()) / steps);
        step.setYaw((pos2.getYaw() - pos1.getYaw()) / steps);
        step.setPitch((pos2.getPitch() - pos1.getPitch()) / steps);

        player.setGameMode(GameMode.SPECTATOR);

        ArmorStand camrig = (ArmorStand) player.getWorld().spawnEntity(pos1, EntityType.ARMOR_STAND);
        camrig.setCustomNameVisible(false);
        camrig.setGravity(false);
        camrig.setInvulnerable(true);
        camrig.setBasePlate(false);
        camrig.setVisible(false);
        camrig.setGlowing(true);
        camrig.setRemoveWhenFarAway(true);

        player.setSpectatorTarget(camrig);

        CamPath(player, camrig);
    }

    public void CamPath(Player player, ArmorStand camrig){
        player.sendMessage("ran CamPath method");
        (new BukkitRunnable(){
            @Override
            public void run() {
                if (camrig.getLocation().equals(pos2)){
                    camrig.remove();
                    player.setGameMode(GameMode.CREATIVE);
                }
                camrig.teleport(camrig.getLocation().add(step));
                camrig.setRotation(camrig.getLocation().getYaw() + step.getYaw(), camrig.getLocation().getPitch() + step.getPitch());
            }
        } ).runTaskTimer(Main.getInstance(), 1, 1);

    }
}
