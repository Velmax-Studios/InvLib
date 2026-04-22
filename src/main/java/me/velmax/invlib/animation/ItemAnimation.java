package me.velmax.invlib.animation;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an animation for an item in a menu.
 */
@FunctionalInterface
public interface ItemAnimation {

    /**
     * Gets the frame for the given tick.
     *
     * @param tick The current tick.
     * @return The ItemStack for this frame.
     */
    @NotNull ItemStack getFrame(long tick);

    /**
     * Creates a simple frame-by-frame animation.
     *
     * @param delay  The delay between frames in ticks.
     * @param frames The list of frames.
     * @return A new ItemAnimation instance.
     */
    static @NotNull ItemAnimation frames(int delay, @NotNull List<ItemStack> frames) {
        return tick -> frames.get((int) ((tick / delay) % frames.size()));
    }
}
