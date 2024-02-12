package de.devsnx.backpacks.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 11.02.2024 21:01
 */

public class BackPackManager {

    private HashMap<UUID, BackPack> backPackCache;

    public BackPackManager() {

        this.backPackCache = new HashMap<UUID, BackPack>();

    }

    public void addPlayerToCache(Player player) {

        if(!backPackCache.containsKey(player.getUniqueId())) {

            backPackCache.put(player.getUniqueId(), new BackPack(player.getUniqueId()));

        }

    }

    public void removePlayerFromCache(Player player) {

        if(backPackCache.containsKey(player.getUniqueId())) {

            backPackCache.remove(player.getUniqueId());

        }

    }

}