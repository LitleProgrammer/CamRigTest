package de.littleprogrammer.camrig.commands;

import de.littleprogrammer.camrig.StartCam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private StartCam startCam;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        startCam = new StartCam((Player) sender);

        return false;
    }
}
