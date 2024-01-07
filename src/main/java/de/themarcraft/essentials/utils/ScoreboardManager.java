package de.themarcraft.essentials.utils;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager extends BukkitRunnable {

    Main plugin;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard playerScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

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

            player.setScoreboard(playerScoreboard);
        }
    }
}
