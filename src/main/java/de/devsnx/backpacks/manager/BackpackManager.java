package de.devsnx.backpacks.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 21:01
 */

public class BackpackManager {

    private HashMap<UUID, HashMap<UUID, BackPack>> playerBackpacks;

    public BackpackManager(){
        playerBackpacks = new HashMap<>();
        loadBackpacks();
    }

    public void addBackpack(UUID playerUUID, BackPack backpack) {
        HashMap<UUID, BackPack> backpacks = playerBackpacks.getOrDefault(playerUUID, new HashMap<>());
        backpacks.put(backpack.getUuid(), backpack);
        playerBackpacks.put(playerUUID, backpacks);
        saveBackpacks();
    }

    public void addItemToBackpack(UUID playerUUID, UUID backpackUUID, ItemStack item) {
        BackPack backpack = playerBackpacks.getOrDefault(playerUUID, new HashMap<>()).get(backpackUUID);
        if (backpack != null) {
            backpack.addItem(item);
            saveBackpacks(); // Speichern nach dem Hinzufügen
        }
    }

    public void removeBackpack(UUID playerUUID, UUID backpackUUID) {
        HashMap<UUID, BackPack> backpacks = playerBackpacks.get(playerUUID);
        if (backpacks != null) {
            backpacks.remove(backpackUUID);
            saveBackpacks();
        }
    }

    public BackPack getBackpack(UUID playerUUID, UUID backpackUUID) {
        HashMap<UUID, BackPack> backpacks = playerBackpacks.get(playerUUID);
        if (backpacks != null) {
            return backpacks.get(backpackUUID);
        }
        return null;
    }

    public HashMap<UUID, BackPack> getPlayerBackpacks(UUID playerUUID) {
        return playerBackpacks.getOrDefault(playerUUID, new HashMap<>());
    }

    private void loadBackpacks(){
        try {
            File file = new File("backpacks.json");
            if (!file.exists())
                return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            Type type = new TypeToken<HashMap<UUID, HashMap<UUID, BackPack>>>(){}.getType();
            playerBackpacks = new Gson().fromJson(jsonString.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBackpacks(){
        try {
            File file = new File("backpacks.json");
            if (!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(playerBackpacks, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}