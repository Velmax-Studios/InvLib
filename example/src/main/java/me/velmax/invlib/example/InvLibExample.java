package me.velmax.invlib.example;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuButton;
import me.velmax.invlib.MenuPresets;
import me.velmax.invlib.animation.AnimatedButton;
import me.velmax.invlib.animation.ItemAnimation;
import me.velmax.invlib.builder.ItemBuilder;
import me.velmax.invlib.builder.MenuBuilder;
import me.velmax.invlib.paged.PagedMenu;
import me.velmax.invlib.special.AnvilMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class InvLibExample extends JavaPlugin implements CommandExecutor {

    private BaseMenu sharedCounterMenu;
    private final AtomicInteger sharedCounter = new AtomicInteger(0);

    @Override
    public void onEnable() {
        getCommand("invlib").setExecutor(this);
        
        // Initialize shared menu
        setupSharedMenu();
        
        getLogger().info("InvLib Example Plugin enabled!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        openMainMenu(player);
        return true;
    }

    private void openMainMenu(Player player) {
        MenuBuilder.chest(3)
                .title(Component.text("InvLib Showcase", NamedTextColor.DARK_AQUA, TextDecoration.BOLD))
                .layout(
                        "BBBBBBBBB",
                        "B L A P S",
                        "B . N . C",
                        "BBBBBBBBB"
                )
                .bind('B', MenuPresets.filler(Material.CYAN_STAINED_GLASS_PANE))
                .bind('L', new ItemBuilder(Material.MAP)
                        .name(Component.text("Layout & Builder Demo", NamedTextColor.YELLOW))
                        .lore(Component.text("Click to see string-based layouts in action", NamedTextColor.GRAY))
                        .build(), e -> openLayoutDemo(player))
                .bind('A', new ItemBuilder(Material.GLOWSTONE_DUST)
                        .name(Component.text("Animations Demo", NamedTextColor.LIGHT_PURPLE))
                        .lore(Component.text("Smooth, optimized item animations", NamedTextColor.GRAY))
                        .build(), e -> openAnimationDemo(player))
                .bind('P', new ItemBuilder(Material.PAPER)
                        .name(Component.text("Paged Menu Demo", NamedTextColor.GREEN))
                        .lore(Component.text("Browse large lists with ease", NamedTextColor.GRAY))
                        .build(), e -> openPagedDemo(player))
                .bind('S', new ItemBuilder(Material.COMPASS)
                        .name(Component.text("Shared Menu Demo", NamedTextColor.GOLD))
                        .lore(Component.text("Synchronized menu for multiple viewers", NamedTextColor.GRAY))
                        .build(), e -> sharedCounterMenu.open(player, Component.text("Shared Clicker")))
                .bind('N', new ItemBuilder(Material.ANVIL)
                        .name(Component.text("Anvil Demo", NamedTextColor.WHITE))
                        .lore(Component.text("Input text via Anvil", NamedTextColor.GRAY))
                        .build(), e -> openAnvilDemo(player))
                .bind('C', new ItemBuilder(Material.CRAFTING_TABLE)
                        .name(Component.text("Crafting Demo", NamedTextColor.GOLD))
                        .lore(Component.text("Virtual 3x3 crafting grid", NamedTextColor.GRAY))
                        .build(), e -> openCraftingDemo(player))
                .open(player);
    }

    private void openCraftingDemo(Player player) {
        me.velmax.invlib.special.CraftingMenu crafting = new me.velmax.invlib.special.CraftingMenu();
        crafting.setButton(0, new ItemBuilder(Material.BARRIER).name(Component.text("Back")).build(), e -> openMainMenu(player));
        crafting.open(player, Component.text("Virtual Crafting"));
    }

    private void openAnvilDemo(Player player) {
        AnvilMenu anvil = new AnvilMenu();
        anvil.onRename(text -> {
            player.sendMessage(Component.text("You are typing: " + text, NamedTextColor.GRAY));
        });
        anvil.onComplete((text, event) -> {
            player.sendMessage(Component.text("You finished with: " + text, NamedTextColor.GREEN));
            player.closeInventory();
        });
        anvil.setButton(AnvilMenu.SLOT_INPUT_LEFT, new ItemBuilder(Material.PAPER).name(Component.text("Type here...")).build());
        anvil.open(player, Component.text("Enter your name"));
    }

    private void openLayoutDemo(Player player) {
        MenuBuilder.chest(5)
                .title(Component.text("Visual Layouts"))
                .layout(
                        "GGGGGGGGG",
                        "G.......G",
                        "G..#.#..G",
                        "G.......G",
                        "GGGGXGGGG"
                )
                .bind('G', MenuPresets.filler(Material.GRAY_STAINED_GLASS_PANE))
                .bind('#', MenuPresets.filler(Material.IRON_BARS))
                .bind('X', MenuPresets.close())
                .open(player);
    }

    private void openAnimationDemo(Player player) {
        // Frame-based animation
        ItemAnimation rainbow = ItemAnimation.frames(10, List.of(
                new ItemStack(Material.RED_STAINED_GLASS_PANE),
                new ItemStack(Material.ORANGE_STAINED_GLASS_PANE),
                new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),
                new ItemStack(Material.GREEN_STAINED_GLASS_PANE),
                new ItemStack(Material.BLUE_STAINED_GLASS_PANE),
                new ItemStack(Material.PURPLE_STAINED_GLASS_PANE)
        ));

        MenuBuilder.chest(3)
                .title(Component.text("Animated Menu"))
                .layout(
                        "RRRRRRRRR",
                        "R...C...R",
                        "RRRRRRRRR"
                )
                .bind('R', new AnimatedButton(rainbow))
                .bind('C', new ItemBuilder(Material.BARRIER).name(Component.text("Back")).build(), e -> openMainMenu(player))
                .open(player);
    }

    private void openPagedDemo(Player player) {
        PagedMenu paged = new PagedMenu(6, List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34));
        
        // Fill with online players
        for (Player online : Bukkit.getOnlinePlayers()) {
            paged.addContent(MenuButton.of(new ItemBuilder(Material.PLAYER_HEAD)
                    .name(Component.text(online.getName(), NamedTextColor.AQUA))
                    .build()));
        }

        // Add some fillers
        paged.setButton(45, paged.createPreviousPageButton());
        paged.setButton(49, new ItemBuilder(Material.BARRIER).name(Component.text("Back")).build(), e -> openMainMenu(player));
        paged.setButton(53, paged.createNextPageButton());

        paged.open(player, Component.text("Online Players (Page " + (paged.getCurrentPage() + 1) + ")"));
    }

    private void setupSharedMenu() {
        sharedCounterMenu = new BaseMenu(27);
        updateSharedCounter();
    }

    private void updateSharedCounter() {
        sharedCounterMenu.setButton(13, new ItemBuilder(Material.GOLD_BLOCK)
                .name(Component.text("Shared Counter: " + sharedCounter.get(), NamedTextColor.GOLD))
                .lore(Component.text("Click to increment for everyone!", NamedTextColor.GRAY))
                .build(), e -> {
            sharedCounter.incrementAndGet();
            updateSharedCounter();
            sharedCounterMenu.updateAll(); // Refresh all viewers
        });
    }
}
