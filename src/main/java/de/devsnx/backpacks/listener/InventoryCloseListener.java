package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 13:47
 */

public class InventoryCloseListener implements Listener {

    private final BackpackManager backpackManager;

    public InventoryCloseListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        // Überprüfe, ob es sich um das Inventar eines Rucksacks handelt
        if (backpackManager.isBackpackInventory(player.getUniqueId(), inventory)) {
            // Erhalte die Rucksack-ID aus dem Inventartitel
            int backpackId = BackpackFileStorage.getBackpackIdByName(player.getUniqueId(), inventory.getTitle());
            // Aktualisiere den Rucksack im BackpackManager
            backpackManager.updateBackpack(player.getUniqueId(), backpackId, inventory);
            player.sendMessage("§aRucksack §b" + inventory.getTitle() + " §awurde gespeichert");
            return;
        } else if(inventory.getItem(40) != null && inventory.getItem(40).getType() == Material.PAPER && inventory.getItem(40).getItemMeta().getDisplayName().equalsIgnoreCase("§bRucksack Informationen")) {

            ItemStack item = inventory.getItem(40);

            Player targetOnline = Bukkit.getPlayer(item.getItemMeta().getLore().get(0).replace("OWNER:", ""));
            Integer backpackId = Integer.valueOf(item.getItemMeta().getLore().get(1).replace("ID:", ""));

            if (targetOnline != null) {
                backpackManager.updateBackpack(targetOnline.getUniqueId(), backpackId, inventory);
                player.sendMessage("§7Rucksack von §e" + targetOnline.getName() + " §7mit dem Namen §e" + inventory.getTitle() + " §7und der ID §e" + backpackId + " §7wurde gespeichert");
                return;
            } else {

                player.sendMessage("Spieler offline");
                return;
            }
        }

        return;

    }

}