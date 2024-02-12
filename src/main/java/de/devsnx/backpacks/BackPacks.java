package de.devsnx.backpacks;

import de.devsnx.backpacks.manager.BackPackManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BackPacks extends JavaPlugin {

    public static BackPacks instance;
    private BackPackManager backPackManager;

    @Override
    public void onEnable() {
        instance = this;
        backPackManager = new BackPackManager();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static BackPacks getInstance() {
        return instance;
    }

    public BackPackManager getBackPackManager() {
        return backPackManager;
    }

}