package me.velmax.invlib.special;

import me.velmax.invlib.BaseMenu;
import org.bukkit.event.inventory.InventoryType;

/**
 * A specialized menu for 3x3 crafting grids.
 */
public class CraftingMenu extends BaseMenu {

    public CraftingMenu() {
        super(InventoryType.WORKBENCH);
    }

    /**
     * Sets a button in the crafting grid.
     *
     * @param row    The row (0-2).
     * @param col    The column (0-2).
     * @param button The button.
     */
    public void setGridButton(int row, int col, me.velmax.invlib.MenuButton button) {
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            throw new IllegalArgumentException("Grid coordinates must be between 0 and 2.");
        }
        setButton(row * 3 + col + 1, button);
    }

    /**
     * Sets the result button.
     *
     * @param button The button.
     */
    public void setResultButton(me.velmax.invlib.MenuButton button) {
        setButton(0, button);
    }
}
