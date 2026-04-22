package me.velmax.invlib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The base class for all menus in InvLib.
 * Acts as an InventoryHolder to identify InvLib menus.
 */
public class BaseMenu implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;

    public BaseMenu(@NotNull InventoryType type, @NotNull Component title) {
        this.inventory = Bukkit.createInventory(this, type, title);
    }

    public BaseMenu(int size, @NotNull Component title) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets a button in the menu.
     *
     * @param slot   The slot index.
     * @param button The button to set.
     */
    public void setButton(int slot, @Nullable MenuButton button) {
        if (button == null) {
            buttons.remove(slot);
            inventory.setItem(slot, null);
        } else {
            buttons.put(slot, button);
            inventory.setItem(slot, button.item());
        }
    }

    /**
     * Gets the button at a specific slot.
     *
     * @param slot The slot index.
     * @return The button, or null if empty.
     */
    public @Nullable MenuButton getButton(int slot) {
        return buttons.get(slot);
    }

    /**
     * Clears all buttons from the menu.
     */
    public void clearButtons() {
        buttons.clear();
        inventory.clear();
    }

    public @Nullable Consumer<InventoryOpenEvent> getOpenHandler() {
        return openHandler;
    }

    public void setOpenHandler(@Nullable Consumer<InventoryOpenEvent> openHandler) {
        this.openHandler = openHandler;
    }

    public @Nullable Consumer<InventoryCloseEvent> getCloseHandler() {
        return closeHandler;
    }

    public void setCloseHandler(@Nullable Consumer<InventoryCloseEvent> closeHandler) {
        this.closeHandler = closeHandler;
    }

    /**
     * Opens the menu for a player.
     *
     * @param player The player to open for.
     */
    public void open(@NotNull org.bukkit.entity.Player player) {
        player.openInventory(inventory);
    }
}
