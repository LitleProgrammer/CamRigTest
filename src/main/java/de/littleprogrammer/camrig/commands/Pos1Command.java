package de.littleprogrammer.camrig.commands;

import de.littleprogrammer.camrig.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pos1Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        Main.getInstance().setPos1(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Position 1 Set");

        return false;
    }
}
