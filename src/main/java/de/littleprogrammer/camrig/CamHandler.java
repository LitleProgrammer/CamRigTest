package de.littleprogrammer.camrig;

import com.sun.tools.javac.file.Locations;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CamHandler {

    private Location pos1;
    private Location pos2;
    private Location playerStartPos;
    private Vector vec1;
    private Vector vec2;
    private Vector vecStep;

    private int duration;
    private int frameDurationMs = 50;
    private int steps;
    private int taskId;
    private int tick = 0;

    private float yawStep;
    private float pitchStep;

    private GameMode playerStartGamemode;


    public void  CamHandler(Player player) {

        if (Main.getInstance().getPos1() == null || Main.getInstance().getPos2() == null || Main.getInstance().getDuration() <= 0) {
            player.sendMessage(ChatColor.RED + "Please set a pos1 and a pos2 first! And Provide a duration");
            return;
        }

        //Gets and sets values from Main
        pos1 = Main.getInstance().getPos1();
        pos2 = Main.getInstance().getPos2();
        duration = Main.getInstance().getDuration();

        //Gets vectors from locations
        vec1 = pos1.toVector();
        vec2 = pos2.toVector();

        //calculating the amount of steps
        steps = (duration * 1000) / frameDurationMs;

        //Making a new Vector containing the movement amount for one step
        vecStep = new Vector((vec2.getX() - vec1.getX()) / steps, (vec2.getY() - vec1.getY()) / steps, (vec2.getZ() - vec1.getZ()) / steps);
        yawStep = (pos2.getYaw() - pos1.getYaw()) / steps;
        pitchStep = (pos2.getPitch() - pos1.getPitch()) / steps;


        System.out.println("VecSTep: " + vecStep.toString());

        //Running start method
        start(player);

    }

    public Vector velocityCalculator(Location start, Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }

    public void start(Player player) {

        //Saving current pos and gamemode of player and making player spectator
        playerStartPos = player.getLocation();
        playerStartGamemode = player.getGameMode();

        player.setGameMode(GameMode.SPECTATOR);

        Main.getInstance().getPlayersInCam().add(player.getUniqueId());

        //Teleporting the player to the start position
        player.teleport(pos1);

        //Moving the player
        taskId = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (tick >= steps) {
                    player.setVelocity(new Vector(0, 0, 0));
                    stop(player);
                    Bukkit.getScheduler().cancelTask(taskId);
                }else {
                    Location currentLoc = player.getLocation();

                    Location nextLoc = currentLoc;
                    nextLoc.setYaw(yawStep + currentLoc.getYaw());
                    nextLoc.setPitch(pitchStep + currentLoc.getPitch());

                    //Use Set Player Rotation packet

                    player.setVelocity(vecStep);
                    System.out.println("tick: " + tick + " von " + steps);
                    tick++;
                }


            }
        }, 1, 1).getTaskId();

    }

    public void stop(Player player) {

        //Setting player back to start pos and gamemode
        if (playerStartPos != null) {
            player.teleport(playerStartPos);
        } else {
            player.teleport(pos1);
        }

        if (playerStartGamemode != null) {
            player.setGameMode(playerStartGamemode);
        }



        Bukkit.getScheduler().cancelTask(taskId);

    }

}
