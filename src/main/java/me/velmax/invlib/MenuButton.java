package me.velmax.invlib;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button in a menu.
 *
 * @param item          The item stack to display.
 * @param clickHandler  The handler for click events.
 */
public record MenuButton(
    @NotNull ItemStack item,
    @Nullable Consumer<InventoryClickEvent> clickHandler
) {
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
