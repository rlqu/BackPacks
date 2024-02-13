package de.devsnx.backpacks.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 21:01
 */

public class BackpackManager {
    private final Map<Player, Map<Integer, String>> playerBackpacks; // Speichere die serialisierten Rucksäcke

    public BackpackManager() {
        this.playerBackpacks = new HashMap<>();
    }

    public void addBackpack(Player player, int backpackId, String serializedBackpack) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.computeIfAbsent(player, k -> new HashMap<>());
        playerBackpackMap.put(backpackId, serializedBackpack);

        // Speichere den Rucksack im Dateisystem
        BackpackFileStorage.saveBackpack(player.getUniqueId(), backpackId, serializedBackpack);
    }

    public Inventory getBackpack(Player player, int backpackId) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.get(player);
        if (playerBackpackMap != null) {
            String serializedBackpack = playerBackpackMap.get(backpackId);
            if (serializedBackpack != null) {
                return BackpackSerializer.deserializeBackpack(serializedBackpack);
            }
        }
        return null;
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
        // Überprüfe, ob der Spieler einen Rucksack hat
        Map<Integer, String> playerBackpackMap = playerBackpacks.get(player);
        if (playerBackpackMap != null) {
            // Durchlaufe die Rucksäcke des Spielers
            for (Map.Entry<Integer, String> entry : playerBackpackMap.entrySet()) {
                // Konstruiere den erwarteten Titel des Rucksack-Inventars
                String expectedTitle = "Backpack #" + entry.getKey();

                // Vergleiche den erwarteten Titel mit dem aktuellen Titel des Inventars
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
                if (entry.getValue().equals(inventoryTitle)) {
                    return entry.getKey();
                }
            }
        }
        return -1; // Rückgabe -1 wenn Rucksack nicht gefunden wurde
    }

    public void updateBackpack(Player player, int backpackId, String serializedBackpack) {
        Map<Integer, String> playerBackpackMap = playerBackpacks.get(player);
        if (playerBackpackMap != null && playerBackpackMap.containsKey(backpackId)) {
            playerBackpackMap.put(backpackId, serializedBackpack);
            BackpackFileStorage.saveBackpack(player.getUniqueId(), backpackId, serializedBackpack);
            // Du könntest hier auch die saveBackpack-Methode aus BackpackFileStorage aufrufen, um den Rucksack zu speichern
        }
    }
}