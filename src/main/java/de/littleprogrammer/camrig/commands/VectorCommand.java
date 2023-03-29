package de.littleprogrammer.camrig.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VectorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);

        Vector vector = new Vector( Double.valueOf(args[0]),Double.valueOf(args[1]),Double.valueOf(args[2]));

        Player player = (Player) sender;
        player.setVelocity(vector);

        return false;
    }
}
