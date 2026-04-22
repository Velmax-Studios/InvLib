package me.velmax.invlib.builder;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.Layout;
import me.velmax.invlib.MenuButton;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A fluent API for building menus.
 */
public final class MenuBuilder {

    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    private final Component title;
    private final int size;
    private final InventoryType type;
    
    private Layout layout;
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;

    private MenuBuilder(int size, @NotNull Component title) {
        this.size = size;
        this.type = null;
        this.title = title;
    }

    private MenuBuilder(@NotNull InventoryType type, @NotNull Component title) {
        this.size = -1;
        this.type = type;
        this.title = title;
    }

    public static @NotNull MenuBuilder chest(int rows, @NotNull Component title) {
        return new MenuBuilder(rows * 9, title);
    }

    public static @NotNull MenuBuilder type(@NotNull InventoryType type, @NotNull Component title) {
        return new MenuBuilder(type, title);
    }

    public @NotNull MenuBuilder layout(@NotNull String... rows) {
        this.layout = Layout.create(rows);
        return this;
    }

    public @NotNull MenuBuilder bind(char key, @NotNull MenuButton button) {
        if (layout == null) {
            throw new IllegalStateException("Cannot bind without a layout. Call layout() first.");
        }
        layout.bind(key, button);
        return this;
    }

    public @NotNull MenuBuilder bind(char key, @NotNull org.bukkit.inventory.ItemStack item, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return bind(key, MenuButton.of(item, handler));
    }

    public @NotNull MenuBuilder bind(char key, @NotNull org.bukkit.Material material, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return bind(key, new org.bukkit.inventory.ItemStack(material), handler);
    }

    public @NotNull MenuBuilder bind(char key, @NotNull ItemBuilder itemBuilder) {
        return bind(key, MenuButton.of(itemBuilder.build()));
    }

    public @NotNull MenuBuilder bind(char key, @NotNull ItemBuilder itemBuilder, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return bind(key, itemBuilder.build(), handler);
    }

    public @NotNull MenuBuilder button(int slot, @NotNull MenuButton button) {
        buttons.put(slot, button);
        return this;
    }

    public @NotNull MenuBuilder button(int slot, @NotNull org.bukkit.inventory.ItemStack item, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return button(slot, MenuButton.of(item, handler));
    }

    public @NotNull MenuBuilder button(int slot, @NotNull org.bukkit.Material material, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return button(slot, new org.bukkit.inventory.ItemStack(material), handler);
    }

    public @NotNull MenuBuilder button(int slot, @NotNull ItemBuilder itemBuilder) {
        return button(slot, MenuButton.of(itemBuilder.build()));
    }

    public @NotNull MenuBuilder button(int slot, @NotNull ItemBuilder itemBuilder, @Nullable Consumer<org.bukkit.event.inventory.InventoryClickEvent> handler) {
        return button(slot, itemBuilder.build(), handler);
    }

    public @NotNull MenuBuilder onOpen(@NotNull Consumer<InventoryOpenEvent> handler) {
        this.openHandler = handler;
        return this;
    }

    public @NotNull MenuBuilder onClose(@NotNull Consumer<InventoryCloseEvent> handler) {
        this.closeHandler = handler;
        return this;
    }

    /**
     * Builds the menu.
     *
     * @return The built BaseMenu instance.
     */
    public BaseMenu build() {
        BaseMenu menu;
        if (type != null) {
            menu = new BaseMenu(type);
        } else {
            menu = new BaseMenu(size);
        }

        // Apply bindings
        if (layout != null) {
            layout.apply(menu);
        }

        // Apply manual buttons
        buttons.forEach(menu::setButton);

        // Apply handlers
        menu.setOpenHandler(openHandler);
        menu.setCloseHandler(closeHandler);

        return menu;
    }

    /**
     * Builds and opens the menu for a player.
     *
     * @param player The player to open for.
     */
    public void open(org.bukkit.entity.Player player) {
        build().open(player, title);
    }
}
