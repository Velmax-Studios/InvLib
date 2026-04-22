package me.velmax.invlib.special;

import me.velmax.invlib.BaseMenu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A specialized menu for Anvil-based input.
 */
public class AnvilMenu extends BaseMenu {

    private Consumer<String> renameHandler;
    private BiConsumer<String, InventoryClickEvent> completeHandler;
    private String renameText;

    public AnvilMenu() {
        super(InventoryType.ANVIL);
    }

    /**
     * Sets a handler that is called whenever the rename text changes.
     *
     * @param handler The handler.
     */
    public void onRename(@Nullable Consumer<String> handler) {
        this.renameHandler = handler;
    }

    /**
     * Sets a handler that is called when the result slot is clicked.
     *
     * @param handler The handler.
     */
    public void onComplete(@Nullable BiConsumer<String, InventoryClickEvent> handler) {
        this.completeHandler = handler;
    }

    /**
     * Internal method to handle rename events.
     *
     * @param text The new text.
     */
    public void handleRename(@NotNull String text) {
        this.renameText = text;
        if (renameHandler != null) {
            renameHandler.accept(text);
        }
    }

    /**
     * Gets the current rename text.
     *
     * @return The text.
     */
    public @Nullable String getRenameText() {
        return renameText;
    }

    public @Nullable BiConsumer<String, InventoryClickEvent> getCompleteHandler() {
        return completeHandler;
    }
}
