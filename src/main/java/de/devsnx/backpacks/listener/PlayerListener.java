package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.manager.BackpackFileStorage;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 14.02.2024 11:22
 */

public class PlayerListener implements Listener {

    private final BackpackManager backpackManager;

    public PlayerListener(BackpackManager backpackManager) {
        this.backpackManager = backpackManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        backpackManager.loadBackpacks(event.getPlayer().getUniqueId());
        BackpackFileStorage.loadBackpackNames(event.getPlayer().getUniqueId());

    }

}