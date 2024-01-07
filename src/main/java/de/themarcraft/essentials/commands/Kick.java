package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Kick implements CommandExecutor, TabCompleter {
    Main plugin;

    public Kick(Main pPlugin) {
        plugin = pPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        try {
            if (commandSender != Bukkit.getConsoleSender()) {
                Player player = (Player) commandSender;
                if (player.hasPermission("themarcraft.kick") || player.hasPermission("themarcraft.*")) {
                    if (plugin.getConfig().getBoolean("Enabled.CustomKickCommand")) {
                        if (args.length == 0) {
                            player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")));
                            return false;
                        }
                        if (args.length == 1) {
                            Player user = Bukkit.getPlayer(args[0]);
                            if (user != null) {
                                plugin.sendTeamMSG(plugin.getConfig().getString(("Messages.TeamKickMessage")).replace("[Player]", user.getDisplayName()).replace("[Banner]", player.getDisplayName()), player.getDisplayName());
                                user.kickPlayer(plugin.formatPlayer(plugin.getConfig().getString("Messages.KickMessage"), player).replace("[Reason]", "Kein Grund").replace("0", player.getDisplayName()));
                                return true;
                            } else {
                                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayerOffline").replace("[Player]", args[0])));
                            }
                        }
                        if (args.length >= 2) {
                            Player user = Bukkit.getPlayer(args[0]);
                            String result = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                            String reason = plugin.format(result);
                            if (user != null) {
                                plugin.sendTeamMSG(plugin.getConfig().getString(("Messages.TeamKickMessage")).replace("[Player]", user.getDisplayName()).replace("[Banner]", player.getDisplayName()), player.getDisplayName());
                                user.kickPlayer(plugin.formatPlayer(plugin.getConfig().getString("Messages.KickMessage").replace("[Reason]", reason), user).replace("[Banner]", player.getDisplayName()));
                                return true;
                            } else {
                                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayerOffline").replace("[Player]", args[0])));
                            }
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getConfig().getString("Messages.Disabled"));
                        return false;
                    }
                    return false;
                } else {
                    plugin.noPermission(player);
                }
                return false;
            } else {
                if (commandSender == Bukkit.getConsoleSender()) {
                    if (plugin.getConfig().getBoolean("Enabled.CustomKickCommand")) {
                        if (args.length == 0) {
                            plugin.log(plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")));
                            return false;
                        }
                        if (args.length == 1) {
                            Player user = Bukkit.getPlayer(args[0]);
                            if (user != null) {
                                plugin.sendTeamMSG(plugin.getConfig().getString(("Messages.TeamKickMessage")).replace("[Player]", user.getDisplayName()).replace("[Banner]", "Console"), "Console");
                                user.kickPlayer(plugin.formatPlayer(plugin.getConfig().getString("Messages.KickMessage").replace("[Banner]", "Console").replace("[Reason]", "Kein Grund").replace("[Player]", user.getDisplayName()), null));
                                return true;
                            } else {
                                plugin.log(plugin.format(plugin.getConfig().getString("Messages.ValidPlayerOffline").replace("[Player]", args[0])));
                            }
                        }
                        if (args.length >= 2) {
                            Player user = Bukkit.getPlayer(args[0]);
                            String result = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                            String reason = plugin.format(result);
                            if (user != null) {
                                plugin.sendTeamMSG(plugin.getConfig().getString(("Messages.TeamKickMessage")).replace("[Player]", user.getDisplayName()).replace("[Banner]", "Console"), "Console");
                                user.kickPlayer(plugin.formatPlayer(plugin.getConfig().getString("Messages.KickMessage").replace("[Reason]", reason).replace("[Banner]", "Console"), null));
                                return true;
                            } else {
                                plugin.log(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayerOffline").replace("[Player]", args[0])));
                            }
                        }
                    } else {
                        plugin.log(plugin.getConfig().getString("Messages.Disabled"));
                        return false;
                    }
                    return false;
                }

            }
        } catch (Exception e) {
            plugin.log(plugin.getConfig().getString("Messages.ErrorMessage"));
            plugin.log(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        try {
            if (Bukkit.getConsoleSender() != commandSender) {
                Player player = (Player) commandSender;
                if (player.hasPermission("themarcraft.kick")) {
                    if (args.length == 1) {
                        List<String> list = new ArrayList<>();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.getDisplayName().contains(args[0])) {
                                list.add(p.getDisplayName());
                            }
                        }
                        return list;
                    }
                    if (args.length == 2) {
                        List<String> list = new ArrayList<>();
                        for (String string : plugin.getConfig().getStringList("KickAutocompleteList"))
                            if (plugin.format(string).contains(args[1]) || plugin.format(string).contains(args[1].toUpperCase()) || plugin.format(string).contains(args[1].toLowerCase())) {
                                list.add(string);
                            }
                        return list;
                    }
                    if (args.length > 2) {
                        List<String> list = new ArrayList<>();
                        return list;
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Dafür hast du keine Rechte");
                    List<String> list = new ArrayList<>();
                    return list;
                }
            } else {
                if (args.length == 1) {
                    List<String> list = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getDisplayName().contains(args[0])) {
                            list.add(p.getDisplayName());
                        }
                    }
                    return list;
                }
                if (args.length == 2) {
                    List<String> list = new ArrayList<>();
                    for (String string : plugin.getConfig().getStringList("KickAutocompleteList"))
                        if (plugin.format(string).contains(args[1]) || plugin.format(string).contains(args[1].toUpperCase()) || plugin.format(string).contains(args[1].toLowerCase())) {
                            list.add(string);
                        }
                    return list;
                }
                if (args.length > 2) {
                    List<String> list = new ArrayList<>();
                    return list;
                }
            }

        } catch (Exception e) {
            plugin.log(plugin.getConfig().getString("Messages.ErrorMessage"));
        }
        return null;
    }
}
