package de.devsnx.backpacks.commands;

import de.devsnx.backpacks.manager.BackpackManager;
import de.devsnx.backpacks.utils.ItemCreator;
import de.devsnx.backpacks.utils.ItemSkull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        player.openInventory(backpackManager.openBackPackInventory(player.getUniqueId()));
        return true;
    }

}