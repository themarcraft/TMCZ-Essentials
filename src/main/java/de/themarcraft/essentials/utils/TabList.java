package de.themarcraft.essentials.utils;

import de.themarcraft.essentials.Main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.luckperms.api.model.group.Group;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TabList extends BukkitRunnable {

    private final Main plugin;
    private Scoreboard scoreboard;

    public TabList(Main plugin) {
        this.plugin = plugin;
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    }

    @Override
    public void run() {
        String header = plugin.format(plugin.getConfig().getString("Tablist.Header"));
        String footer = plugin.format(plugin.getConfig().getString("Tablist.Footer"));
        List<Group> sortedGroups = plugin.luckPerms.getGroupManager().getLoadedGroups().stream()
                .sorted(Comparator.comparingInt(group -> group.getWeight().orElse(0)))
                .collect(Collectors.toList());

        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }

        int order = 100;
        DecimalFormat decimalFormat = new DecimalFormat("000");
        for (Group group : sortedGroups) {
            String teamName = decimalFormat.format(order) + "_" + group.getName();
            Team team = scoreboard.registerNewTeam(teamName);
            order--;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = plugin.luckPerms.getUserManager().getUser(player.getUniqueId());
            order = 100;
            if (user != null) {
                QueryOptions queryOptions = plugin.luckPerms.getContextManager().getStaticQueryOptions();
                for (Group group2 : sortedGroups) {
                    for (Group group : user.getInheritedGroups(queryOptions)) {
                        Team team = scoreboard.getTeam(decimalFormat.format(order) + "_" + group.getName());
                        if (team != null) {
                            team.addEntry(player.getName());
                        }
                    }
                    order--;
                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = plugin.luckPerms.getUserManager().getUser(player.getUniqueId());
            order = 100;
            Scoreboard playerScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            if (user != null) {
                QueryOptions queryOptions = plugin.luckPerms.getContextManager().getStaticQueryOptions();
                for (Group group2 : sortedGroups) {
                    for (Group group : user.getInheritedGroups(queryOptions)) {
                        Team team = scoreboard.getTeam(decimalFormat.format(order) + "_" + group.getName());
                        if (team != null) {
                            team.addEntry(player.getName());

                            if (playerScoreboard.getTeam(decimalFormat.format(order) + "_" + player.getDisplayName()) != null) {
                                playerScoreboard.getTeam(decimalFormat.format(order) + "_" + player.getDisplayName()).unregister();
                            }
                            Team playerTeam = playerScoreboard.registerNewTeam(decimalFormat.format(order) + "_" + player.getDisplayName());
                            playerTeam.setDisplayName(decimalFormat.format(order) + "_" + player.getDisplayName());
                            playerTeam.setPrefix(decimalFormat.format(order) + "_" + player.getDisplayName());

                            if (playerScoreboard.getObjective(player.getDisplayName()) != null) {
                                playerScoreboard.getObjective(player.getDisplayName()).unregister();
                            }

                            Objective objective = playerScoreboard.registerNewObjective(player.getDisplayName(), "dummy");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                            objective.setDisplayName("§a§lthemarcraft.de");
                            objective.getScore("§a").setScore(10);
                            objective.getScore("§7» §a§lServer").setScore(9);
                            objective.getScore(plugin.getConfig().getString("Server.Name")).setScore(8);
                            objective.getScore("§b").setScore(7);
                            objective.getScore("§7» §a§lSpieler").setScore(6);
                            objective.getScore(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()).setScore(5);
                            objective.getScore("§c").setScore(4);
                            objective.getScore("§7» §a§lKonto").setScore(3);
                            objective.getScore(plugin.vaultAPI.format(plugin.vaultAPI.getBalance(player))).setScore(2);
                            objective.getScore("§d").setScore(1);
                        }
                    }
                    order--;
                }
            }
            if (plugin.vanish.isVanished(player)) {
                player.setPlayerListName(ChatColor.ITALIC + plugin.luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + ChatColor.ITALIC + " §8| §7" + ChatColor.ITALIC + player.getDisplayName());
            } else {
                player.setPlayerListName(plugin.luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + " §8| §7" + player.getDisplayName());
            }
            player.setPlayerListHeaderFooter(header, footer);
            player.setScoreboard(scoreboard);
            //player.setScoreboard(playerScoreboard);
        }
    }

}