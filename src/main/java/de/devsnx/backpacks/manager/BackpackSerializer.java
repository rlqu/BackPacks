package de.devsnx.backpacks.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:37
 */

public class BackpackSerializer {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public static String serializeBackpack(Inventory backpackInventory) {
        ItemStack[] contents = backpackInventory.getContents();
        List<SerializedItemStack> serializedContents = new ArrayList<>();
        for (ItemStack itemStack : contents) {
            if (itemStack != null) {
                serializedContents.add(new SerializedItemStack(itemStack));
            }
        }
        return gson.toJson(serializedContents);
    }

    public static Inventory deserializeBackpack(String json) {
        List<SerializedItemStack> serializedContents = gson.fromJson(json, new ArrayList<SerializedItemStack>().getClass());
        Inventory backpackInventory = Bukkit.createInventory(null, 27); // Erstelle ein neues Inventar
        for (SerializedItemStack serializedItemStack : serializedContents) {
            ItemStack itemStack = serializedItemStack.toItemStack();
            backpackInventory.addItem(itemStack); // Füge den Gegenstand dem Rucksack-Inventar hinzu
        }
        return backpackInventory;
    }

}

class SerializedItemStack {
    private final String type;
    private final int amount;
    private final short durability;
    private final String[] enchantments;

    public SerializedItemStack(ItemStack itemStack) {
        this.type = itemStack.getType().name();
        this.amount = itemStack.getAmount();
        this.durability = itemStack.getDurability();
        this.enchantments = new String[itemStack.getEnchantments().size()];
        int index = 0;
        for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            enchantments[index++] = entry.getKey().getName() + ":" + entry.getValue();
        }
    }

    public ItemStack toItemStack() {
        Material material = Material.getMaterial(type);
        ItemStack itemStack = new ItemStack(material, amount, durability);
        for (String enchantmentString : enchantments) {
            String[] parts = enchantmentString.split(":");
            Enchantment enchantment = Enchantment.getByName(parts[0]);
            int level = Integer.parseInt(parts[1]);
            if (enchantment != null) {
                itemStack.addUnsafeEnchantment(enchantment, level);
            }
        }
        return itemStack;
    }
}