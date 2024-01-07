package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Team implements CommandExecutor {

    Main plugin;

    public Team(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String name;
        if (commandSender == Bukkit.getConsoleSender()) {
            name = "Console";
        } else {
            name = commandSender.getName();
        }
        String result = String.join(" ", args);
        plugin.sendTeamMSG(result, name);
        return true;
    }
}
