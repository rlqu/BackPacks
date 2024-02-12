package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.manager.BackpackManager;
import de.devsnx.backpacks.manager.YourBackpackHolder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 13:47
 */

public class InventoryCloseListener {

    private final BackpackManager backpackManager;

    public InventoryCloseListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof YourBackpackHolder)) {
            return; // Not a backpack inventory
        }

        UUID playerUUID = event.getPlayer().getUniqueId();
        UUID backpackUUID = ((YourBackpackHolder) inventory.getHolder()).getBackpackUUID();

        ItemStack[] inventoryContents = inventory.getContents();
        Map<Integer, ItemStack> contents = new HashMap<>();

        for (int i = 0; i < inventoryContents.length; i++) {
            ItemStack item = inventoryContents[i];
            contents.put(i, item);
        }

        for (ItemStack item : contents.values()) {
            if (item == null || item.getType() == Material.AIR) {
                continue; // Skip empty slots or air
            }

            backpackManager.addItemToBackpack(playerUUID, backpackUUID, item);
        }

    }

}