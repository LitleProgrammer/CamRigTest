package de.littleprogrammer.camrig;

/*
* Getting the variables (player, startLoc, endLoc, duration)
* generate path
* - get start and end point
* - devide end by start point to get one step
* - in a for loop add startpoint to list, then get point before and add step and set to list
*
* runPath
* - in a runnable add tick if tick is at 1 teleport player to start pos
* - if tick is greater than 1 get from list tick
* - and teleport player to it
* - then get current location from tick and tick + 1 location and put it to the vector method
* - velocicates the player by the returned vector
*
* calculateVector
* - returns the end vector substracted by the start vector
 */


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class CamPath {


    //Locations
    private Location start;
    private Location end;
    private Location step;

    //Integers
    private int durationInTicks;
    private int tick = 0;
    private int taskId;

    //Lists
    private List<Location> pathLocations = new ArrayList<>();
    private List<UUID> playersInCam = new ArrayList<>();
    private List<UUID> players;

    //Before states
    private Map<UUID, GameMode> playersGamemodesBefore = new HashMap<>();
    private Map<UUID, Location> playersLocationsBefore = new HashMap<>();
    private Map<UUID, Boolean> playersFlyingBefore = new HashMap<>();



    //The method gets values given on calling the class and makes the instances
    public CamPath(List<UUID> players, Location start, Location end, int durationInS) {
        this.players = players;
        this.start = start;
        this.end = end;
        this.durationInTicks = durationInS * 20;
    }



    //The method generates the locations for the path and puts them into a list called pathLocations
    public void generatePath() {
        //Making a step location, which gets added to the location before to fill the list
        double stepX = (end.getX() - start.getX()) / durationInTicks;
        double stepY = (end.getY() - start.getY()) / durationInTicks;
        double stepZ = (end.getZ() - start.getZ()) / durationInTicks;
        float stepYaw = (end.getYaw() - start.getYaw()) / durationInTicks;
        float stepPitch = (end.getPitch() - start.getPitch()) / durationInTicks;

        step = new Location(Bukkit.getWorld("world"), stepX, stepY, stepZ, stepYaw, stepPitch);


        //Fills the list with locations (previous location + step location)
        pathLocations.add(start);
        for (int i = 1; i <= durationInTicks; i++) {
            Location prevLocation = pathLocations.get(i-1).clone();
            Location nextlocation = prevLocation.add(step);

            float yaw = nextlocation.getYaw() + stepYaw;
            float pitch = nextlocation.getPitch() + stepPitch;
            nextlocation.setYaw(yaw);
            nextlocation.setPitch(pitch);

            pathLocations.add(nextlocation);
        }

        //Storing before variables
        for (UUID playerUUID : players) {
            playersGamemodesBefore.put(playerUUID, Bukkit.getPlayer(playerUUID).getGameMode());
            playersLocationsBefore.put(playerUUID, Bukkit.getPlayer(playerUUID).getLocation());
            playersFlyingBefore.put(playerUUID, Bukkit.getPlayer(playerUUID).isFlying());

            playersInCam.add(playerUUID);
        }


        runPath();
    }


    //This method actually makes the player move on the path by the predefined locations in the pathLocations list
    public void runPath() {
         taskId =  new BukkitRunnable() {
            @Override
            public void run() {
                //Looping through all players and moving them
                for (UUID playerUUID : players) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    //If it's the first iteration of the runnable the player gets teleported to the start and given the first velocity
                    if (tick == 0) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(start);
                        Vector velocity = calculateVector(start, pathLocations.get(tick + 1));
                        player.setVelocity(velocity);

                        //If it's not the first iteration of the runnable get the current location, at which the player should be after the velocity and teleport them to it.
                        //Then we get the next location and calculate the next velocity and apply it to the player.
                        //And if the path is at his end teleport the player to it, cancel the runnable and call stop() method
                    } else if (tick >= durationInTicks) {
                        player.teleport(end);
                        cancel();
                        stop();
                    } else if (tick > 0 && !(tick >= durationInTicks) && !(tick == pathLocations.size() - 1)) {
                        player.teleport(pathLocations.get(tick));
                        Vector velocity = calculateVector(pathLocations.get(tick), pathLocations.get(tick + 1));
                        player.setVelocity(velocity);
                    }

                }
                //Tick gets set one higher
                tick++;
            }
        }.runTaskTimer(Main.getInstance(), 1, 1).getTaskId();
    }


    //Stopping everything and putting player back to the state where it started
    public void stop() {
        //I don't know if it needs a try catch but safe is safe right :D
        try {
            Bukkit.getScheduler().cancelTask(taskId);
        }catch (Exception e) {
            e.printStackTrace();
        }

        //Reading and setting default variables
        for (UUID playerUUID : players) {
            Player target = Bukkit.getPlayer(playerUUID);

            target.setGameMode(playersGamemodesBefore.get(playerUUID));
            target.teleport(playersLocationsBefore.get(playerUUID));
            target.setFlying(playersFlyingBefore.get(playerUUID));

            playersInCam.remove(playerUUID);
        }

    }


    //This method calculates the velocity by a starting and ending position
    public Vector calculateVector(Location start, Location end) {
        return end.toVector().subtract(start.toVector());
    }


}
