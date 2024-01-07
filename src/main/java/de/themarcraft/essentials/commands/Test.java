package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test implements CommandExecutor {

    private final Main plugin;

    public Test(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.log("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        sender.sendMessage(String.valueOf(plugin.vaultAPI.getBalance(((Player) sender).getDisplayName())));
        sender.sendMessage(String.valueOf(plugin.vaultAPI.hasAccount((Player) sender)));
        sender.sendMessage(String.valueOf(plugin.vaultAPI.hasAccount("TEST")));
        sender.sendMessage(String.valueOf(plugin.vaultAPI.has((Player) sender, 2000)));
        sender.sendMessage(String.valueOf(plugin.vaultAPI.has((Player) sender, 10000000)));
        EconomyResponse response = plugin.vaultAPI.withdrawPlayer("_TheMarCraft_", 50);
        sender.sendMessage("Anzahl: " + response.amount + " Kontostand: " + response.balance + " Typ: " + response.type.toString());
        sender.sendMessage(String.valueOf(plugin.vaultAPI.createPlayerAccount("TESTING")));
        return true;
    }

}
