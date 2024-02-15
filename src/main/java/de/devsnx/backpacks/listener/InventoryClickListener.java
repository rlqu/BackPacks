package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.BackPacks;
import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
        ItemStack clickedItem = event.getCurrentItem();

        if(backpackManager.isBackpackInventory(player.getUniqueId(), clickedInventory)) {

            if(event.getSlot() >= 27) {
                event.setCancelled(true);

                if(clickedItem.getType() == Material.NAME_TAG){

                    String backpackName = clickedInventory.getTitle();
                    int backpackID = BackpackFileStorage.getBackpackIdByName(player.getUniqueId(), backpackName);

                    player.closeInventory();
                    player.sendMessage("§aBitte gib einen Namen für den Rucksack ein: ");

                    Bukkit.getPluginManager().registerEvents(new Listener() {
                        @EventHandler
                        public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
                            Player chatPlayer = chatEvent.getPlayer();
                            String playerName = chatPlayer.getName();
                            String message = chatEvent.getMessage();

                            if (playerName.equals(player.getName())) {

                                chatEvent.setCancelled(true);

                                if (message.matches("[a-zA-Z0-9 ]+")) {

                                    String newBackpackName = message;

                                    if(!BackpackFileStorage.isBackpackNameAlreadyExists(player.getUniqueId(), newBackpackName)){
                                        BackpackFileStorage.renameBackpack(player.getUniqueId(), backpackID, message);
                                        HandlerList.unregisterAll(this);
                                        chatPlayer.sendMessage("§7Du hast den Rucksacknamen von §e" + backpackName + " §7auf §b" + newBackpackName + " §7geändert.");
                                        return;
                                    } else {

                                        chatPlayer.sendMessage("§cDer Rucksackname existiert bereits für einen anderen Rucksack. Bitte versuche es erneut.");

                                    }
                                }else {

                                    chatPlayer.sendMessage("§cDer Rucksackname darf keine Sonderzeichen enthalten. Bitte versuche es erneut.");

                                }

                            }

                        }

                    }, BackPacks.getInstance());

                }

                return;
            }

        }

        if (clickedInventory != null && clickedInventory.getTitle().equals("§dDeine Backpacks")) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() == Material.CHEST) {
                String itemName = clickedItem.getItemMeta().getDisplayName().replace("§b", "");

                if(BackpackFileStorage.getBackpackIdByName(player.getUniqueId(), itemName) != -1){

                    int backpackId = BackpackFileStorage.getBackpackIdByName(player.getUniqueId(), itemName);
                    player.openInventory(backpackManager.openBackPack(player.getUniqueId(), backpackId));

                } else {

                    player.sendMessage("§cKein Rucksack #2");

                }

            }

            if(clickedItem.getType() == Material.SKULL_ITEM){

                if(clickedItem.getItemMeta().getDisplayName().equals("§aJetzt Rucksack erstellen.")){

                    int backpacks = backpackManager.getPlayerBackpacks(player.getUniqueId()).size();

                    if(player.hasPermission("backpack.*")) {
                        if(backpacks < 36){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission("backpack.33")) {
                        if(backpacks < 33){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission("backpack.30")) {
                        if(backpacks < 30){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission("backpack.27")) {
                        if(backpacks < 27){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission("backpack.24")) {
                        if(backpacks < 24){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }

                    } else if(player.hasPermission( "backpack.20")) {
                        if(backpacks < 20){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.16")) {
                        if(backpacks < 16){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.12")) {
                        if(backpacks < 12){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.8")) {
                        if(backpacks < 8){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
                            return;
                        } else {
                            player.sendMessage(maxRucksäcke);
                            return;
                        }
                    } else if(player.hasPermission( "backpack.4")) {
                        if(backpacks < 4){
                            createBackPack(player);
                            player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
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
        int newBackpackId = backpackManager.getNextAvailableBackpackId(player.getUniqueId());
        BackpackFileStorage.createBackpack(player.getUniqueId(), newBackpackId, "Rucksack #"+ newBackpackId);
        player.sendMessage("§aBackpack erstellt: §eRucksack#" + newBackpackId);
        backpackManager.loadBackpacks(player.getUniqueId());
    }

}