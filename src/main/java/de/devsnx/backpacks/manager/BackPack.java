package de.devsnx.backpacks.manager;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 14:12
 */

public class BackPack {

    private UUID uuid;
    private HashMap<ItemStack, Integer> items;

    public BackPack(UUID uuid) {
        this.uuid = uuid;
        this.items = new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addItem(ItemStack item) {
        int currentQuantity = items.getOrDefault(item, 0);
        items.put(item, currentQuantity + quantity);
    }

    public void removeItem(ItemStack item, int quantity) {
        int currentQuantity = items.getOrDefault(item, 0);
        if (currentQuantity <= quantity) {
            items.remove(item);
        } else {
            items.put(item, currentQuantity - quantity);
        }
    }

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }
}