package de.themarcraft.essentials.listeners;

import de.themarcraft.essentials.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if ((message.startsWith("@team ") || message.startsWith("@TEAM ")) && player.hasPermission("themarcraft.team")) {
            plugin.sendTeamMSG(message.substring(6, message.length()), player.getDisplayName());
            event.setCancelled(true);
        } else {
            String finalMessage = plugin.getConfig().getString("ChatFormat").replace("[Player]", player.getDisplayName()).replace("[Prefix]", plugin.luckPerms.getUserManager().getUser(player.getDisplayName()).getCachedData().getMetaData().getPrefix()).replace("[Message]", plugin.format(message));
            event.setFormat(plugin.format(finalMessage));
        }
    }
}
