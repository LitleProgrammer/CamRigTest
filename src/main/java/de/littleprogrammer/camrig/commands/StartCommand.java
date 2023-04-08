package de.littleprogrammer.camrig.commands;

import de.littleprogrammer.camrig.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartCommand implements CommandExecutor {

    //private StartCam startCam;
    //private CamHandler camHandler;

    private CamPath camPath;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length > 2) {
            sender.sendMessage(ChatColor.RED + "/startCam <duration> <extraPlayer>");
            return false;
        }

        Player player = (Player) sender;

        //Main.getInstance().setDuration(Integer.valueOf(args[0]));
        //camHandler = new CamHandler();
        //camHandler.CamHandler((Player) sender);
        //startCam = new StartCam((Player) sender);
        List<UUID> players = new ArrayList<>();
        players.add(player.getUniqueId());
        Player chactus = Bukkit.getPlayer(args[1]);
        players.add(chactus.getUniqueId());


        camPath = new CamPath(players, Main.getInstance().getPos1(), Main.getInstance().getPos2(), Integer.valueOf(args[0]));
        camPath.generatePath();
        return false;
    }
}
