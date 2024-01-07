package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Unban implements CommandExecutor, TabCompleter {
    Main plugin;

    public Unban(Main pPlugin) {
        plugin = pPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (plugin.getConfig().getBoolean("Enabled.CustomBanCommand")) {
            if (Bukkit.getConsoleSender() != commandSender) {
                Player player = (Player) commandSender;
                if (commandSender.hasPermission("themarcraft.unban") || commandSender.hasPermission("themarcraft.*")) {
                    if (args.length == 1) {
                        try {
                            if (plugin.database.isBanned(args[0])) {
                                try {
                                    plugin.database.unban(args[0]);
                                    commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.UnbanSuccess").replace("[Player]", args[0])));
                                    return true;
                                } catch (Exception e) {
                                    commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ErrorMessage").replace("[Player]", args[0])));
                                    return false;
                                }
                            } else {
                                commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.PlayerNotBanned").replace("[Player]", args[0])));
                                return false;
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    commandSender.sendMessage(plugin.getPrefix() + plugin.getConfig().getString("Messages.ValidPlayer"));
                } else {
                    plugin.noPermission(player);
                }
                return false;
            } else {
                if (args.length == 1) {
                    try {
                        if (plugin.database.isBanned(args[0])) {
                            try {
                                plugin.database.unban(args[0]);
                                plugin.log(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.UnbanSuccess").replace("[Player]", args[0])));
                                return true;
                            } catch (Exception ex) {
                                plugin.log(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ErrorMessage").replace("[Player]", args[0])));
                                return false;
                            }
                        } else {
                            plugin.log(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.PlayerNotBanned").replace("[Player]", args[0])));
                            return false;
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                plugin.log(plugin.getConfig().getString("Messages.ValidPlayer"));
                return false;
            }
        } else {
            plugin.log(plugin.getConfig().getString("Messages.Disabled"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            try {
                List<String> bannedPlayers = plugin.database.getBanns();

                for (String playerName : bannedPlayers) {
                    if (playerName.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(playerName);
                    }
                }

                return completions;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return completions;
    }

}