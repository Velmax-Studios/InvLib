package me.velmax.invlib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a visual mapping of slots in an inventory.
 * 
 * <p>Layouts allow developers to design menus using string patterns, where each
 * unique character represents a specific slot or group of slots.</p>
 */
public final class Layout {

    private final String[] rows;
    private final Map<Character, MenuButton> ingredients = new HashMap<>();

    private Layout(String... rows) {
        this.rows = rows;
        validate();
    }

    /**
     * Creates a new layout with the given row patterns.
     *
     * @param rows The row patterns.
     * @return A new Layout instance.
     */
    public static @NotNull Layout create(@NotNull String... rows) {
        return new Layout(rows);
    }

    /**
     * Binds a character in the pattern to a {@link MenuButton}.
     *
     * @param key    The character key.
     * @param button The button to bind.
     * @return This Layout instance.
     */
    public @NotNull Layout bind(char key, @Nullable MenuButton button) {
        if (button == null) {
            ingredients.remove(key);
        } else {
            ingredients.put(key, button);
        }
        return this;
    }

    /**
     * Applies this layout to a {@link BaseMenu}.
     *
     * @param menu The menu to apply to.
     */
    public void apply(@NotNull BaseMenu menu) {
        int width = rows[0].length();
        for (int r = 0; r < rows.length; r++) {
            String row = rows[r];
            for (int c = 0; c < row.length(); c++) {
                char key = row.charAt(c);
                MenuButton button = ingredients.get(key);
                if (button != null) {
                    menu.setButton(r * width + c, button);
                }
            }
        }
    }

    private void validate() {
        if (rows.length == 0) {
            throw new IllegalArgumentException("Layout must have at least one row.");
        }
        int width = rows[0].length();
        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException("All rows in a layout must have the same length.");
            }
        }
    }

    public int getRows() {
        return rows.length;
    }

    public int getColumns() {
        return rows[0].length();
    }
}
