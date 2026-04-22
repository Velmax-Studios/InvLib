package me.velmax.invlib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an active inventory view for a specific player.
 */
public final class MenuWindow implements InventoryHolder {

    private final Player viewer;
    private final BaseMenu content;
    private Component title;
    private Inventory inventory;

    public MenuWindow(@NotNull Player viewer, @NotNull BaseMenu content, @NotNull Component title) {
        this.viewer = viewer;
        this.content = content;
        this.title = title;
    }

    /**
     * Opens the window for the viewer.
     */
    public void open() {
        if (content.getType() != null) {
            this.inventory = Bukkit.createInventory(this, content.getType(), title);
        } else {
            this.inventory = Bukkit.createInventory(this, content.getSize(), title);
        }
        
        refresh();
        viewer.openInventory(inventory);
    }

    /**
     * Updates the title of the window.
     * This will recreate the inventory and reopen it for the player.
     *
     * @param title The new title.
     */
    public void setTitle(@NotNull Component title) {
        this.title = title;
        if (isOpen()) {
            open(); // Re-opening with new title
        }
    }

    /**
     * Refreshes the inventory content from the BaseMenu.
     */
    public void refresh() {
        if (inventory == null) return;
        inventory.clear();
        content.getButtons().forEach(inventory::setItem);
    }

    /**
     * Gets the viewer of this window.
     *
     * @return The player.
     */
    public @NotNull Player getViewer() {
        return viewer;
    }

    /**
     * Gets the content (BaseMenu) of this window.
     *
     * @return The BaseMenu.
     */
    public @NotNull BaseMenu getContent() {
        return content;
    }

    /**
     * Checks if the window is currently open for the viewer.
     *
     * @return True if open.
     */
    public boolean isOpen() {
        return viewer.getOpenInventory().getTopInventory().getHolder() == this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
