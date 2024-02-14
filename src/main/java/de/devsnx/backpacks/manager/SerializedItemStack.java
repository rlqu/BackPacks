package de.devsnx.backpacks.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class SerializedItemStack {
    private final String type;
    private final int amount;
    private final short durability;
    private final String[] enchantments;
    private final int slot;

    public SerializedItemStack(ItemStack itemStack, int slot) {
        if (itemStack == null) {
            this.type = "";
            this.amount = 0;
            this.durability = 0;
            this.enchantments = new String[0];
            this.slot = slot;
            return;
        }
        this.slot = slot;

        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            this.type = Material.ENCHANTED_BOOK.name();
            this.amount = itemStack.getAmount();
            this.durability = itemStack.getDurability();

            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            Map<Enchantment, Integer> enchantmentsMap = meta.getStoredEnchants();
            this.enchantments = new String[enchantmentsMap.size()];
            int index = 0;
            for (Map.Entry<Enchantment, Integer> entry : enchantmentsMap.entrySet()) {
                enchantments[index++] = entry.getKey().getName() + ":" + entry.getValue();
            }
        } else {
            this.type = itemStack.getType().name();
            this.amount = itemStack.getAmount();
            this.durability = itemStack.getDurability();
            Map<Enchantment, Integer> enchantmentsMap = itemStack.getEnchantments();
            this.enchantments = new String[enchantmentsMap.size()];
            int index = 0;
            for (Map.Entry<Enchantment, Integer> entry : enchantmentsMap.entrySet()) {
                enchantments[index++] = entry.getKey().getName() + ":" + entry.getValue();
            }
        }
    }

    public ItemStack toItemStack() {
        Material material = Material.getMaterial(type);
        if (material == null) {
            // handle unknown material type
            return new ItemStack(Material.AIR);
        }

        ItemStack itemStack;
        if (material == Material.ENCHANTED_BOOK) {
            itemStack = new ItemStack(material);
        } else {
            itemStack = new ItemStack(material, amount, durability);
        }

        for (String enchantmentString : enchantments) {
            String[] parts = enchantmentString.split(":");
            if (parts.length != 2) {
                continue;
            }
            Enchantment enchantment = Enchantment.getByName(parts[0]);
            if (enchantment == null) {
                continue;
            }
            int level;
            try {
                level = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (material == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                if (meta == null) {
                    meta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(material);
                }
                meta.addStoredEnchant(enchantment, level, true);
                itemStack.setItemMeta(meta);
            } else {
                itemStack.addUnsafeEnchantment(enchantment, level);
            }
        }
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

}
