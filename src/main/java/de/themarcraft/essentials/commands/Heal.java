package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

    Main plugin;

    public Heal(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            plugin.log(plugin.getConfig().getString("Messages.PlayerOnly"));
            return false;
        } else if (args.length == 0) {
            Player player = (Player) commandSender;
            player.setHealth(20);
            player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Heal.Success"), player));
            return true;
        } else if (args.length == 1 && commandSender.hasPermission("themarcraft.heal.other")) {
            try {
                Player player = Bukkit.getPlayer(args[0]);
                player.setHealth(20);
                commandSender.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Heal.Other"), player));
                player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Heal.Success"), player));
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
