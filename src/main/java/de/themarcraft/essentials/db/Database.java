package de.themarcraft.essentials.db;

import de.themarcraft.essentials.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    Main plugin;
    private Connection connection;

    public Database(Main plugin) {
        this.plugin = plugin;
    }


    public Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }

        //Try to connect to my MySQL database running locally
        String host = plugin.getConfig().getString("SQL.Host");
        String database = plugin.getConfig().getString("SQL.Database");
        String user = plugin.getConfig().getString("SQL.Username");
        String passwd = plugin.getConfig().getString("SQL.Password");

        plugin.log("Connecting to Database...");
        plugin.log(ChatColor.YELLOW + "Host: " + ChatColor.AQUA + host);
        plugin.log(ChatColor.YELLOW + "Database: " + ChatColor.AQUA + database);
        plugin.log(ChatColor.YELLOW + "User: " + ChatColor.AQUA + user);
        plugin.log(ChatColor.YELLOW + "Password: " + ChatColor.AQUA + "************");

        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, passwd);

        this.connection = connection;

        plugin.log(ChatColor.GREEN + "Connected to database");

        return connection;
    }

    public void initializeDatabase() throws SQLException {

        Statement statement = getConnection().createStatement();

        //Create the player_stats table
        String createBanTable = "CREATE TABLE IF NOT EXISTS `bans` (`name` VARCHAR(255) NOT NULL , `banned` BOOLEAN NOT NULL , `timeFrom` TIMESTAMP NULL , `timeUntil` TIMESTAMP NULL , `bannedFrom` VARCHAR(255) NOT NULL , `reason` VARCHAR(255) NULL , `duration` INT NOT NULL ) ENGINE = InnoDB;";
        String createEconomyTable = "CREATE TABLE IF NOT EXISTS `economy` ( `name` VARCHAR(255) NOT NULL, `cash` DOUBLE NOT NULL DEFAULT 1000, `bank` DOUBLE NOT NULL DEFAULT 0, UNIQUE (`name`) ) ENGINE=InnoDB;";
        String createSettings = "CREATE TABLE IF NOT EXISTS `tmczSettings` ( `set` BOOLEAN, `prefix` VARCHAR(255) NOT NULL, `playeronly` VARCHAR(255) NOT NULL, `nopermission` VARCHAR(255) NOT NULL, `validplayer` VARCHAR(255) NOT NULL ) ENGINE=InnoDB;";

        statement.execute(createBanTable);
        statement.execute(createEconomyTable);
        statement.execute(createSettings);

        statement.close();

        insertConfig();

    }

    public void ban(Player player, String bannedFrom, @Nullable String reason, Timestamp from, Timestamp until, int duration) throws SQLException {
        PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `bans` SET name = ?, reason = ?, timeFrom = ?, timeUntil = ?, bannedFrom = ?, duration = ?, banned = ?");
        statement.setString(1, player.getDisplayName());
        if (reason != null) {
            statement.setString(2, reason);
        } else {
            statement.setString(2, plugin.getConfig().getString("Messages.NoReason"));
        }
        statement.setTimestamp(3, from);
        statement.setTimestamp(4, until);
        statement.setString(5, bannedFrom);
        statement.setInt(6, duration);
        statement.setBoolean(7, true);

        statement.executeUpdate();

        statement.close();
    }

    public boolean isBanned(String player) throws SQLException {
        PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM bans WHERE name = ?");
        statement.setString(1, player);

        ResultSet resultSet = statement.executeQuery();

        boolean result;

        if (resultSet.next()) {
            result = resultSet.getBoolean("banned");
        } else {
            result = false;
        }


        statement.close();

        return result;
    }

    public List<String> getBanData(String player) throws SQLException {
        PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM bans WHERE name = ?");
        statement.setString(1, player);

        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        if (isBanned(player)) {
            List<String> returnValues = new ArrayList<>();
            returnValues.add(String.valueOf(resultSet.getBoolean("banned")));
            if (resultSet.getString("reason") != null) {
                returnValues.add(resultSet.getString("reason"));
            } else {
                returnValues.add(plugin.getConfig().getString("Messages.NoReason"));
            }
            returnValues.add(resultSet.getString("timeFrom"));
            returnValues.add(resultSet.getString("timeUntil"));
            returnValues.add(resultSet.getString("duration"));
            returnValues.add(resultSet.getString("bannedFrom"));

            statement.close();

            return returnValues;
        } else {
            List<String> returnValues = new ArrayList<>();
            returnValues.add("false");

            statement.close();
            return returnValues;
        }
    }

    public void unban(String player) throws SQLException {
        PreparedStatement statement = plugin.database.getConnection().prepareStatement("DELETE FROM bans WHERE name = ?");
        statement.setString(1, player);

        statement.executeUpdate();

        statement.close();
    }

    public List<String> getBanns() throws SQLException {
        List<String> returnNames = new ArrayList<>();

        PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM bans");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("name");

            returnNames.add(name);
        }

        statement.close();

        return returnNames;
    }

    public void insertConfig() throws SQLException {
        PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM tmczSettings");

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            if (!resultSet.getBoolean("set")) {
                PreparedStatement insert = plugin.database.getConnection().prepareStatement("INSERT INTO `tmczSettings` (`set`, `prefix`, `playeronly`, `nopermission`, `validplayer`) VALUES (?, ?, ?, ?, ?);");
                insert.setBoolean(1, true);
                insert.setString(2, plugin.getPrefix());
                insert.setString(3, plugin.getConfig().getString("Messages.PlayerOnly"));
                insert.setString(4, plugin.getConfig().getString("Messages.NoPermission"));
                insert.setString(5, plugin.getConfig().getString("Messages.ValidPlayer"));

                // Use executeUpdate for INSERT statements
                insert.executeUpdate();

                insert.close();
            } else {
                PreparedStatement delete = plugin.database.getConnection().prepareStatement("DELETE FROM `tmczSettings`");
                // Use executeUpdate for DELETE statements
                delete.executeUpdate();
                delete.close();

                PreparedStatement insert = plugin.database.getConnection().prepareStatement("INSERT INTO `tmczSettings` (`set`, `prefix`, `playeronly`, `nopermission`, `validplayer`) VALUES (?, ?, ?, ?, ?);");
                insert.setBoolean(1, true);
                insert.setString(2, plugin.getPrefix());
                insert.setString(3, plugin.getConfig().getString("Messages.PlayerOnly"));
                insert.setString(4, plugin.getConfig().getString("Messages.NoPermission"));
                insert.setString(5, plugin.getConfig().getString("Messages.ValidPlayer"));

                // Use executeUpdate for INSERT statements
                insert.executeUpdate();

                insert.close();
            }
        }

        statement.close();
    }

}
