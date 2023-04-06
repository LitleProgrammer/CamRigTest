package de.littleprogrammer.camrig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PathPlayer {
    private final Player player;
    private final Location startLocation;
    private final Location endLocation;
    private final int duration;
    private int elapsed;

    public PathPlayer(Player player, Location startLocation, Location endLocation, int durationSeconds) {
        this.player = player;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.duration = durationSeconds * 10; // Convert seconds to ticks
        this.elapsed = 0;
    }

    public void startMovement() {
        Vector direction = endLocation.toVector().subtract(startLocation.toVector()).normalize();
        double distance = startLocation.distance(endLocation);
        double speed = distance / (double) duration;
        Vector velocity = direction.multiply(speed / 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (elapsed >= duration) {
                    // Stop the movement and reset the player's position to the ending location
                    player.teleport(endLocation);
                    player.setVelocity(new Vector(0, 0, 0));
                    cancel();
                    return;
                }

                // Calculate the position of the player along the path
                double t = (double) elapsed / duration;
                Location newPosition = interpolate(startLocation, endLocation, t);

                // Set the player's position and rotation
                player.teleport(newPosition);
                lookAt(newPosition, endLocation);

                elapsed += 1; // Update the elapsed time (in ticks)
            }
        }.runTaskTimer(Main.getInstance(), 0, 2); // Run the task every tick
    }

    private Location interpolate(Location start, Location end, double t) {
        double x = start.getX() + (end.getX() - start.getX()) * t;
        double y = start.getY() + (end.getY() - start.getY()) * t;
        double z = start.getZ() + (end.getZ() - start.getZ()) * t;
        float yaw = interpolateYaw(start.getYaw(), end.getYaw(), t);
        float pitch = interpolatePitch(start.getPitch(), end.getPitch(), t);
        return new Location(start.getWorld(), x, y, z, yaw, pitch);
    }

    private float interpolateYaw(float start, float end, double t) {
        if (Math.abs(end - start) <= 180) {
            return (float) (start + (end - start) * t);
        } else {
            float delta = (360 + end - start) % 360;
            if (delta < 180) {
                return (float) ((start - delta) + delta * t);
            } else {
                return (float) ((start + delta) - delta * t);
            }
        }
    }

    private float interpolatePitch(float start, float end, double t) {
        return (float) (start + (end - start) * t);
    }

    private void lookAt(Location from, Location to) {
        Vector direction = to.toVector().subtract(from.toVector()).normalize();
        Location lookAtLocation = from.clone().add(direction);
        player.teleport(lookAtLocation);
        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(from);
        player.teleport(lookAtLocation);
    }
}

