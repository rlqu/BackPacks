package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.BackPacks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 13:58
 */

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        BackPacks.getInstance().getBackPackManager().addPlayerToCache(event.getPlayer());

    }

}
