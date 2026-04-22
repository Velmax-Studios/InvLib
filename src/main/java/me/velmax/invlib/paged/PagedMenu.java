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
            setButton(slot, null);
        }

        int startIdx = currentPage * contentSlots.size();
        for (int i = 0; i < contentSlots.size(); i++) {
            int buttonIdx = startIdx + i;
            if (buttonIdx >= contentButtons.size()) break;

            setButton(contentSlots.get(i), contentButtons.get(buttonIdx));
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPages() {
        if (contentSlots.isEmpty()) return 0;
        return (int) Math.ceil((double) contentButtons.size() / contentSlots.size());
    }

    public void nextPage() {
        if (currentPage < getMaxPages() - 1) {
            currentPage++;
            update();
            updateAll();
        }
    }

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
