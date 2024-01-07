package de.themarcraft.essentials.utils;

import de.themarcraft.essentials.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetServer {

    public static void setServer(Player player, String server, Main plugin) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `messaging` SET player = ?, server = ?, msg = ?");
            statement.setString(1, player.getDisplayName());
            statement.setString(2, server);
            statement.setString(3, null);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {

        }
    }
}
