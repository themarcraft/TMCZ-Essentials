package de.themarcraft.essentials.listeners;

import de.themarcraft.essentials.Main;
import de.themarcraft.essentials.commands.Ban;
import de.themarcraft.essentials.utils.TabList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.List;

public class PlayerListener implements Listener {
    Main plugin;
    Ban ban;

    public PlayerListener(Main pPlugin, Ban pBan) {
        ban = pBan;
        plugin = pPlugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = (Player) event.getPlayer();
        if (plugin.getConfig().getBoolean("Enabled.JoinMessage")) {
            List<String> joinMsg = plugin.getConfig().getStringList("Messages.Join");
            int rand = (int) (Math.random() * joinMsg.size());
            String joinMsgFinal = plugin.formatPlayer(joinMsg.get(rand), player);
            event.setJoinMessage(joinMsgFinal);
        }
        if (!ban.kickBannedPlayer(player)) {

            if (!plugin.vaultAPI.hasAccount(player)) {
                plugin.vaultAPI.createPlayerAccount(player);
            }

            //player.setPlayerListName(plugin.permissions.getPrefix(player) + " §8| §7" + player.getDisplayName());
            //plugin.log(plugin.luckPerms.getUserManager().getUser(player.getDisplayName()).getCachedData().getMetaData().getPrefix() + " §8| §7" + player.getDisplayName());
            plugin.vanish.unvanishPlayer(player);

            new TabList(plugin).run();

            player.setFlySpeed(0.2f);
            player.setWalkSpeed(0.2f);
            player.setInvulnerable(false);
            player.setInvisible(false);
        }


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("Enabled.QuitMessage")) {
            Player player = (Player) event.getPlayer();
            List<String> quitMsg = plugin.getConfig().getStringList("Messages.Quit");
            int rand = (int) (Math.random() * quitMsg.size());
            String quitMsgFinal = plugin.formatPlayer(quitMsg.get(rand), player);
            event.setQuitMessage(quitMsgFinal);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfig().getBoolean("Enabled.DeathMessage")) {
            List<String> deathMsg;
            Player player = (Player) event.getEntity();
            String deathMsgFinal;
            if (player.getKiller() == null) {
                deathMsg = plugin.getConfig().getStringList("Messages.Death");
                int rand = (int) (Math.random() * deathMsg.size());
                deathMsgFinal = plugin.formatPlayer(deathMsg.get(rand), player);
            } else if (player.getKiller() != null) {
                deathMsg = plugin.getConfig().getStringList("Messages.DeathByPlayer");
                int rand = (int) (Math.random() * deathMsg.size());
                deathMsgFinal = plugin.formatPlayer(deathMsg.get(rand), player);
            } else {
                deathMsgFinal = "";
            }
            event.setDeathMessage(deathMsgFinal);
            int death;
            if (!plugin.getConfig().isSet("Stats.Deaths." + player.getDisplayName())) {
                death = 0;
            } else {
                death = plugin.getConfig().getInt("Stats.Deaths." + player.getDisplayName());
            }
            plugin.getConfig().set("Stats.Deaths." + player.getDisplayName(), death + 1);
            plugin.saveConfig();
        }
    }
}
