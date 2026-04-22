package me.velmax.invlib;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

/**
 * The base class for all menus in InvLib.
 * 
 * <p>A BaseMenu acts as a container for inventory content, including buttons,
 * handlers, and layout information. It is decoupled from any specific player view,
 * allowing it to be shared between multiple viewers simultaneously.</p>
 */
public class BaseMenu {

    private static final Set<BaseMenu> ALL_MENUS = Collections.newSetFromMap(new WeakHashMap<>());

    private final int size;
    private final InventoryType type;
    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    private final Set<MenuWindow> activeWindows = new java.util.HashSet<>();
    
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;

    /**
     * Creates a new BaseMenu with a specific inventory type.
     *
     * @param type The type of inventory (e.g., CHEST, HOPPER, ANVIL).
     */
    public BaseMenu(@NotNull InventoryType type) {
        this.size = -1;
        this.type = type;
        ALL_MENUS.add(this);
    }

    /**
     * Creates a new chest-based BaseMenu with a specific size or row count.
     *
     * @param size The number of slots (if &gt; 6) or rows (if &lt;= 6).
     */
    public BaseMenu(int size) {
        this.size = size <= 6 ? size * 9 : size;
        this.type = null;
        ALL_MENUS.add(this);
    }

    /**
     * Gets all menus that are currently in memory.
     * Internal use only.
     *
     * @return An unmodifiable set of all active menus.
     */
    public static Set<BaseMenu> getAllMenus() {
        return Collections.unmodifiableSet(ALL_MENUS);
    }

    /**
     * Gets all active windows for this menu.
     * Internal use only.
     */
    public Set<MenuWindow> getActiveWindows() {
        return activeWindows;
    }

    /**
     * Gets all raw buttons in the menu.
     * Internal use only.
     */
    public Map<Integer, MenuButton> getRawButtons() {
        return buttons;
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
     * Sets a simple button with an item and no click handler.
     *
     * @param slot The slot index.
     * @param item The item stack.
     */
    public void setButton(int slot, @NotNull ItemStack item) {
        setButton(slot, MenuButton.of(item));
    }

    /**
     * Sets a button with an item and a click handler.
     *
     * @param slot    The slot index.
     * @param item    The item stack.
     * @param handler The click handler.
     */
    public void setButton(int slot, @NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> handler) {
        setButton(slot, MenuButton.of(item, handler));
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

    /**
     * Gets the total size of the menu in slots.
     *
     * @return The size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the inventory type of the menu.
     *
     * @return The inventory type.
     */
    public @Nullable InventoryType getType() {
        return type;
    }

    /**
     * Gets the open handler for the menu.
     *
     * @return The open handler.
     */
    public @Nullable Consumer<InventoryOpenEvent> getOpenHandler() {
        return openHandler;
    }

    public void setOpenHandler(@Nullable Consumer<InventoryOpenEvent> openHandler) {
        this.openHandler = openHandler;
    }

    /**
     * Gets the close handler for the menu.
     *
     * @return The close handler.
     */
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

    /**
     * Registers an active window for this menu.
     * Internal use only.
     *
     * @param window The window to register.
     */
    public void registerWindow(@NotNull MenuWindow window) {
        activeWindows.add(window);
    }

    /**
     * Unregisters an active window for this menu.
     * Internal use only.
     *
     * @param window The window to unregister.
     */
    public void unregisterWindow(@NotNull MenuWindow window) {
        activeWindows.remove(window);
    }

    /**
     * Refreshes all active windows for this menu.
     */
    public void updateAll() {
        activeWindows.forEach(MenuWindow::refresh);
    }
}
