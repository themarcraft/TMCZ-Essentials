package de.themarcraft.essentials.commands;

import de.themarcraft.essentials.Main;
import de.themarcraft.essentials.utils.SetServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements CommandExecutor, Listener {

    Main plugin;

    public ServerSelector(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender == Bukkit.getConsoleSender()) {
            plugin.log(plugin.format(plugin.getConfig().getString("Messages.PlayerOnly")));
            return false;
        }
        Player player = (Player) commandSender;
        Inventory serverSelector = createServerSelector();
        player.openInventory(serverSelector);
        return false;
    }

    public Inventory createServerSelector() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Wähle einen Server");

        ItemStack air = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        ItemStack lobby = new ItemStack(Material.COMPASS);
        ItemMeta lobbyMeta = lobby.getItemMeta();
        List lobbyLore = new ArrayList<>();
        lobbyLore.add(ChatColor.YELLOW + "Gehe zur Lobby");
        lobbyMeta.setDisplayName(ChatColor.YELLOW + "Lobby");
        lobbyMeta.setLore(lobbyLore);
        lobby.setItemMeta(lobbyMeta);

        ItemStack cb = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta cbMeta = cb.getItemMeta();
        List cbLore = new ArrayList<>();
        cbLore.add(ChatColor.YELLOW + "Betrete den CityBuild Server");
        cbLore.add(ChatColor.YELLOW + "Und erstelle ein eigenes Grundstück");
        cbMeta.setDisplayName(ChatColor.YELLOW + "CityBuild");
        cbMeta.setLore(cbLore);
        cb.setItemMeta(cbMeta);

        ItemStack farm = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta farmMeta = farm.getItemMeta();
        List farmLore = new ArrayList<>();
        farmLore.add(ChatColor.YELLOW + "Farme für dein eigenes CityBuild Grundstück");
        farmMeta.setDisplayName(ChatColor.YELLOW + "Farmserver");
        farmMeta.setLore(farmLore);
        farm.setItemMeta(farmMeta);

        inventory.setItem(20, lobby);
        inventory.setItem(22, cb);
        inventory.setItem(24, farm);

        return inventory;
    }

    @EventHandler
    public void onUiInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Wähle einen Server")) {
            if (event.getCurrentItem() != null) {
                switch (event.getCurrentItem().getType()) {
                    case COMPASS:
                        SetServer.setServer(player, "lobby", plugin);
                        break;
                    case GRASS_BLOCK:
                        SetServer.setServer(player, "cb01", plugin);
                        break;
                    case DIAMOND_PICKAXE:
                        player.sendMessage(plugin.getPrefix() + "Dieser Server entsteht bald");
                        break;
                }
            }
            event.setCancelled(true);
        }
    }
}
