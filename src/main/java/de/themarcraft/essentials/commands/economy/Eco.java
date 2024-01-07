package de.themarcraft.essentials.commands.economy;

import de.themarcraft.essentials.Main;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Eco implements CommandExecutor, TabCompleter {

    Main plugin;

    public Eco(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.playerOnly();
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 1) {
            player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Syntax")));
            return false;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                plugin.vaultAPI.depositPlayer(player, Double.parseDouble(args[1]));
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Give.Success")).replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
            } else if (args[0].equalsIgnoreCase("take")) {
                plugin.vaultAPI.withdrawPlayer(player, Double.parseDouble(args[1]));
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Take.Success")).replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
            } else if (args[0].equalsIgnoreCase("set")) {
                plugin.vaultAPI.setPlayerBalance(player.getDisplayName(), Double.parseDouble(args[1]));
                player.sendMessage(plugin.returnCommandString("Messages.Economy.Set.Success").replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
            } else {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Syntax")));
            }
        } else if (args[1].equals("*")) {
            try {
                for (Player reciver : Bukkit.getOnlinePlayers()) {
                    if (reciver != player) {
                        EconomyResponse reciverMoney = plugin.vaultAPI.setPlayerBalance(reciver.getDisplayName(), Double.valueOf(args[2]));
                    }
                }
                player.sendMessage(plugin.returnCommandString("Messages.Economy.Set.Other").replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[2]))).replace("[Player]", "*"));
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Syntax")));
            }
        } else if (args.length == 3 && player.hasPermission("themarcraft.economy.eco.other")) {
            try {
                Player reciver = Bukkit.getPlayer(args[1]);
                if (args[0].equalsIgnoreCase("give")) {
                    plugin.vaultAPI.depositPlayer(reciver, Double.parseDouble(args[2]));
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Give.Other")).replace("[Player]", reciver.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
                } else if (args[0].equalsIgnoreCase("take")) {
                    plugin.vaultAPI.withdrawPlayer(reciver, Double.parseDouble(args[2]));
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Take.Other")).replace("[Player]", reciver.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
                } else if (args[0].equalsIgnoreCase("set")) {
                    plugin.vaultAPI.setPlayerBalance(reciver.getDisplayName(), Double.parseDouble(args[2]));
                    player.sendMessage(plugin.returnCommandString("Messages.Economy.Set.Other").replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[2]))).replace("[Player]", reciver.getDisplayName()));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.Economy.Syntax")));
                }
            } catch (Exception e) {
                player.sendMessage(plugin.getPrefix() + plugin.format(plugin.getConfig().getString("Messages.ValidPlayer")).replace("[Player]", args[1]).replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))));
            }
        } else if (args.length == 3) {
            plugin.noPermission(player);
        } else {
            player.sendMessage(plugin.returnCommandString("Messages.Economy.Syntax"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List list = new ArrayList<>();
        if (args.length == 1) {
            list.add("give");
            list.add("take");
            list.add("set");
            return list;
        } else if (args.length == 2 && commandSender.hasPermission("themarcraft.economy.eco.other")) {
            try {
                Double d = Double.valueOf(args[1]) + 2d;
                return list;
            } catch (Exception n) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getDisplayName().contains(args[1])) {
                        list.add(p.getDisplayName());
                    }
                }
                return list;
            }
        } else {
            return list;
        }
    }
}
