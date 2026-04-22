package me.velmax.invlib.internal;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuButton;
import me.velmax.invlib.MenuWindow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Global listener that routes inventory events to their respective MenuWindow and BaseMenu instances.
 */
public final class InventoryManager implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        InventoryHolder holder = topInventory.getHolder();

        if (!(holder instanceof MenuWindow window)) {
            return;
        }

        BaseMenu menu = window.getContent();

        // Handle clicks in the menu itself
        if (event.getClickedInventory() == topInventory) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            MenuButton button = menu.getButton(slot);
            if (button != null && button.clickHandler() != null) {
                button.clickHandler().accept(event);
            }
        } else {
            // Clicks in player inventory while menu is open
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MenuWindow) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MenuWindow window) {
            BaseMenu menu = window.getContent();
            if (menu.getOpenHandler() != null) {
                menu.getOpenHandler().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MenuWindow window) {
            BaseMenu menu = window.getContent();
            if (menu.getCloseHandler() != null) {
                menu.getCloseHandler().accept(event);
            }
        }
    }
}
