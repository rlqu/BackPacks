package de.devsnx.backpacks.listener;

import de.devsnx.backpacks.BackPacks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 12.02.2024 13:59
 */

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        BackPacks.getInstance().getBackPackManager().removePlayerFromCache(event.getPlayer());

    }
}
