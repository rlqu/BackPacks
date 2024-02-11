package de.devsnx.backpacks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 20:58
 */

public class commandBackPack implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0){


        }

        return false;

    }

}
