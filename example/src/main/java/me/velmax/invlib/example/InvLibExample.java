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
import me.velmax.invlib.special.MerchantMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class InvLibExample extends JavaPlugin implements CommandExecutor, TabCompleter {

    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        // IMPORTANT: Initialize the library
        me.velmax.invlib.InvLib.init(this);
        
        getCommand("invlib").setExecutor(this);
        getCommand("invlib").setTabCompleter(this);
        
        getLogger().info("InvLib Showcase Plugin enabled! Use /invlib to begin.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            openMainMenu(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "performance" -> openPerformanceDemo(player);
            case "animate" -> openAnimationDemo(player);
            case "input" -> openAnvilDemo(player);
            case "trade" -> openMerchantDemo(player);
            case "craft" -> openCraftingDemo(player);
            default -> player.sendMessage(mm.deserialize("<red>Unknown subcommand. Use /invlib for the main menu."));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("performance", "animate", "input", "trade", "craft").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * MAIN MENU: Showcases Layouts and Button Binding
     */
    private void openMainMenu(Player player) {
        String titleStr = getConfig().getString("main-menu.title", "InvLib Showcase");
        Material fillerMat = Material.valueOf(getConfig().getString("main-menu.filler-material", "GRAY_STAINED_GLASS_PANE"));

        MenuBuilder.chest(3)
                .title(mm.deserialize(titleStr))
                .layout(
                        "FFFFFFFFF",
                        "F P A I T",
                        "FFFFXCFFF"
                )
                .bind('F', MenuPresets.filler(fillerMat))
                .bind('P', ItemBuilder.start(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                        .name(mm.deserialize("<green><bold>Performance Test"))
                        .lore(mm.deserialize("<gray>Opens a paged menu with all"), mm.deserialize("<gray>Minecraft items (800+)."))
                        .build(), e -> openPerformanceDemo(player))
                .bind('A', ItemBuilder.start(Material.MAGMA_CREAM)
                        .name(mm.deserialize("<light_purple><bold>Animation Gallery"))
                        .lore(mm.deserialize("<gray>Showcases complex global"), mm.deserialize("<gray>item animations."))
                        .build(), e -> openAnimationDemo(player))
                .bind('I', ItemBuilder.start(Material.NAME_TAG)
                        .name(mm.deserialize("<white><bold>Anvil Input"))
                        .lore(mm.deserialize("<gray>Test real-time text input"), mm.deserialize("<gray>handling via Anvils."))
                        .build(), e -> openAnvilDemo(player))
                .bind('T', ItemBuilder.start(Material.EMERALD)
                        .name(mm.deserialize("<gold><bold>Merchant Trades"))
                        .lore(mm.deserialize("<gray>Virtual villager trading"), mm.deserialize("<gray>without actual entities."))
                        .build(), e -> openMerchantDemo(player))
                .bind('C', ItemBuilder.start(Material.CRAFTING_TABLE)
                        .name(mm.deserialize("<yellow><bold>Virtual Crafting"))
                        .lore(mm.deserialize("<gray>A standalone 3x3 grid"), mm.deserialize("<gray>for custom crafting."))
                        .build(), e -> openCraftingDemo(player))
                .bind('X', MenuPresets.close())
                .open(player);
    }

    /**
     * PERFORMANCE DEMO: Paged menu with ALL items
     */
    private void openPerformanceDemo(Player player) {
        // Prepare content slots (everything except the bottom row for navigation)
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 45; i++) slots.add(i);

        PagedMenu paged = new PagedMenu(6, slots);
        
        // Add ALL non-air materials
        for (Material mat : Material.values()) {
            if (mat.isAir() || !mat.isItem()) continue;
            paged.addContent(MenuButton.of(ItemBuilder.start(mat)
                    .name(mm.deserialize("<aqua>" + formatName(mat.name())))
                    .lore(mm.deserialize("<gray>Internal Name: " + mat.name()))
                    .build()));
        }

        // Navigation
        paged.setButton(45, paged.createPreviousPageButton());
        paged.setButton(49, ItemBuilder.start(Material.BARRIER).name(mm.deserialize("<red>Back to Menu")).build(), e -> openMainMenu(player));
        paged.setButton(53, paged.createNextPageButton());

        paged.open(player, mm.deserialize("<dark_aqua>Item Browser <gray>(Page " + (paged.getCurrentPage() + 1) + ")"));
    }

    /**
     * ANIMATION DEMO: Complex global animations
     */
    private void openAnimationDemo(Player player) {
        // Rainbow border
        ItemAnimation rainbow = ItemAnimation.frames(5, Arrays.stream(Material.values())
                .filter(m -> m.name().contains("_STAINED_GLASS_PANE"))
                .map(ItemStack::new)
                .toList());

        // Pulsing core
        ItemAnimation pulse = ItemAnimation.frames(10, List.of(
                new ItemStack(Material.NETHER_STAR),
                new ItemStack(Material.BEACON),
                new ItemStack(Material.END_CRYSTAL)
        ));

        MenuBuilder.chest(5)
                .title(mm.deserialize("<gradient:blue:purple>Dynamic Animations"))
                .layout(
                        "RRRRRRRRR",
                        "R.......R",
                        "R...P...R",
                        "R.......R",
                        "RRRRXRRRR"
                )
                .bind('R', new AnimatedButton(rainbow))
                .bind('P', new AnimatedButton(pulse, e -> player.sendMessage(mm.deserialize("<gold>You clicked the pulsing core!"))))
                .bind('X', MenuPresets.close())
                .open(player);
    }

    /**
     * ANVIL DEMO: Text Input
     */
    private void openAnvilDemo(Player player) {
        AnvilMenu anvil = new AnvilMenu();
        
        anvil.setButton(AnvilMenu.SLOT_INPUT_LEFT, ItemBuilder.start(Material.PAPER)
                .name(mm.deserialize("<gray>Type something..."))
                .build());

        anvil.onRename(text -> {
            player.sendActionBar(mm.deserialize("<yellow>Current Input: <white>" + text));
        });

        anvil.onComplete((text, event) -> {
            player.sendMessage(mm.deserialize("<green>Submission Successful: <white>" + text));
            player.closeInventory();
        });

        anvil.open(player, mm.deserialize("Custom Input"));
    }

    /**
     * MERCHANT DEMO: Trading
     */
    private void openMerchantDemo(Player player) {
        MerchantMenu merchant = new MerchantMenu(mm.deserialize("<gold>Legendary Trader"));

        // Add some fun trades
        merchant.addTrade(new MerchantRecipe(new ItemStack(Material.DIAMOND, 1), 999), 
                new ItemStack(Material.DIRT, 64), new ItemStack(Material.GRAVEL, 64));
        
        merchant.addTrade(new MerchantRecipe(new ItemStack(Material.NETHERITE_INGOT, 1), 5),
                new ItemStack(Material.DIAMOND_BLOCK, 2));

        merchant.open(player);
    }

    /**
     * CRAFTING DEMO: Virtual 3x3
     */
    private void openCraftingDemo(Player player) {
        me.velmax.invlib.special.CraftingMenu crafting = new me.velmax.invlib.special.CraftingMenu();
        
        // Add a back button at slot 0 (outside the 3x3 grid)
        crafting.setButton(0, ItemBuilder.start(Material.BARRIER).name(mm.deserialize("<red>Back")).build(), e -> openMainMenu(player));
        
        crafting.open(player, mm.deserialize("Virtual Crafting Table"));
    }

    private String formatName(String name) {
        return Arrays.stream(name.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
