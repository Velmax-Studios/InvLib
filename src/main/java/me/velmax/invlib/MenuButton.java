package me.velmax.invlib;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button in a menu.
 */
public class MenuButton {

    private ItemStack item;
    private final Consumer<InventoryClickEvent> clickHandler;

    public MenuButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> clickHandler) {
        this.item = item;
        this.clickHandler = clickHandler;
    }

    /**
     * Gets the item stack to display.
     *
     * @return The item stack.
     */
    public @NotNull ItemStack item() {
        return item;
    }

    /**
     * Sets the item stack to display.
     *
     * @param item The item stack.
     */
    public void setItem(@NotNull ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the handler for click events.
     *
     * @return The click handler.
     */
    public @Nullable Consumer<InventoryClickEvent> clickHandler() {
        return clickHandler;
    }

    /**
     * Creates a button with no click handler.
     *
     * @param item The item stack.
     * @return The button.
     */
    public static @NotNull MenuButton of(@NotNull ItemStack item) {
        return new MenuButton(item, null);
    }

    /**
     * Creates a button with a click handler.
     *
     * @param item         The item stack.
     * @param clickHandler The handler.
     * @return The button.
     */
    public static @NotNull MenuButton of(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> clickHandler) {
        return new MenuButton(item, clickHandler);
    }
}
