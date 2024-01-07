package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

    Main plugin;

    public Fly(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.log(plugin.format(plugin.getConfig().getString("Messages.PlayerOnly")));
            return false;
        } else if (args.length == 0) {
            Player player = (Player) commandSender;
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyDisabled")));
                return true;
            } else {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyEnabled")));
                return true;
            }
        } else {
            Player sender = (Player) commandSender;
            try {
                Float speed = Float.valueOf(args[0]);
                if (speed <= 10) {
                    sender.setFlySpeed(speed / 10);
                    sender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlySpeed").replace("[Speed]", speed.toString())));
                    return true;
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyInvalidNumber").replace("[Speed]", args[0])));
                    return false;
                }
            } catch (NumberFormatException n) {
                if (sender.hasPermission("themarcraft.fly.other")) {
                    try {
                        Player player = Bukkit.getPlayer(args[0]);
                        if (player.getAllowFlight()) {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                            player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyDisabled")));
                            sender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyOtherDisabled").replace("[Player]", player.getDisplayName())));
                            return true;
                        } else {
                            player.setAllowFlight(true);
                            player.setFlying(true);
                            player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyEnabled")));
                            sender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.FlyOtherEnabled").replace("[Player]", player.getDisplayName())));
                            return true;
                        }
                    } catch (Exception e) {
                        sender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")));
                        return false;
                    }
                } else {
                    plugin.noPermission(sender);
                    return false;
                }
            }
        }
    }
}
