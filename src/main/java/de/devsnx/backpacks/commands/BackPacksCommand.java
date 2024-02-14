package de.devsnx.backpacks.commands;

import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 14.02.2024 11:54
 */

public class BackPacksCommand implements CommandExecutor {

    private final BackpackManager backpackManager;

    public BackPacksCommand(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        Map<Integer, String> playerBackpacks = backpackManager.getPlayerBackpacks(player);

        Inventory backpackListInventory = Bukkit.createInventory(null, 9 * 3, "§dDeine Backpacks");

        for (Map.Entry<Integer, String> entry : playerBackpacks.entrySet()) {
            String backpackTitle = "Backpack #" + entry.getKey();
            backpackListInventory.addItem(backpackManager.createBackpackItem(backpackTitle));
        }

        player.openInventory(backpackListInventory);
        return true;
    }

}