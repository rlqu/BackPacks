package de.devsnx.backpacks.commands;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import de.devsnx.backpacks.manager.BackpackSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:45
 */

public class CreateBackpackCommand implements CommandExecutor {
    private final BackpackManager backpackManager;

    public CreateBackpackCommand(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Überprüfe, ob der Spieler das Recht hat, einen neuen Rucksack zu erstellen

        // Erhalte die nächste verfügbare Rucksack-ID für den Spieler
        int newBackpackId = backpackManager.getNextAvailableBackpackId(player);

        // Erstelle ein neues Rucksack-Inventory mit 27 Slots (Standardgröße einer Truhe)
        Inventory newBackpackInventory = player.getServer().createInventory(player, 27, "Backpack #" + newBackpackId);

        // Füge den neuen Rucksack zum BackpackManager hinzu
        backpackManager.addBackpack(player, newBackpackId, "empty"); // hier "empty" als Platzhalter, weil das Inventar noch leer ist

        // Serialisiere das Rucksack-Inventory und speichere es in einer Datei
        String serializedBackpack = BackpackSerializer.serializeBackpack(newBackpackInventory);
        BackpackFileStorage.saveBackpack(player.getUniqueId(), newBackpackId, serializedBackpack);

        // Gib eine Bestätigungsnachricht aus
        player.sendMessage("New backpack created with ID " + newBackpackId + "!");

        return true;
    }
}