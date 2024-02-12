package de.devsnx.backpacks.manager;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 14:16
 */

public class YourBackpackHolder implements InventoryHolder {
    private final UUID backpackUUID;

    public YourBackpackHolder(UUID backpackUUID) {
        this.backpackUUID = backpackUUID;
    }

    public UUID getBackpackUUID() {
        return backpackUUID;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}