package de.themarcraft.essentials.apis;

import de.themarcraft.essentials.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class VaultAPI implements Economy {
    private Economy economy;
    private Main plugin;

    public VaultAPI(Main plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        plugin.log("Loading Vault Plugin...");
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.log(ChatColor.RED + "ERROR! Vault plugin not found.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            // Use 'this' to refer to the current instance of your JavaPlugin class
            getServer().getServicesManager().register(Economy.class, this, plugin, ServicePriority.High);
            rsp = getServer().getServicesManager().getRegistration(Economy.class);
        }

        // Set the 'economy' field to the provider from the registration
        economy = rsp.getProvider();
        if (economy != null) {
            plugin.log(ChatColor.GREEN + "VaultAPI Loaded");
            plugin.log(ChatColor.GOLD + "VaultAPI: " + economy.getName());
            plugin.log(ChatColor.GOLD + "Provider: " + economy);
            return true;
        } else {
            plugin.log(ChatColor.RED + "ERROR! Economy provider not found.");
            return false;
        }
    }

    // Implement the rest of the methods in the Economy interface as needed

    // Example: implement isEnabled
    public boolean isEnabled() {
        return economy != null;
    }

    @Override
    public String getName() {
        return "TMCZ-Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00");
        return decimalFormat.format(v) + currencyNameSingular();
    }

    @Override
    public String currencyNamePlural() {
        return "TMCZ-Dollar";
    }

    @Override
    public String currencyNameSingular() {
        return "$";
    }

    @Override
    public boolean hasAccount(String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                statement.close();
                return false;
            } else {
                statement.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                statement.close();
                return false;
            } else {
                statement.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                statement.close();
                return false;
            } else {
                statement.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                statement.close();
                return false;
            } else {
                statement.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public double getBalance(String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
            } else {
                result = 0d;
            }


            statement.close();

            return result;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
            } else {
                result = 0d;
            }


            statement.close();

            return result;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public double getBalance(String s, String s1) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
            } else {
                result = 0d;
            }


            statement.close();

            return result;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
            } else {
                result = 0d;
            }


            statement.close();

            return result;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public boolean has(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
                if (result >= v) {
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
                if (result >= v) {
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean has(String s, String s1, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, s);

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
                if (result >= v) {
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("SELECT * FROM economy WHERE name = ?");
            statement.setString(1, offlinePlayer.getName());

            ResultSet resultSet = statement.executeQuery();

            Double result;

            if (resultSet.next()) {
                result = resultSet.getDouble("cash");
                if (result >= v) {
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public EconomyResponse setPlayerBalance(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            statement.setDouble(1, v);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, v, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) - v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(offlinePlayer.getName()) - v;

            statement.setDouble(1, withdraw);
            statement.setString(2, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) - v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(offlinePlayer.getName()) - v;

            statement.setDouble(1, withdraw);
            statement.setString(2, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) + v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(offlinePlayer.getName()) + v;

            statement.setDouble(1, withdraw);
            statement.setString(2, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) + v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `cash` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(offlinePlayer.getName()) + v;

            statement.setDouble(1, withdraw);
            statement.setString(2, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `bank` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) + v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("UPDATE `economy` SET `bank` = ? WHERE `economy`.`name` = ?;");

            Double withdraw = getBalance(s) - v;

            statement.setDouble(1, withdraw);
            statement.setString(2, s);

            statement.executeUpdate();

            statement.close();

            return new EconomyResponse(v, withdraw, EconomyResponse.ResponseType.SUCCESS, "");

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `economy` SET name = ?");
            statement.setString(1, s);

            statement.executeUpdate();

            statement.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `economy` SET name = ?");
            statement.setString(1, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `economy` SET name = ?");
            statement.setString(1, s);

            statement.executeUpdate();

            statement.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        try {
            PreparedStatement statement = plugin.database.getConnection().prepareStatement("INSERT `economy` SET name = ?");
            statement.setString(1, offlinePlayer.getName());

            statement.executeUpdate();

            statement.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}
