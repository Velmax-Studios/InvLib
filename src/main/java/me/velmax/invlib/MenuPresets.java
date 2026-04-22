package me.velmax.invlib;

import me.velmax.invlib.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Common menu button presets.
 */
public final class MenuPresets {

    private MenuPresets() {
        throw new UnsupportedOperationException();
    }

    /**
     * A button that closes the inventory.
     */
    public static @NotNull MenuButton close() {
        return MenuButton.of(
            ItemBuilder.start(Material.BARRIER)
                .name(Component.text("Close", NamedTextColor.RED))
                .build(),
            event -> event.getWhoClicked().closeInventory()
        );
    }

    /**
     * A button that opens another menu.
     *
     * @param target The target menu.
     * @param title  The title of the target menu.
     * @return The back button.
     */
    public static @NotNull MenuButton back(@NotNull BaseMenu target, @NotNull Component title) {
        return MenuButton.of(
            ItemBuilder.start(Material.ARROW)
                .name(Component.text("Back", NamedTextColor.YELLOW))
                .build(),
            event -> target.open((Player) event.getWhoClicked(), title)
        );
    }

    /**
     * A generic navigation button.
     *
     * @param material The material.
     * @param name     The name.
     * @param action   The action to perform.
     * @return The button.
     */
    public static @NotNull MenuButton nav(@NotNull Material material, @NotNull Component name, @NotNull Consumer<Player> action) {
        return MenuButton.of(
            ItemBuilder.start(material).name(name).build(),
            event -> action.accept((Player) event.getWhoClicked())
        );
    }
}
