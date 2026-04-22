package me.velmax.invlib.builder;

import me.velmax.invlib.BaseMenu;
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

    public @NotNull BaseMenu build() {
        BaseMenu menu = (type != null) ? new BaseMenu(type, title) : new BaseMenu(size, title);
        buttons.forEach(menu::setButton);
        menu.setOpenHandler(openHandler);
        menu.setCloseHandler(closeHandler);
        return menu;
    }
}
