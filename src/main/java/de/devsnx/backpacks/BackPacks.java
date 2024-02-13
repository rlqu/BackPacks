package de.devsnx.backpacks;

import de.devsnx.backpacks.commands.BackpackCommand;
import de.devsnx.backpacks.commands.CreateBackpackCommand;
import de.devsnx.backpacks.listener.InventoryCloseListener;
import de.devsnx.backpacks.manager.BackpackManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BackPacks extends JavaPlugin {

    public static BackPacks instance;
    private BackpackManager backPackManager;

    @Override
    public void onEnable() {
        instance = this;
        backPackManager = new BackpackManager();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryCloseListener(backPackManager), this);

        getCommand("createbackpack").setExecutor(new CreateBackpackCommand(backPackManager));
        getCommand("openbackpack").setExecutor(new BackpackCommand(backPackManager));

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static BackPacks getInstance() {
        return instance;
    }

    public BackpackManager getBackPackManager() {
        return backPackManager;
    }

}