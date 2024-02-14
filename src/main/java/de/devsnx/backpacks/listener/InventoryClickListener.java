package de.devsnx.backpacks.listener;

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

    public InventoryClickListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        // Überprüfen, ob das geklickte Inventar das Rucksack-Inventar eines Spielers ist
        if (clickedInventory != null && clickedInventory.getTitle().equals("§dDeine Backpacks")) {
            event.setCancelled(true); // Verhindert, dass das Item aus dem Rucksack-Inventar genommen wird

            // Überprüfen, ob das angeklickte Item ein Rucksack ist
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.CHEST) {
                // Extrahieren Sie die Rucksack-ID aus dem Item-Namen
                String itemName = clickedItem.getItemMeta().getDisplayName();
                String[] parts = itemName.split("#");
                if (parts.length == 2) {
                    try {
                        int backpackId = Integer.parseInt(parts[1].trim());
                        // Laden Sie das entsprechende Rucksack-Inventar und öffnen Sie es für den Spieler
                        String serializedBackpack = backpackManager.getPlayerBackpack(player, backpackId);
                        if (serializedBackpack != null) {

                            Inventory oldBackpackInventory = BackpackSerializer.deserializeBackpack(serializedBackpack);

                            // Erstelle ein neues Inventar mit dem gewünschten Titel
                            Inventory newBackpackInventory = Bukkit.createInventory(player, oldBackpackInventory.getSize(), "Backpack #" + backpackId);

                            // Kopiere die Inhalte des alten Inventars in das neue Inventar
                            newBackpackInventory.setContents(oldBackpackInventory.getContents());

                            // Öffne das neue Inventar für den Spieler
                            player.openInventory(newBackpackInventory);
                        }
                    } catch (NumberFormatException e) {
                        // Fehler beim Extrahieren der Rucksack-ID
                        player.sendMessage("Invalid backpack ID.");
                    }
                }
            }
        }
    }
}