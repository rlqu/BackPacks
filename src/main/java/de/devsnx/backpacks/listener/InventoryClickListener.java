package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import de.devsnx.backpacks.manager.BackpackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:22
 */

public class InventoryClickListener implements Listener {

    private final BackpackManager backpackManager;

    private String maxRucksäcke = "Du hast die Maximale anzahl der Rucksäcke erreicht.";

    public InventoryClickListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getTitle().equals("§dDeine Backpacks")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.CHEST) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                String[] parts = itemName.split("#");
                if (parts.length == 2) {
                    try {
                        int backpackId = Integer.parseInt(parts[1].trim());
                        String serializedBackpack = backpackManager.getPlayerBackpack(player, backpackId);
                        if (serializedBackpack != null) {

                            Inventory oldBackpackInventory = BackpackSerializer.deserializeBackpack(serializedBackpack);

                            Inventory newBackpackInventory = Bukkit.createInventory(player, oldBackpackInventory.getSize(), "Backpack #" + backpackId);
                            newBackpackInventory.setContents(oldBackpackInventory.getContents());

                            player.openInventory(newBackpackInventory);
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("Invalid backpack ID.");
                    }
                }
            }

            if(clickedItem.getType() == Material.SKULL_ITEM){

                if(clickedItem.getItemMeta().getDisplayName().equals("§aJetzt Rucksack erstellen.")){

                    int backpacks = backpackManager.getPlayerBackpacks(player).size();

                    if(player.hasPermission("backpack.27")) {
                        if(backpacks < 27){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission("backpack.24")) {
                        if(backpacks < 24){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }

                    } else if(player.hasPermission( "backpack.20")) {
                        if(backpacks < 20){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.16")) {
                        if(backpacks < 16){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.12")) {
                        if(backpacks < 12){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.8")) {
                        if(backpacks < 8){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.4")) {
                        if(backpacks < 4){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else {

                        player.sendMessage(maxRucksäcke);

                    }

                }

            }

        }

    }

    private void createBackPack(Player player){
        int newBackpackId = backpackManager.getNextAvailableBackpackId(player);
        BackpackFileStorage.createBackpack(player.getUniqueId(), newBackpackId);
        player.sendMessage("Backpack erstellt #" + newBackpackId);
        backpackManager.loadBackpacks(player);
    }

}