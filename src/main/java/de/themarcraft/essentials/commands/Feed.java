package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {

    Main plugin;

    public Feed(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            plugin.log(plugin.getConfig().getString("Messages.PlayerOnly"));
            return false;
        } else if (args.length == 0) {
            Player player = (Player) commandSender;
            player.setFoodLevel(20);
            player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Feed.Success"), player));
            return true;
        } else if (args.length == 1 && commandSender.hasPermission("themarcraft.feed.other")) {
            try {
                Player player = Bukkit.getPlayer(args[0]);
                player.setFoodLevel(20);
                commandSender.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Feed.Other"), player));
                player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Feed.Success"), player));
                return true;
            } catch (Exception e) {
                commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")));
                return false;
            }
        } else {
            plugin.noPermission((Player) commandSender);
        }
        return false;
    }
}
