package de.themarcraft.essentials;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.themarcraft.essentials.apis.VaultAPI;
import de.themarcraft.essentials.commands.*;
import de.themarcraft.essentials.commands.economy.Balance;
import de.themarcraft.essentials.commands.economy.Eco;
import de.themarcraft.essentials.commands.economy.Pay;
import de.themarcraft.essentials.db.Database;
import de.themarcraft.essentials.listeners.ChatListener;
import de.themarcraft.essentials.listeners.PlayerListener;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import de.themarcraft.essentials.utils.TabList;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin implements PluginMessageListener {

    public static Permission perms = null;
    public Set<Player> vanishedPlayers;
    public String version = "1.0-Beta";
    public Connection connection;
    public Vanish vanish;
    public Database database;
    public Economy economy = null;
    public VaultAPI vaultAPI;
    public LuckPerms luckPerms;

    private static String getMinecraftVersion(Server server) {
        String version = server.getVersion();
        int start = version.indexOf("MC: ") + 4;
        int end = version.length() - 1;
        return version.substring(start, end);
    }

    private static String getMinecraftVersionRound(Server server) {
        String version = server.getVersion();
        int start = version.indexOf("MC: ") + 4;
        int end = version.length() - 3;
        return version.substring(start, end);
    }

    @Override
    public void onEnable() {
        vanishedPlayers = new HashSet<>();
        this.database = new Database(this);
        vaultAPI = new VaultAPI(this);

        Bukkit.getConsoleSender().sendMessage("\n" +
                ChatColor.GREEN + " _____  __  __   ___  ____   " + ChatColor.BLUE + " __   __   _         __ " + ChatColor.RESET + "\n" +
                ChatColor.GREEN + "|_   _||  \\/  | / __||_  /  " + ChatColor.BLUE + "  \\ \\ / /  / |       /  \\" + ChatColor.RESET + "\n" +
                ChatColor.GREEN + "  | |  | |\\/| || (__  / /   " + ChatColor.BLUE + "   \\   /   | |   _  | () |" + ChatColor.RESET + "\n" +
                ChatColor.GREEN + "  |_|  |_|  |_| \\___|/___|  " + ChatColor.BLUE + "    \\_/    |_|  (_)  \\__/" + ChatColor.RESET + "\n");

        this.log("");
        this.log("Plugin &b" + this.getName() + " " + getVersion() + "&r loaded");
        this.log("");

        Ban ban = new Ban(this, database);
        this.reloadConfig();
        this.saveDefaultConfig();

        PlayerListener playerListener = new PlayerListener(this, ban);
        vanish = new Vanish(this, vanishedPlayers);
        this.getServer().getPluginManager().registerEvents(playerListener, this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerSelector(this), this);

        getCommand("kick").setExecutor(new Kick(this));
        getCommand("ban").setExecutor(ban);
        getCommand("bans").setExecutor(new Bans(this));
        getCommand("team").setExecutor(new Team(this));
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("gm").setExecutor(new Gamemode(this));
        getCommand("heal").setExecutor(new Heal(this));
        getCommand("test").setExecutor(new Test(this));
        getCommand("feed").setExecutor(new Feed(this));
        getCommand("vanish").setExecutor(vanish);
        getCommand("speed").setExecutor(new Speed(this));
        getCommand("unban").setExecutor(new Unban(this));
        getCommand("economy").setExecutor(new Eco(this));
        getCommand("pay").setExecutor(new Pay(this));
        getCommand("balance").setExecutor(new Balance(this));
        getCommand("serverselector").setExecutor(new ServerSelector(this));

        try {
            this.database.initializeDatabase();
        } catch (SQLException e) {
            log("Error While Initialising the Database");
            log("Please Confirm Your MySQL Login Data and the MySQL Server");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        if (!vaultAPI.setupEconomy()) {
            log("Vault Economy wurde nicht gefunden! Deaktiviere das Plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        log("Loading LuckPerms...");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            log(ChatColor.GREEN + "LuckPerms Loaded");
            log(ChatColor.BLUE + "LuckPerms: " + Bukkit.getPluginManager().getPlugin("LuckPerms"));
            log(ChatColor.BLUE + "Provider: " + luckPerms);
        } else {
            log(ChatColor.RED + "Error while Loading LuckPerms");
            getServer().getPluginManager().disablePlugin(this);
        }

        new TabList(this).runTaskTimer(this, 0L, 200L);
        //new ScoreboardManager(this).runTaskTimer(this, 0L, 200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            vanish.unvanishPlayer(player);
            player.setFlySpeed(0.2f);
            player.setWalkSpeed(0.2f);
        }
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        this.log("");
        this.log("Plugin &b" + this.getName() + " " + getVersion() + "&r disabled");
        this.log("");
    }

    public String getPrefix() {
        return format(this.getConfig().getString("Prefix") + ChatColor.RESET);
    }

    public String formatPlayer(String p, @Nullable Player name) {
        if (name != null) {
            p = p.replace("[Player]", name.getDisplayName());
            if (this.getConfig().isSet("Stats.Bans." + name.getDisplayName() + ".BannedFrom") && this.getConfig().getBoolean("Stats.Bans." + name.getDisplayName() + ".Banned")) {
                p = p.replace("[Banner]", this.getConfig().getString("Stats.Bans." + name.getDisplayName() + ".BannedFrom"));
            }
            if (this.getConfig().isSet("Stats.Deaths." + name.getUniqueId().toString())) {
                p = p.replace("[Deaths]", String.valueOf(this.getConfig().getInt("Stats.Deaths." + name.getUniqueId().toString())));
            } else {
                p = p.replace("[Deaths]", "0");
            }
            if (name.getKiller() != null) {
                p = p.replace("[Killer]", name.getKiller().getDisplayName());
            }
        }
        p = ChatColor.translateAlternateColorCodes('&', p);
        p = p.replace("[NewLine]", "\n");
        p = p.replace("[MaxPlayers]", String.valueOf(Bukkit.getMaxPlayers()));
        p = p.replace("[OnlinePlayers]", String.valueOf(Bukkit.getOnlinePlayers().size()));
        p = p.replace("[Servername]", Bukkit.getServer().getName());
        p = p.replace("[Version]", getMinecraftVersion(Bukkit.getServer()));
        p = p.replace("[VersionRound]", getMinecraftVersionRound(Bukkit.getServer()));
        return p;
    }

    public String format(String p) {
        p = ChatColor.translateAlternateColorCodes('&', p);
        p = p.replace("[NewLine]", "\n");
        p = p.replace("[MaxPlayers]", String.valueOf(Bukkit.getMaxPlayers()));
        p = p.replace("[OnlinePlayers]", String.valueOf(Bukkit.getOnlinePlayers().size()));
        p = p.replace("[Servername]", Bukkit.getServer().getName());
        p = p.replace("[Version]", getMinecraftVersion(Bukkit.getServer()));
        p = p.replace("[VersionRound]", getMinecraftVersionRound(Bukkit.getServer()));
        return p + ChatColor.RESET;
    }

    public void noPermission(Player player) {
        player.sendMessage(this.getPrefix() + this.format(this.getConfig().getString("Messages.NoPermission")));
    }

    public void playerOnly() {
        log(format(getConfig().getString("Messages.PlayerOnly")));
    }

    public void log(String msg) {
        String message = ChatColor.translateAlternateColorCodes('&', (this.getPrefix() + msg).replace("»", ">"));
        message = message.replace("ü", "ue").replace("ä", "ae").replace("ö", "oe");
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public String getVersion() {
        return "&e" + this.getDescription().getVersion() + "&r";
    }

    public void sendTeamMSG(String string, String player) {
        String result = format(this.getConfig().getString("Messages.TeamPrefix") + string).replace("[Player]", player);
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("themarcraft.team")) {
                players.sendMessage(format(result));
            }
        }
        log(format(result));
    }

    public String returnCommandString(String string) {
        return getPrefix() + format(getConfig().getString(string));
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }

    }
}
