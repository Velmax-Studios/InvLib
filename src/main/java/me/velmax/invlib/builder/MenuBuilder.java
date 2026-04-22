package me.velmax.invlib.builder;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.Layout;
import me.velmax.invlib.MenuButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
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
 * A fluent builder for creating and opening inventory menus.
 * 
 * <p>The MenuBuilder simplifies the process of creating chest inventories,
 * applying layouts, and binding interaction logic.</p>
 */
public final class MenuBuilder {

    private final BaseMenu menu;
    private Component title = Component.empty();
    private Layout layout;
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;
    private final Map<Integer, MenuButton> manualButtons = new HashMap<>();

    private MenuBuilder(@NotNull BaseMenu menu) {
        this.menu = menu;
    }

    /**
     * Starts building a chest-based menu with a specific number of rows.
     *
     * @param rows The number of rows (1-6).
     * @return A new MenuBuilder instance.
     */
    public static @NotNull MenuBuilder chest(int rows) {
        return new MenuBuilder(new BaseMenu(rows * 9));
    }

    /**
     * Starts building a menu with a specific inventory type.
     *
     * @param type The type of inventory.
     * @return A new MenuBuilder instance.
     */
    public static @NotNull MenuBuilder type(@NotNull InventoryType type) {
        return new MenuBuilder(new BaseMenu(type));
    }

    /**
     * Sets the title of the menu.
     *
     * @param title The title component.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    /**
     * Applies a layout pattern to the menu.
     *
     * @param rows The layout rows.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder layout(@NotNull String... rows) {
        this.layout = Layout.create(rows);
        return this;
    }

    /**
     * Binds a character in the layout to a button.
     *
     * @param key    The character key.
     * @param button The button to bind.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder bind(char key, @NotNull MenuButton button) {
        if (layout == null) {
            throw new IllegalStateException("Cannot bind without a layout. Call layout() first.");
        }
        layout.bind(key, button);
        return this;
    }

    public @NotNull MenuBuilder bind(char key, @NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> handler) {
        return bind(key, MenuButton.of(item, handler));
    }

    public @NotNull MenuBuilder bind(char key, @NotNull Material material, @Nullable Consumer<InventoryClickEvent> handler) {
        return bind(key, new ItemStack(material), handler);
    }

    public @NotNull MenuBuilder bind(char key, @NotNull ItemBuilder itemBuilder) {
        return bind(key, MenuButton.of(itemBuilder.build()));
    }

    public @NotNull MenuBuilder bind(char key, @NotNull ItemBuilder itemBuilder, @Nullable Consumer<InventoryClickEvent> handler) {
        return bind(key, itemBuilder.build(), handler);
    }

    /**
     * Sets a button at a specific slot.
     *
     * @param slot   The slot index.
     * @param button The button to set.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder button(int slot, @NotNull MenuButton button) {
        manualButtons.put(slot, button);
        return this;
    }

    public @NotNull MenuBuilder button(int slot, @NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> handler) {
        return button(slot, MenuButton.of(item, handler));
    }

    public @NotNull MenuBuilder button(int slot, @NotNull Material material, @Nullable Consumer<InventoryClickEvent> handler) {
        return button(slot, new ItemStack(material), handler);
    }

    public @NotNull MenuBuilder button(int slot, @NotNull ItemBuilder itemBuilder) {
        return button(slot, MenuButton.of(itemBuilder.build()));
    }

    public @NotNull MenuBuilder button(int slot, @NotNull ItemBuilder itemBuilder, @Nullable Consumer<InventoryClickEvent> handler) {
        return button(slot, itemBuilder.build(), handler);
    }

    /**
     * Sets the handler for when the inventory is opened.
     *
     * @param handler The open handler.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder onOpen(@NotNull Consumer<InventoryOpenEvent> handler) {
        this.openHandler = handler;
        return this;
    }

    /**
     * Sets the handler for when the inventory is closed.
     *
     * @param handler The close handler.
     * @return This builder instance.
     */
    public @NotNull MenuBuilder onClose(@NotNull Consumer<InventoryCloseEvent> handler) {
        this.closeHandler = handler;
        return this;
    }

    /**
     * Builds the final BaseMenu instance.
     *
     * @return The built menu.
     */
    public @NotNull BaseMenu build() {
        if (layout != null) {
            layout.apply(menu);
        }
        manualButtons.forEach(menu::setButton);
        menu.setOpenHandler(openHandler);
        menu.setCloseHandler(closeHandler);
        return menu;
    }

    /**
     * Builds and opens the menu for a player.
     *
     * @param player The player to open for.
     */
    public void open(@NotNull Player player) {
        build().open(player, title);
    }
}
