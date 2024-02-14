package de.devsnx.backpacks.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:40
 */

public class BackpackFileStorage {
    private static final String BACKPACKS_FOLDER = "plugins/backpacks";

    public static void createBackpack(UUID playerUUID, int backpackId) {

        File playerFolder = new File(BACKPACKS_FOLDER, playerUUID.toString());
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

        File backpackFile = new File(playerFolder, backpackId + ".json");
        try (FileWriter writer = new FileWriter(backpackFile)) {
            ArrayList<String> test = new ArrayList<>();
            test.add(" ");
            writer.write(test.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}