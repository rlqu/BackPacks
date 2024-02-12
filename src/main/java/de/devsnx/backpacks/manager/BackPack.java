package de.devsnx.backpacks.manager;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 13:52
 */

public class BackPack {

    private UUID uuid;
    private Integer backPacks;
    private HashMap<ItemStack, Integer> itemsFromBackPack;

    public BackPack(UUID uuid){
        this.uuid = uuid;
        this.itemsFromBackPack = new HashMap<ItemStack, Integer>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<ItemStack, Integer> getItemsFromBackPack() {
        return itemsFromBackPack;
    }

    private void loadBackPacks(){

    }

    private void saveBackPacks(){

    }

}