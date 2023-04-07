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


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CamPath {

    private Player player;
    private Location start;
    private Location end;
    private Location step;
    private int durationInTicks;
    private int tick = 0;

    private List<Location> pathLocations = new ArrayList<>();


    //The method gets values given on calling the class and makes the instances
    public CamPath(Player player, Location start, Location end, int duration) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.durationInTicks = duration * 20;
    }



    //The method generates the locations for the path and puts them into a list called pathLocations
    public void generatePath() {
        //Making a step location, which gets added to the location before to fill the list
        double stepX = (end.getX() - start.getX()) / durationInTicks;
        double stepY = (end.getY() - start.getY()) / durationInTicks;
        double stepZ = (end.getZ() - start.getZ()) / durationInTicks;
        float stepYaw = (end.getYaw() - start.getYaw()) / durationInTicks;
        float stepPitch = (end.getPitch() - start.getPitch()) / durationInTicks;

        step = new Location(player.getWorld(), stepX, stepY, stepZ, stepYaw, stepPitch);


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


        runPath();
    }


    //This method actually makes the player move on the path by the predefined locations in the pathLocations list
    public void runPath() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //If it's the first iteration of the runnable the player gets teleported to the start and given the first velocity
                if (tick == 0) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(start);
                    Vector velocity = calculateVector(start, pathLocations.get(tick+1));
                    player.setVelocity(velocity);

                //If it's not the first iteration of the runnable we get the current location, at which the player should be after the velocity and teleport them to it.
                //Then we get the next location and calculate the next velocity and apply it to the player.
                } else if (tick >= durationInTicks) {
                    player.teleport(end);
                    cancel();
                } else if (tick > 0 && !(tick >= durationInTicks) && !(tick == pathLocations.size()-1)) {
                    player.teleport(pathLocations.get(tick));
                    Vector velocity = calculateVector(pathLocations.get(tick), pathLocations.get(tick+1));
                    player.setVelocity(velocity);
                }
                //Tick gets set one higher
                tick ++;
            }
        }.runTaskTimer(Main.getInstance(), 1, 1);
        System.out.println(pathLocations.get(0));
        System.out.println(pathLocations.get(1));
        System.out.println(pathLocations.get(2));
        System.out.println(pathLocations.get(3));
        System.out.println(pathLocations.get(4));
    }


    //This method calculates the velocity by a starting and ending position
    public Vector calculateVector(Location start, Location end) {
        return end.toVector().subtract(start.toVector());
    }


}
