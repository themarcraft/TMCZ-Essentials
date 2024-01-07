package de.themarcraft.essentials.commands.economy;

import de.themarcraft.essentials.Main;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pay implements CommandExecutor {

    Main plugin;

    public Pay(Main plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.playerOnly();
            return false;
        }
        Player player = (Player) commandSender;
        if (args.length == 0) {
            player.sendMessage(plugin.returnCommandString("Messages.ValidPlayer"));
            return false;
        } else if (args.length == 1) {
            player.sendMessage(plugin.returnCommandString("Messages.InvalidNumber"));
            return false;
        } else {
            plugin.log(args[0]);
            if (args[0].equals("*")) {
                plugin.log("Ja");
                try {
                    Double cash = Double.valueOf(args[1]) * (Bukkit.getOnlinePlayers().size() - 1);
                    if (plugin.vaultAPI.has(player, cash)) {
                        EconomyResponse playerMoney = plugin.vaultAPI.withdrawPlayer(player, cash);
                        player.sendMessage(plugin.returnCommandString("Messages.Economy.Pay.*").replace("[Player]", "*").replace("[Amount]", plugin.vaultAPI.format(Double.valueOf(args[1]))).replace("[Balance]", plugin.vaultAPI.format(playerMoney.balance)));
                        for (Player reciver : Bukkit.getOnlinePlayers()) {
                            if (reciver != player) {
                                EconomyResponse reciverMoney = plugin.vaultAPI.depositPlayer(reciver, Double.valueOf(args[1]));
                                reciver.sendMessage(plugin.returnCommandString("Messages.Economy.Pay.Other").replace("[Player]", player.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(reciverMoney.amount)).replace("[Balance]", plugin.vaultAPI.format(reciverMoney.balance)));
                            }
                        }
                    }
                } catch (Exception exception) {
                    plugin.log(exception.getMessage());
                    player.sendMessage(plugin.returnCommandString("Messages.InvalidNumber"));
                }
            } else {
                try {
                    Player reciver = Bukkit.getPlayer(args[0]);
                    try {
                        Double cash = Double.valueOf(args[1]);
                        if (plugin.vaultAPI.has(player, cash)) {
                            EconomyResponse playerMoney = plugin.vaultAPI.withdrawPlayer(player, cash);
                            EconomyResponse reciverMoney = plugin.vaultAPI.depositPlayer(reciver, cash);
                            player.sendMessage(plugin.returnCommandString("Messages.Economy.Pay.You").replace("[Player]", reciver.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(playerMoney.amount)).replace("[Balance]", plugin.vaultAPI.format(playerMoney.balance)));
                            reciver.sendMessage(plugin.returnCommandString("Messages.Economy.Pay.Other").replace("[Player]", player.getDisplayName()).replace("[Amount]", plugin.vaultAPI.format(reciverMoney.amount)).replace("[Balance]", plugin.vaultAPI.format(reciverMoney.balance)));
                        } else {
                            player.sendMessage(plugin.returnCommandString("Messages.Economy.NotEnoughMoney"));
                        }
                    } catch (Exception e) {
                        player.sendMessage(plugin.returnCommandString("Messages.InvalidNumber"));
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
}
