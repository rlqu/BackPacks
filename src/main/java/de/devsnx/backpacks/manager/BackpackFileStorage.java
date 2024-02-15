package de.devsnx.backpacks.manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:40
 */

public class BackpackFileStorage {

    private static final String BACKPACKS_FOLDER = "plugins/backpacks";

    private static Map<UUID, Map<String, Integer>> playerBackpacksMap = new HashMap<>();
    private static Map<UUID, Map<Integer, String>> playerBackpackIdsMap = new HashMap<>();


    public static void createBackpack(UUID playerUUID, int backpackId, String backpackName) {
        File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

        // Erstelle JSON-Datei für den Rucksack
        File backpackFile = new File(playerFolder, backpackId + ".json");
        try (FileWriter writer = new FileWriter(backpackFile)) {
            ArrayList<String> testData = new ArrayList<>();
            testData.add(" ");
            writer.write(testData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Erstelle Textdatei mit dem Namen des Rucksacks
        File backpackNameFile = new File(playerFolder, backpackId + ".txt");
        try (FileWriter writer = new FileWriter(backpackNameFile)) {
            writer.write("backpackName:" + backpackName + "\n");
            writer.write("backpackId:" + backpackId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Speichern Sie die Zuordnung zwischen Namen und ID
        playerBackpacksMap.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(backpackName, backpackId);
        playerBackpackIdsMap.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(backpackId, backpackName);
    }

    public static void saveBackpack(UUID playerUUID, int backpackId, String serializedBackpack) {
        File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }
        File backpackFile = new File(playerFolder, backpackId + ".json");
        try (FileWriter writer = new FileWriter(backpackFile)) {
            writer.write(serializedBackpack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadBackpack(UUID playerUUID, int backpackId) {
        File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
        if (!playerFolder.exists()) {
            return null;
        }
        File backpackFile = new File(playerFolder, backpackId + ".json");
        if (!backpackFile.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(backpackFile)) {
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //loadBackpackNames

    public static void loadBackpackNames(UUID playerUUID) {
        File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
        if (!playerFolder.exists()) {
            return;
        }
        for (int backpackId = 1; ; backpackId++) {
            File backpackNameFile = new File(playerFolder, backpackId + ".txt");
            if (!backpackNameFile.exists()) {
                break; // Break loop if file doesn't exist for this ID
            }
            System.out.println("ID: " + backpackId);
            try (BufferedReader br = new BufferedReader(new FileReader(backpackNameFile))) {
                String line;
                String backpackName = null;
                int parsedBackpackId = -1;
                while ((line = br.readLine()) != null) {
                    String[] keyValue = line.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        if (key.equals("backpackName")) {
                            backpackName = value;
                        } else if (key.equals("backpackId")) {
                            parsedBackpackId = Integer.parseInt(value);
                        }
                    }
                }
                if (backpackName != null && parsedBackpackId != -1) {
                    playerBackpacksMap.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(backpackName, parsedBackpackId);
                    playerBackpackIdsMap.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(parsedBackpackId, backpackName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getBackpackIdByName(UUID playerUUID, String backpackName) {
        Map<String, Integer> backpacks = playerBackpacksMap.get(playerUUID);
        if (backpacks != null) {
            return backpacks.getOrDefault(backpackName, -1);
        }
        return -1;
    }

    public static void renameBackpack(UUID playerUUID, int backpackId, String newBackpackName) {
        loadBackpackNames(playerUUID); // Load backpack names if not already loaded
        Map<Integer, String> backpackIds = playerBackpackIdsMap.get(playerUUID);
        if (backpackIds != null && backpackIds.containsKey(backpackId)) {
            String oldBackpackName = backpackIds.get(backpackId);
            backpackIds.put(backpackId, newBackpackName); // Update name in ID map
            playerBackpacksMap.get(playerUUID).remove(oldBackpackName); // Remove old name from name map
            playerBackpacksMap.get(playerUUID).put(newBackpackName, backpackId); // Add new name to name map
            File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
            File nameFile = new File(playerFolder, backpackId + ".txt");
            if (nameFile.exists()) {
                try (FileWriter writer = new FileWriter(nameFile)) {
                    // Update name in text file
                    ArrayList<String> backpackNameData = new ArrayList<>();
                    backpackNameData.add("backpackName:" + newBackpackName);
                    backpackNameData.add("backpackId:" + backpackId);
                    writer.write(String.join(System.lineSeparator(), backpackNameData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isBackpackNameAlreadyExists(UUID playerUUID, String backpackName) {
        loadBackpackNames(playerUUID); // Load backpack names if not already loaded
        Map<String, Integer> playerBackpacks = playerBackpacksMap.get(playerUUID);
        return playerBackpacks != null && playerBackpacks.containsKey(backpackName);
    }

    public static String getBackpackNameById(UUID playerUUID, int backpackId) {
        Map<Integer, String> backpackIds = playerBackpackIdsMap.get(playerUUID);
        if (backpackIds != null) {
            return backpackIds.getOrDefault(backpackId, null);
        }
        return null;
    }

    public static void unloadBackpackNames(UUID playerUUID) {
        playerBackpacksMap.remove(playerUUID);
        playerBackpackIdsMap.remove(playerUUID);
    }

}