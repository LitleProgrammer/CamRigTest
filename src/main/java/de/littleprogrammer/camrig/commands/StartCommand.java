package de.littleprogrammer.camrig.commands;

import de.littleprogrammer.camrig.CamHandler;
import de.littleprogrammer.camrig.Main;
import de.littleprogrammer.camrig.StartCam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    //private StartCam startCam;
    private CamHandler camHandler;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "/startCam <duration>");
            return false;
        }

        Main.getInstance().setDuration(Integer.valueOf(args[0]));
        camHandler = new CamHandler();
        camHandler.CamHandler((Player) sender);
        //startCam = new StartCam((Player) sender);

        return false;
    }
}
