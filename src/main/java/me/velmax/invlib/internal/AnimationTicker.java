package me.velmax.invlib.internal;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuButton;
import me.velmax.invlib.MenuWindow;
import me.velmax.invlib.animation.AnimatedButton;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A global ticker that handles animations for all active menus.
 */
public class AnimationTicker extends BukkitRunnable {

    private long tick = 0;

    @Override
    public void run() {
        tick++;
        
        for (BaseMenu menu : BaseMenu.getAllMenus()) {
            // Optimization: Only tick if the menu has active viewers
            if (menu.getActiveWindows().isEmpty()) {
                continue;
            }
            
            // org.bukkit.Bukkit.getLogger().info("Ticking menu with " + menu.getActiveWindows().size() + " viewers");
            
            menu.getRawButtons().forEach((slot, button) -> {
                if (button instanceof AnimatedButton animated) {
                    animated.update(tick);
                    
                    // Update the slot for all viewers
                    for (MenuWindow window : menu.getActiveWindows()) {
                        if (window.getInventory() != null) {
                            window.getInventory().setItem(slot, animated.item());
                        }
                    }
                }
            });
        }
    }
}
