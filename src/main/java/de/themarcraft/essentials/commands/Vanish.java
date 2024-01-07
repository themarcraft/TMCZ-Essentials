package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class Vanish implements CommandExecutor {

    Main plugin;
    Set vanishedPlayers;

    public Vanish(Main plugin, Set vanishedPlayers) {
        this.plugin = plugin;
        this.vanishedPlayers = vanishedPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == Bukkit.getConsoleSender()) {
            plugin.log(plugin.format(plugin.getConfig().getString("Messages.PlayerOnly")));
            return false;
        } else {
            Player player = (Player) sender;

            if (isVanished(player)) {
                unvanishPlayer(player);
                player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Vanish.Disabled"), player));
            } else {
                vanishPlayer(player);
                player.sendMessage(plugin.getPrefix() + plugin.formatPlayer(plugin.getConfig().getString("Messages.Vanish.Enabled"), player));
            }

            return true;

        }
    }


    public void vanishPlayer(Player player) {
        vanishedPlayers.add(player);
        player.setInvisible(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("themarcraft.vanish.see")) {
                onlinePlayer.hidePlayer(plugin, player);
            }
        }
        player.setPlayerListName(ChatColor.ITALIC + plugin.luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + ChatColor.ITALIC + " §8| §7" + ChatColor.ITALIC + player.getDisplayName());

    }

    public void unvanishPlayer(Player player) {
        vanishedPlayers.remove(player);
        player.setInvisible(false);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(plugin, player);
        }
        player.setPlayerListName(plugin.luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + " §8| §7" + player.getDisplayName());
    }

    public boolean isVanished(Player player) {
        if (vanishedPlayers.contains(player)) {
            return true;
        } else {
            return false;
        }
    }


}
