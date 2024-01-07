package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed implements CommandExecutor {

    Main plugin;

    public Speed(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.log(plugin.format(plugin.getConfig().getString("Messages.PlayerOnly")));
            return false;
        } else {
            try {
                Player player = (Player) commandSender;
                Float speed = Float.valueOf(args[0]);
                if (speed <= 10) {
                    player.setWalkSpeed(speed / 10);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Speed.Success").replace("[Speed]", speed.toString())));
                    return true;
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Speed.Invalid").replace("[Speed]", speed.toString())));
                    return false;
                }
            } catch (Exception e) {
                commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Speed.Invalid")));
                return false;
            }
        }
    }
}
