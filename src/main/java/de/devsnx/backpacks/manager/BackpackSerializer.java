package de.devsnx.backpacks.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:37
 */

public class BackpackSerializer {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public static String serializeBackpack(Inventory backpackInventory) {
        ItemStack[] contents = backpackInventory.getContents();
        List<SerializedItemStack> serializedContents = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack != null) {
                serializedContents.add(new SerializedItemStack(itemStack, i));
            }
        }
        return gson.toJson(serializedContents);
    }

    public static Inventory deserializeBackpack(String json) {
        List<SerializedItemStack> serializedContents = gson.fromJson(json, new TypeToken<List<SerializedItemStack>>(){}.getType());
        Inventory backpackInventory = Bukkit.createInventory(null, 27);
        for (SerializedItemStack serializedItemStack : serializedContents) {
            ItemStack itemStack = serializedItemStack.toItemStack();
            backpackInventory.setItem(serializedItemStack.getSlot(), itemStack);
        }
        return backpackInventory;
    }

}