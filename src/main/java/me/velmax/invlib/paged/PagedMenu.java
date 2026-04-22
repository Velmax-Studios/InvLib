package me.velmax.invlib.paged;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuButton;
import me.velmax.invlib.MenuWindow;
import me.velmax.invlib.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A menu that supports multiple pages of content.
 */
public class PagedMenu extends BaseMenu {

    private final List<MenuButton> contentButtons = new ArrayList<>();
    private final List<Integer> contentSlots;
    private int currentPage = 0;

    /**
     * Creates a new PagedMenu.
     *
     * @param size         The size of the inventory (rows or total slots).
     * @param contentSlots The slots where paged content should be displayed.
     */
    public PagedMenu(int size, @NotNull List<Integer> contentSlots) {
        super(size);
        this.contentSlots = contentSlots;
    }

    /**
     * Adds a button to the paged content.
     *
     * @param button The button to add.
     */
    public void addContent(@NotNull MenuButton button) {
        contentButtons.add(button);
        update();
    }

    /**
     * Updates the menu content for the current page.
     */
    public void update() {
        // Clear only content slots
        for (int slot : contentSlots) {
            setButton(slot, (MenuButton) null);
        }

        int startIdx = currentPage * contentSlots.size();
        for (int i = 0; i < contentSlots.size(); i++) {
            int buttonIdx = startIdx + i;
            if (buttonIdx >= contentButtons.size()) break;

            setButton(contentSlots.get(i), contentButtons.get(buttonIdx));
        }
    }

    /**
     * Gets the current page index.
     *
     * @return The current page (0-indexed).
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Gets the total number of pages.
     *
     * @return The max pages.
     */
    public int getMaxPages() {
        if (contentSlots.isEmpty()) return 0;
        return (int) Math.ceil((double) contentButtons.size() / contentSlots.size());
    }

    /**
     * Switches to the next page if possible.
     */
    public void nextPage() {
        if (currentPage < getMaxPages() - 1) {
            currentPage++;
            update();
            updateAll();
        }
    }

    /**
     * Switches to the previous page if possible.
     */
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            update();
            updateAll();
        }
    }

    /**
     * Creates a standard next page button.
     *
     * @return The next page button.
     */
    public @NotNull MenuButton createNextPageButton() {
        return MenuButton.of(
            ItemBuilder.start(org.bukkit.Material.ARROW)
                .name(Component.text("Next Page", net.kyori.adventure.text.format.NamedTextColor.GREEN))
                .build(),
            event -> nextPage()
        );
    }

    /**
     * Creates a standard previous page button.
     *
     * @return The previous page button.
     */
    public @NotNull MenuButton createPreviousPageButton() {
        return MenuButton.of(
            ItemBuilder.start(org.bukkit.Material.ARROW)
                .name(Component.text("Previous Page", net.kyori.adventure.text.format.NamedTextColor.GREEN))
                .build(),
            event -> previousPage()
        );
    }
}
