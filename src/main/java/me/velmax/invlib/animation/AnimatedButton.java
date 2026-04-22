package me.velmax.invlib.animation;

import me.velmax.invlib.MenuButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A specialized button that uses an animation for its appearance.
 */
public class AnimatedButton extends MenuButton {

    private final ItemAnimation animation;

    public AnimatedButton(@NotNull ItemAnimation animation) {
        this(animation, null);
    }

    public AnimatedButton(@NotNull ItemAnimation animation, @Nullable Consumer<InventoryClickEvent> clickHandler) {
        super(animation.getFrame(0), clickHandler);
        this.animation = animation;
    }

    /**
     * Gets the animation used by this button.
     *
     * @return The animation.
     */
    public @NotNull ItemAnimation getAnimation() {
        return animation;
    }

    /**
     * Updates the button's item based on the current tick.
     *
     * @param tick The current tick.
     */
    public void update(long tick) {
        setItem(animation.getFrame(tick));
    }
}
