package de.devsnx.backpacks.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 21:01
 */

public class BackpackManager {

    private final Map<Player, Map<Integer, String>> playerBackpacks;

    public BackpackManager() {
        this.playerBackpacks = new HashMap<>();
    }

    public void addBackpack(Player player, int backpackId, String serializedBackpack) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.computeIfAbsent(player, k -> new HashMap<>());
        playerBackpackMap.put(backpackId, serializedBackpack);
        BackpackFileStorage.saveBackpack(player.getUniqueId(), backpackId, serializedBackpack);
    }

    public int getNextAvailableBackpackId(Player player) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.getOrDefault(player, new HashMap<>());
        int nextId = 1;
        while (playerBackpackMap.containsKey(nextId)) {
            nextId++;
        }
        return nextId;
    }

    public boolean isBackpackInventory(Player player, Inventory inventory) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.get(player);
        if (playerBackpackMap != null) {
            for (Map.Entry<Integer, String> entry : playerBackpackMap.entrySet()) {
                String expectedTitle = "Backpack #" + entry.getKey();
                if (expectedTitle.equals(inventory.getTitle())) {
                    return true; // Das Inventar ist ein Rucksack-Inventar
                }
            }
        }
        return false; // Das Inventar ist kein Rucksack-Inventar
    }

    public int getBackpackIdFromInventoryTitle(String inventoryTitle) {
        for (Map<Integer, String> backpacks : playerBackpacks.values()) {
            for (Map.Entry<Integer, String> entry : backpacks.entrySet()) {
                String expectedTitle = "Backpack #" + entry.getKey();
                if (expectedTitle.equals(inventoryTitle)) {
                    return entry.getKey();
                }
            }
        }
        return -1; // Rückgabe -1 wenn Rucksack nicht gefunden wurde
    }

    public void updateBackpack(Player player, int backpackId, Inventory updatedBackpackInventory) {
        String serializedBackpack = BackpackSerializer.serializeBackpack(updatedBackpackInventory);
        Map<Integer, String> playerBackpackMap = playerBackpacks.get(player);
        if (playerBackpackMap != null) {
            if (playerBackpackMap.containsKey(backpackId)) {
                playerBackpackMap.put(backpackId, serializedBackpack);
                BackpackFileStorage.saveBackpack(player.getUniqueId(), backpackId, serializedBackpack);
            }
        }
    }

    public void loadBackpacks(Player player) {
        Map<Integer, String> backpackMap = new HashMap<>();
        for (int backpackId = 1; ; backpackId++) {
            String serializedBackpack = BackpackFileStorage.loadBackpack(player.getUniqueId(), backpackId);
            if (serializedBackpack == null) {
                break;
            }
            backpackMap.put(backpackId, serializedBackpack);
        }
        playerBackpacks.put(player, backpackMap);
    }

    public void unloadBackpacks(Player player) {
        playerBackpacks.remove(player);
    }

    public ItemStack createBackpackItem(String backpackTitle) {
        ItemStack backpackItem = new ItemStack(Material.CHEST);
        ItemMeta meta = backpackItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(backpackTitle);
            backpackItem.setItemMeta(meta);
        }
        return backpackItem;
    }

    public Map<Integer, String> getPlayerBackpacks(Player player) {
        return playerBackpacks.getOrDefault(player, new HashMap<>());
    }

    public String getPlayerBackpack(Player player, int backpackId) {
        Map<Integer, String> playerBackpacksMap = playerBackpacks.get(player);
        if (playerBackpacksMap != null) {
            return playerBackpacksMap.get(backpackId);
        }
        return null;
    }

}