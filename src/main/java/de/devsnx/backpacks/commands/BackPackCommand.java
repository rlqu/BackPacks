package de.devsnx.backpacks.commands;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 15.02.2024 20:46
 */

public class BackPackCommand implements CommandExecutor {

    private final BackpackManager backpackManager;

    public BackPackCommand(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    //backpack <ID>
    //backpack <ID> <Spieler>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        if(args.length == 1){
            try {
                Integer number = Integer.valueOf(args[0]);
                if (backpackManager.getPlayerBackpack(player.getUniqueId(), number) == null) {
                    player.sendMessage("§cDu hast keinen Rucksack mit der ID §e" + number);
                    return true;
                }

                player.openInventory(backpackManager.openBackPack(player.getUniqueId(), number));

            } catch (NumberFormatException e) {
                player.sendMessage("§e" + args[0] + " §cist keine gültige Nummer. Bitte gib eine Nummer von §e1-36 §cfür den Rucksack an.");
                return true;
            }
            return true;
        }

        if (args.length == 2) {
            try {

                Integer number = Integer.valueOf(args[0]);

                Player targetOnline = Bukkit.getPlayer(args[1]);
                if (targetOnline != null) {

                    if (backpackManager.getPlayerBackpack(targetOnline.getUniqueId(), number) == null) {
                        player.sendMessage("§cDer Spieler §e" + args[1] + " §chat keinen Rucksack mit der ID §e" + number);
                        return true;
                    }

                    player.openInventory(backpackManager.openBackPackFromOtherPlayer(targetOnline.getUniqueId(), targetOnline.getName() ,number));
                    player.sendMessage("§7Du hast den Rucksack von §e" + targetOnline.getName() + " §7mit der ID§e" + number +" §7geöffnet");
                    return true;

                } else {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if((target == null) || (!target.hasPlayedBefore())){
                        player.sendMessage("§cSpieler " + args[1] + " existiert nicht im Rucksackspeicher.");
                        return true;
                    }

                    backpackManager.loadBackpacks(target.getUniqueId());
                    BackpackFileStorage.loadBackpackNames(target.getUniqueId());

                    if (backpackManager.getPlayerBackpack(target.getUniqueId(), number) == null) {
                        player.sendMessage("§cDer Spieler §e" + args[1] + " §chat keinen Rucksack mit der ID §e" + number);
                        return true;
                    }

                    player.openInventory(backpackManager.openBackPackFromOtherPlayer(target.getUniqueId(), target.getName(), number));
                    player.sendMessage("§7Du hast den Rucksack von §e" + target.getName() + " §7mit der ID§e" + number +" §7geöffnet");
                    return true;

                }

            } catch (NumberFormatException e) {
                player.sendMessage("§e" + args[0] + " §cist keine gültige Nummer. Bitte gib eine Nummer von §e1-36 §cfür den Rucksack an.");
                return true;
            }
        }

        sendHelp(player);
        return false;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§cVerwendung: /backpack <ID> <Spieler>");
    }
}
