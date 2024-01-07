package de.themarcraft.essentials.commands.economy;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Balance implements CommandExecutor, TabCompleter {

    Main plugin;

    public Balance(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (Bukkit.getConsoleSender() == commandSender) {
            plugin.playerOnly();
            return false;
        }
        Player player = (Player) commandSender;
        if (args.length == 1 && player.hasPermission("themarcraft.economy.balance.other")) {
            try {
                Player other = Bukkit.getPlayer(args[0]);
                player.sendMessage(plugin.returnCommandString("Messages.Balance.Other").replace("[Player]", other.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(plugin.vaultAPI.getBalance(other))));

                return true;
            } catch (Exception e) {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")).replace("[Player]", args[0]));
                return false;
            }
        } else {
            player.sendMessage(plugin.returnCommandString("Messages.Balance.You").replace("[Amount]", plugin.vaultAPI.format(plugin.vaultAPI.getBalance(player))));
            plugin.vaultAPI.getBalance(player);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List list = new ArrayList<>();
        if (args.length == 1 && commandSender.hasPermission("themarcraft.economy.balance.other")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getDisplayName().contains(args[0])) {
                    list.add(p.getDisplayName());
                }
            }
        }
        return list;
    }
}
