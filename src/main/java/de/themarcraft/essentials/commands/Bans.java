package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class Bans implements CommandExecutor {

    Main plugin;

    public Bans(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            StringBuilder table = new StringBuilder();
            table.append("§7§n" + padRight("Benutzername", 15)).append("§r §6| §r").append("§c§n" + padRight("Grund", 20)).append("§r §6| §r").append("§e§n" + padRight("Gebannt bis", 15)).append("§r\n");
            for (String name : plugin.database.getBanns()) {
                table.append("§7" + padRight(name, 15)).append("§r §6| §r").append("§c" + padRight(plugin.database.getBanData(name).get(1), 20)).append("§r §6| §r").append("§e" + padRight(plugin.database.getBanData(name).get(3).replace("2008-04-25 00:00:00", plugin.format(plugin.getConfig().getString("Messages.PermaBan"))), 15)).append("\n");
            }
            commandSender.sendMessage(table.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private String padRight(String s, int width) {
        return String.format("%-" + width + "s", s);
    }
}
