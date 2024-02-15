package de.devsnx.backpacks.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 20:33
 */

public class SerializedItemStack {

    private final String type;
    private final int amount;
    private final short durability;
    private final String[] enchantments;
    private final List<String> lore;
    private final String displayName;
    private final int slot;

    public SerializedItemStack(ItemStack itemStack, int slot) {
        if (itemStack == null) {
            this.type = "";
            this.amount = 0;
            this.durability = 0;
            this.enchantments = new String[0];
            this.lore = null;
            this.displayName = null;
            this.slot = slot;
            return;
        }

        this.slot = slot;
        this.type = itemStack.getType().name();
        this.amount = itemStack.getAmount();
        this.durability = itemStack.getDurability();
        this.lore = itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : null;
        this.displayName = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : null;

        Map<Enchantment, Integer> enchantmentsMap;
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            enchantmentsMap = meta.getStoredEnchants();
        } else {
            enchantmentsMap = itemStack.getEnchantments();
        }

        this.enchantments = new String[enchantmentsMap.size()];
        int index = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantmentsMap.entrySet()) {
            enchantments[index++] = entry.getKey().getName() + ":" + entry.getValue();
        }
    }

    public ItemStack toItemStack() {
        Material material = Material.getMaterial(type);
        if (material == null || material.getMaxDurability() == 0) {
            // handle unknown or non-item material type
            return new ItemStack(Material.AIR);
        }

        ItemStack itemStack = new ItemStack(material, amount, durability);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && lore != null) {
            meta.setLore(lore);
            if (displayName != null) { // Set display name if available
                meta.setDisplayName(displayName);
            }
            itemStack.setItemMeta(meta);
        }

        for (String enchantmentString : enchantments) {
            String[] parts = enchantmentString.split(":");
            if (parts.length != 2) {
                continue;
            }
            Enchantment enchantment = Enchantment.getByName(parts[0]);
            if (enchantment == null || !enchantment.canEnchantItem(itemStack)) {
                continue;
            }
            int level;
            try {
                level = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (material == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                if (bookMeta == null) {
                    bookMeta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(material);
                }
                bookMeta.addStoredEnchant(enchantment, level, true);
                itemStack.setItemMeta(bookMeta);
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