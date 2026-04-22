package me.velmax.invlib.internal;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuButton;
import me.velmax.invlib.MenuWindow;
import me.velmax.invlib.special.AnvilMenu;
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
            int slot = event.getSlot();

            // Special handling for Anvil completion
            if (menu instanceof AnvilMenu anvil && slot == 2) {
                if (anvil.getCompleteHandler() != null) {
                    anvil.getCompleteHandler().accept(anvil.getRenameText(), event);
                }
                return;
            }

            MenuButton button = menu.getButton(slot);
            if (button != null && button.clickHandler() != null) {
                button.clickHandler().accept(event);
            }
        } else {
            // Clicks in player inventory while menu is open
            // Prevent ANY action that could move items between inventories
            if (event.isShiftClick() ||
                event.getAction() == InventoryAction.COLLECT_TO_CURSOR ||
                event.getAction() == InventoryAction.HOTBAR_SWAP ||
                event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MenuWindow window && window.getContent() instanceof AnvilMenu anvil) {
            String text = event.getView().getRenameText();
            anvil.handleRename(text != null ? text : "");
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
            menu.unregisterWindow(window);
            if (menu.getCloseHandler() != null) {
                menu.getCloseHandler().accept(event);
            }
        }
    }
}
