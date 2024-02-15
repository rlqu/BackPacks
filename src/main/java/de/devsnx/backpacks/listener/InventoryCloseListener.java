package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

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
        if (backpackManager.isBackpackInventory(player, inventory)) {
            // Erhalte die Rucksack-ID aus dem Inventartitel
            int backpackId = BackpackFileStorage.getBackpackIdByName(player.getUniqueId(), inventory.getTitle());
            // Aktualisiere den Rucksack im BackpackManager
            backpackManager.updateBackpack(player, backpackId, inventory);
            player.sendMessage("§aRucksack §b" + inventory.getTitle() + " §awurde gespeichert");
            return;
        }

        return;

    }

}