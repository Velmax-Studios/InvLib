package me.velmax.invlib;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The base class for all menus in InvLib.
 * Now acts as a container for menu content (buttons and logic).
 */
public class BaseMenu {

    private final int size;
    private final InventoryType type;
    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;

    public BaseMenu(@NotNull InventoryType type) {
        this.size = -1;
        this.type = type;
    }

    public BaseMenu(int size) {
        this.size = size;
        this.type = null;
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
        } else {
            buttons.put(slot, button);
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
     * Gets all buttons in the menu.
     *
     * @return A map of slot to ItemStack for the buttons.
     */
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> items = new HashMap<>();
        buttons.forEach((slot, button) -> items.put(slot, button.item()));
        return items;
    }

    /**
     * Clears all buttons from the menu.
     */
    public void clearButtons() {
        buttons.clear();
    }

    public int getSize() {
        return size;
    }

    public @Nullable InventoryType getType() {
        return type;
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
     * This creates a new MenuWindow and opens it.
     *
     * @param player The player to open for.
     * @param title  The title of the menu.
     */
    public void open(@NotNull org.bukkit.entity.Player player, @NotNull Component title) {
        new MenuWindow(player, this, title).open();
    }
}
