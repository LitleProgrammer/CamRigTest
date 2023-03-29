package de.littleprogrammer.camrig;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StartCam {
    private Location pos1;
    private Location pos2;

    private Vector vector1;
    private Vector vector2;

    int singleFrameDurationMs = 50;

    private int steps;

    private int duration;

    private Location step;
    private Vector stepVector;

    private double stepVectorX;
    private double stepVectorY;
    private double stepVectorZ;


    public StartCam(Player player){

        player.sendMessage("ran StartCam method");

        if (Main.getInstance().getPos1() == null || Main.getInstance().getPos2() == null){
            player.sendMessage(ChatColor.RED + "Bitte setze erst den Start/End punkt mit /pos1 oder /pos2");
            return;
        }else {
            pos1 = Main.getInstance().getPos1();
            pos2 = Main.getInstance().getPos2();

            vector1 = pos1.toVector();
            vector2 = pos2.toVector();

            duration = Main.getInstance().getDuration();
        }
        //player.sendMessage(player.getAddress().getAddress().toString());

        steps = (duration * 1000) / singleFrameDurationMs;

        step = new Location(player.getWorld(), 1, 1, 1, 1, 1);

        stepVectorX = (vector2.getX() - vector1.getX()) / steps;
        stepVectorY = (vector2.getY() - vector1.getY()) / steps;
        stepVectorZ = (vector2.getZ() - vector1.getZ()) / steps;

        stepVector = new Vector(stepVectorX, stepVectorY, stepVectorZ);

        System.out.println("StepVector: " + stepVector.toString() + " Vector1: " + vector1.toString() + " Vector2: " + vector2.toString());

        /*step.setX((pos2.getX() - pos1.getX()) / steps);
        step.setY((pos2.getY() - pos1.getY()) / steps);
        step.setZ((pos2.getZ() - pos1.getZ()) / steps);*/
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

        //player.setSpectatorTarget(camrig);
        player.teleport(pos1);

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
                //camrig.teleport(camrig.getLocation().add(step));
                player.setVelocity(stepVector);
                player.getLocation().setYaw(player.getLocation().getYaw() + step.getYaw());
                player.getLocation().setPitch(player.getLocation().getPitch() + step.getPitch());
                float newYaw = player.getLocation().getYaw() + step.getYaw();
                float newPitch = player.getLocation().getPitch() + step.getPitch();
                Location yawPitchLoc = player.getLocation();
                yawPitchLoc.setPitch(newPitch);
                yawPitchLoc.setYaw(newYaw);
                player.teleport(yawPitchLoc);
            }
        } ).runTaskTimer(Main.getInstance(), 1, 1);

    }
}
