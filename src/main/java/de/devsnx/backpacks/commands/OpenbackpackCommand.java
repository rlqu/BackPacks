package de.devsnx.backpacks.commands;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import de.devsnx.backpacks.manager.BackpackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 13.02.2024 17:31
 */

public class OpenbackpackCommand implements CommandExecutor {
    private final BackpackManager backpackManager;

    public OpenbackpackCommand(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /openbackpack <backpack_id>");
            return true;
        }

        int backpackId;
        try {
            backpackId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid backpack ID. Please enter a valid number.");
            return true;
        }

        String serializedBackpack = BackpackFileStorage.loadBackpack(player.getUniqueId(), backpackId);
        if (serializedBackpack == null) {
            player.sendMessage("You don't have a backpack with ID " + backpackId + ".");
            return true;
        }


        Inventory oldBackpackInventory = BackpackSerializer.deserializeBackpack(serializedBackpack);

        // Erstelle ein neues Inventar mit dem gewünschten Titel
        Inventory newBackpackInventory = Bukkit.createInventory(player, oldBackpackInventory.getSize(), "Backpack #" + backpackId);

        // Kopiere die Inhalte des alten Inventars in das neue Inventar
        newBackpackInventory.setContents(oldBackpackInventory.getContents());

        // Öffne das neue Inventar für den Spieler
        player.openInventory(newBackpackInventory);

        return true;
    }

}