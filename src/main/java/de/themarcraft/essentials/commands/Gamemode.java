package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

    Main plugin;

    public Gamemode(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.log(plugin.format(plugin.getConfig().getString("Messages.PlayerOnly")));
            return false;
        } else if (args.length == 1) {
            Player player = (Player) commandSender;
            if (args[0].equals("0") || args[0].equals("1") || args[0].equals("2") || args[0].equals("3") || args[0].equals("s") || args[0].equals("c") || args[0].equals("a") || args[0].equals("sp")) {
                if (args[0].equals("0") || args[0].equals("s")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Survival"))));
                } else if (args[0].equals("1") || args[0].equals("c")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Creative"))));
                } else if (args[0].equals("2") || args[0].equals("a")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Adventure"))));
                } else if (args[0].equals("3") || args[0].equals("sp")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Spectator"))));
                }
            } else {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Invalid")));
            }
        } else if (commandSender.hasPermission("themarcraft.gm.other")) {
            Player player = (Player) commandSender;
            try {
                Player target = Bukkit.getPlayer(args[0]);
                if (args[1].equals("0") || args[1].equals("s")) {
                    target.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Other").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Survival")).replace("[Player]", target.getDisplayName())));
                    target.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Survival"))));
                } else if (args[1].equals("1") || args[1].equals("c")) {
                    target.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Other").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Creative")).replace("[Player]", target.getDisplayName())));
                    target.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Creative"))));
                } else if (args[1].equals("2") || args[1].equals("a")) {
                    target.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Other").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Adventure")).replace("[Player]", target.getDisplayName())));
                    target.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Adventure"))));
                } else if (args[1].equals("3") || args[1].equals("sp")) {
                    target.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Other").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Spectator")).replace("[Player]", target.getDisplayName())));
                    target.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Success").replace("[Gamemode]", plugin.getConfig().getString("Messages.Gamemode.Spectator"))));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Gamemode.Invalid")));
                }
            } catch (Exception e) {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")));
            }
        } else {
            plugin.noPermission((Player) commandSender);
        }
        return false;
    }
}
