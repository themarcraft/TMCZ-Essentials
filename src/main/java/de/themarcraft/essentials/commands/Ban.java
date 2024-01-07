package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import de.themarcraft.essentials.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ban implements CommandExecutor, TabCompleter {
    Main plugin;
    Database database;

    public Ban(Main plugin, Database database) {
        this.plugin = plugin;
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (plugin.getConfig().getBoolean("Enabled.CustomBanCommand")) {
            try {
                if (commandSender != Bukkit.getConsoleSender()) {
                    Player player = (Player) commandSender;
                    if (player.hasPermission("themarcraft.ban") || player.hasPermission("themarcraft.*") || player.isOp()) {
                        if (args.length == 0) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Syntax: /ban <playername> <duration> <unit> <reason>");
                            player.sendMessage(plugin.getPrefix() + "Gebe einen Spielernamen an");
                            return false;
                        }
                        if (args.length == 1) {
                            player.sendMessage(plugin.getPrefix() + "Bitte gebe die Dauer des Banns an");
                        }
                        if (args.length == 2) {
                            Player user = Bukkit.getPlayer(args[0]);
                            if (user != null) {
                                ban(null, args[1], user, player);
                                return true;
                            } else {
                                player.sendMessage(plugin.getPrefix() + "Der Spieler " + args[0] + " ist Offline");
                            }
                        }
                        if (args.length >= 3) {
                            Player user = Bukkit.getPlayer(args[0]);
                            String reason = plugin.format(args[2]).replace("\\_", "\\unterstrich").replace("_", " ").replace("\\unterstrich", "_");
                            if (user != null) {
                                ban(args[2], args[1], user, player);
                                return true;
                            } else {
                                player.sendMessage(plugin.getPrefix() + "Der Spieler " + args[0] + " ist Offline");
                            }
                        }
                    } else {
                        plugin.noPermission(player);
                    }
                    return false;
                }// Console Command
                else {
                    if (args.length == 0) {
                        plugin.log(ChatColor.RED + "Syntax: /ban <playername> <duration> <unit> <reason>");
                        plugin.log("Gebe einen Spielernamen an");
                        return false;
                    }
                    if (args.length == 1) {
                        plugin.log("Bitte gebe die Dauer des Banns an");
                    }
                    if (args.length == 2) {
                        Player user = Bukkit.getPlayer(args[0]);
                        if (user != null) {
                            ban(null, args[1], user, commandSender);
                            return true;
                        } else {
                            plugin.log("Der Spieler " + args[0] + " ist Offline");
                        }
                    }
                    if (args.length >= 3) {
                        Player user = Bukkit.getPlayer(args[0]);
                        String reason = plugin.format(args[2]).replace("\\_", "\\unterstrich").replace("_", " ").replace("\\unterstrich", "_");
                        if (user != null) {
                            ban(args[2], args[1], user, commandSender);
                            return true;
                        } else {
                            plugin.log("Der Spieler " + args[0] + " ist Offline");
                        }
                    }
                }
                return false;
            } catch (Exception e) {
                plugin.log(plugin.getConfig().getString("Messages.ErrorMessage"));
                plugin.log(e.toString());
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        try {
            if (Bukkit.getConsoleSender() != commandSender) {
                Player player = (Player) commandSender;
                if (player.hasPermission("themarcraft.ban")) {
                    if (args.length == 1) {
                        List<String> completions = new ArrayList<>();

                        if (args.length == 1) {

                            if (args.length == 1) {

                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (p.getDisplayName().toLowerCase().startsWith(args[0].toLowerCase())) {
                                        completions.add(p.getDisplayName());
                                    }
                                }

                                return completions;
                            }
                        }

                        return completions;
                    }
                    if (args.length == 2) {
                        List<String> list = new ArrayList<>();
                        try {
                            try {
                                Integer.parseInt(args[1]);
                                list.add(args[1] + "h");
                                list.add(args[1] + "m");
                                list.add(args[1] + "s");
                                list.add(args[1] + "d");
                                list.add(args[1] + "w");
                                list.add(args[1] + "M");
                                list.add(args[1] + "y");
                            } catch (NumberFormatException ignored) {
                                try {
                                    Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                                } catch (NumberFormatException ex) {
                                    if ("perma".contains(args[1])) {
                                        if (player.hasPermission("themarcraft.ban.perma")) {
                                            list.add("perma");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            list.add("0h");
                            list.add("0m");
                            list.add("0s");
                            list.add("0d");
                            list.add("0w");
                            list.add("0M");
                            list.add("0y");
                            if (player.hasPermission("themarcraft.ban.perma")) {
                                list.add("perma");
                            }
                        }
                        return list;
                    }
                    if (args.length == 3) {
                        List<String> list = new ArrayList<>();
                        for (String string : plugin.getConfig().getStringList("BanAutocompleteList"))
                            if (plugin.format(string).contains(args[2]) || plugin.format(string).contains(args[2].toUpperCase()) || plugin.format(string).contains(args[2].toLowerCase())) {
                                list.add(plugin.format(string));
                            }
                        return list;
                    }
                    if (args.length > 3) {
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
                    List<String> completions = new ArrayList<>();

                    if (args.length == 1) {

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getDisplayName().toLowerCase().startsWith(args[0].toLowerCase())) {
                                completions.add(player.getDisplayName());
                            }
                        }

                        return completions;
                    }

                    return completions;
                }
                if (args.length == 2) {
                    List<String> list = new ArrayList<>();
                    try {
                        try {
                            Integer.parseInt(args[1]);
                            list.add(args[1] + "h");
                            list.add(args[1] + "m");
                            list.add(args[1] + "s");
                            list.add(args[1] + "d");
                            list.add(args[1] + "w");
                            list.add(args[1] + "M");
                            list.add(args[1] + "y");
                        } catch (NumberFormatException ignored) {
                            try {
                                Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                            } catch (NumberFormatException ex) {
                                if ("perma".contains(args[1])) {
                                    list.add("perma");
                                }
                            }
                        }
                    } catch (Exception e) {
                        list.add("0h");
                        list.add("0m");
                        list.add("0s");
                        list.add("0d");
                        list.add("0w");
                        list.add("0M");
                        list.add("0y");
                        list.add("perma");
                    }
                    return list;
                }
                if (args.length == 3) {
                    List<String> list = new ArrayList<>();
                    for (String string : plugin.getConfig().getStringList("BanAutocompleteList"))
                        if (plugin.format(string).contains(args[2])) {
                            list.add(plugin.format(string));
                        }
                    return list;
                }
                if (args.length > 3) {
                    List<String> list = new ArrayList<>();
                    return list;
                }
            }

        } catch (Exception e) {
            plugin.log(plugin.getConfig().getString("Messages.ErrorMessage"));
        }
        return null;
    }

    public void ban(@Nullable String reason, String unfDuration, Player player, CommandSender commandSender) throws SQLException {
        plugin.reloadConfig();
        boolean error = false;
        boolean noPerm = false;
        boolean console = false;
        int duration = 0;
        String unit = unfDuration;
        Boolean perma = false;
        Player bannedFrom = null;
        try {
            bannedFrom = (Player) commandSender;
            console = false;
        } catch (Exception e) {
            console = true;
        }
        try {
            if (!unfDuration.equals("perma")) {
                duration = Integer.valueOf(unfDuration.substring(0, unfDuration.length() - 1));
                unit = unfDuration.substring(unfDuration.length() - 1);
            } else {
                duration = 10;
            }
        } catch (NumberFormatException e) {
            commandSender.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.InvalidTimeUnit")));
            error = true;
        }
        Timestamp until = new Timestamp(System.currentTimeMillis());
        Timestamp newTS = new Timestamp(until.getTime());
        if (Objects.equals(unit, "s")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "h")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(60 * 60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "m")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "d")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(24 * 60 * 60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "w")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(7 * 24 * 60 * 60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "M")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(4L * 7 * 24 * 60 * 60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "y")) {
            @SuppressWarnings("removal") Long factor = Long.valueOf(365L * 24 * 60 * 60 * 1000);
            long milliSecToAdd = factor * duration;
            long newTimeMilliSec = newTS.getTime();
            newTS.setTime(newTimeMilliSec + milliSecToAdd);
        } else if (Objects.equals(unit, "perma")) {
            if (!console) {
                if (bannedFrom.hasPermission("themarcraft.ban.perma") || bannedFrom.hasPermission("themarcraft.*")) {
                    newTS = Timestamp.valueOf("2008-04-25 00:00:00.000");
                    perma = true;
                    noPerm = false;
                } else {
                    plugin.noPermission(player);
                    error = true;
                    noPerm = true;
                }
            } else {
                newTS = Timestamp.valueOf("2008-04-25 00:00:00.000");
                perma = true;
            }
        } else {
            if (!error || !noPerm) {
                error = true;
            }
        }
        if (!error) {

            String name;
            if (commandSender == Bukkit.getConsoleSender()) {
                name = "Console";
            } else {
                name = bannedFrom.getDisplayName();
            }
            database.ban(player, name, reason, new Timestamp(System.currentTimeMillis()), newTS, duration);

            String banEnd = new SimpleDateFormat(plugin.getConfig().getString("DateFormat")).format(Timestamp.valueOf(String.valueOf(newTS)));
            if (reason == null) {
                reason = plugin.getConfig().getString("Messages.NoReason");
            }
            if (perma) {
                player.kickPlayer(
                        plugin.formatPlayer(plugin.getConfig().getString("Messages.BanMessage")
                                .replace("[Duration]", "~")
                                .replace("[Until]", plugin.getConfig().getString("Messages.PermaBan"))
                                .replace("[Banner]", name)
                                .replace("[Reason]", reason), player)
                );
            } else {
                player.kickPlayer(
                        plugin.formatPlayer(plugin.getConfig().getString("Messages.BanMessage")
                                .replace("[Duration]", "~")
                                .replace("[Until]", banEnd)
                                .replace("[Banner]", name)
                                .replace("[Reason]", reason), player)
                );
            }
            plugin.sendTeamMSG(plugin.getConfig().getString(("Messages.TeamBanMessage")).replace("[Player]", player.getDisplayName()).replace("[Reason]", reason).replace("[Duration]", duration + unit).replace("[Banner]", bannedFrom.getDisplayName()), bannedFrom.getDisplayName());
        }
    }

    /* public List<String> getBanData(Player player){
        plugin.reloadConfig();
        if (plugin.getConfig().getBoolean("Stats.Bans." + player.getDisplayName() + ".Banned")){
            List<String> returnValues = new ArrayList<>();
            returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".Banned"));
            if (plugin.getConfig().isSet("Stats.Bans." + player.getDisplayName() + ".Reason")){
                returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".Reason"));
            }else {
                returnValues.add(plugin.getConfig().getString("Messages.NoReason"));
            }
            returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".From"));
            returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".Until"));
            returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".Duration"));
            returnValues.add(plugin.getConfig().getString("Stats.Bans." + player.getDisplayName() + ".BannedFrom"));

            return returnValues;
        }else {
            List<String> returnValues = new ArrayList<>();
            returnValues.add("false");
            return returnValues;
        }
    } // Outdated

    public boolean isBanned(Player player){
        plugin.reloadConfig();
        if (plugin.getConfig().getBoolean("Stats.Bans." + player.getDisplayName() + ".Banned")){
            return true;
        }else {
            return false;
        }
    } // Outdated */ // Outdated Code

    public boolean kickBannedPlayer(Player player) throws SQLException {
        plugin.reloadConfig();
        if (database.isBanned(player.getDisplayName())) {
            List<String> banData = database.getBanData(player.getDisplayName());
            String banEnd = new SimpleDateFormat(plugin.getConfig().getString("DateFormat")).format(Timestamp.valueOf(banData.get(3)));
            double until = Double.valueOf(banData.get(3).replace(" ", "").replace("-", "").replace(":", ""));
            double current = Double.valueOf(new Timestamp(System.currentTimeMillis()).toString().replace(" ", "").replace("-", "").replace(":", ""));
            double different;
            different = until - current;
            if (Objects.equals(banData.get(3), "2008-04-25 00:00:00")) {
                player.kickPlayer(
                        plugin.formatPlayer(plugin.getConfig().getString("Messages.BanMessage")
                                .replace("[Duration]", plugin.getConfig().getString("Messages.PermaBan"))
                                .replace("[Until]", plugin.getConfig().getString("Messages.PermaBan"))
                                .replace("[Reason]", banData.get(1))
                                .replace("[Banner]", banData.get(5)), player)
                );
                return true;
            } else if (different <= 0) {
                database.unban(player.getDisplayName());
                return false;
            } else {
                player.kickPlayer(
                        plugin.formatPlayer(plugin.getConfig().getString("Messages.BanMessage")
                                .replace("[Duration]", banData.get(4))
                                .replace("[Until]", banEnd)
                                .replace("[Reason]", banData.get(1))
                                .replace("[Banner]", banData.get(5)), player)
                );
                return true;

            }
        }
        return false;
    }

}

