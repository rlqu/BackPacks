package de.devsnx.backpacks.manager;

import de.devsnx.backpacks.utils.ItemCreator;
import de.devsnx.backpacks.utils.ItemSkull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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
                String expectedTitle = BackpackFileStorage.getBackpackNameById(player.getUniqueId(), entry.getKey());
                if (expectedTitle.equals(inventory.getTitle())) {
                    return true; // Das Inventar ist ein Rucksack-Inventar
                }
            }
        }
        return false; // Das Inventar ist kein Rucksack-Inventar
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
            if(!backpackMap.containsKey(backpackId)){
                backpackMap.put(backpackId, serializedBackpack);
            }
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
            meta.setDisplayName("§b" + backpackTitle);
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

    public Inventory openBackPackInventory(Player player){

        Map<Integer, String> playerBackpacks = getPlayerBackpacks(player);

        Inventory backpackListInventory = Bukkit.createInventory(null, 9 * 6, "§dDeine Backpacks");
        if(playerBackpacks.size() == 0) {

            backpackListInventory.setItem(22, new ItemCreator().material(Material.BARRIER).displayName("§cDu hast aktuell keine Rucksäcke").build());

        } else {
            for (Map.Entry<Integer, String> entry : playerBackpacks.entrySet()) {
                String backbackTitle = BackpackFileStorage.getBackpackNameById(player.getUniqueId(), entry.getKey());
                backpackListInventory.addItem(createBackpackItem(backbackTitle));
            }
        }

        for(int i  = 4*9; i < 5*9; i++){
            backpackListInventory.setItem(i, new ItemCreator().material(Material.STAINED_GLASS_PANE).data((short)7).displayName("").build());
        }

        backpackListInventory.setItem(47, ItemSkull.getSkull3("http://textures.minecraft.net/texture/2e3f50ba62cbda3ecf5479b62fedebd61d76589771cc19286bf2745cd71e47c6", 1, "Info:"));
        backpackListInventory.setItem(51, ItemSkull.getSkull3("http://textures.minecraft.net/texture/3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716", 1, "§aJetzt Rucksack erstellen."));

        return backpackListInventory;

    }

}