package me.velmax.invlib;

import me.velmax.invlib.internal.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The main entry point for the InvLib library.
 */
public final class InvLib {

    private static InventoryManager manager;
    private static JavaPlugin plugin;

    private InvLib() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Initializes the library with the given plugin.
     * This registers the global listener.
     *
     * @param plugin The plugin instance.
     */
    public static void init(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin, "Plugin cannot be null");
        if (InvLib.plugin != null) {
            return;
        }
        InvLib.plugin = plugin;
        InvLib.manager = new InventoryManager();
        plugin.getServer().getPluginManager().registerEvents(manager, plugin);
        
        // Start the animation ticker
        new me.velmax.invlib.internal.AnimationTicker().runTaskTimer(plugin, 1, 1);
    }

    /**
     * Gets the plugin instance used to initialize the library.
     *
     * @return The plugin instance.
     */
    public static @NotNull JavaPlugin getPlugin() {
        if (plugin == null) {
            throw new IllegalStateException("InvLib has not been initialized. Call InvLib.init(plugin) first.");
        }
        return plugin;
    }

    /**
     * Gets the global inventory manager.
     *
     * @return The inventory manager.
     */
    public static @NotNull InventoryManager getManager() {
        if (manager == null) {
            throw new IllegalStateException("InvLib has not been initialized. Call InvLib.init(plugin) first.");
        }
        return manager;
    }
}
