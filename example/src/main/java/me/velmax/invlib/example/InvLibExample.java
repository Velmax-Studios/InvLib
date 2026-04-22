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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.SkullMeta;
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
            case "confirm" -> openConfirmationDemo(player);
            case "settings" -> openSettingsDemo(player);
            case "players" -> openPlayerListDemo(player);
            case "shop" -> openShopDemo(player);
            case "profile" -> openProfileDemo(player);
            default -> player.sendMessage(mm.deserialize("<red>Unknown subcommand. Use /invlib for the main menu."));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("performance", "animate", "input", "trade", "craft", "confirm", "settings", "players", "shop", "profile").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * MAIN MENU: Showcases Layouts and Button Binding
     */
    private void openMainMenu(Player player) {
        String titleStr = getConfig().getString("main-menu.title", "<gradient:#4e54c8:#8f94fb><bold>InvLib Showcase");
        Material fillerMat = Material.valueOf(getConfig().getString("main-menu.filler-material", "GRAY_STAINED_GLASS_PANE"));

        MenuBuilder.chest(6)
                .title(mm.deserialize(titleStr))
                .layout(
                        "FFFFFFFFF",
                        "F.P.A.I.F",
                        "F.......F",
                        "F.T.C.Y.F",
                        "F.......F",
                        "FS.R.L.XF"
                )
                .bind('F', MenuPresets.filler(fillerMat))
                .bind('P', ItemBuilder.start(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                        .name(mm.deserialize("<green><bold>Performance Test"))
                        .lore(mm.deserialize("<gray>Explore all Minecraft items"), mm.deserialize("<gray>in a paged browser."))
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
                .bind('Y', ItemBuilder.start(Material.LIME_CONCRETE)
                        .name(mm.deserialize("<green><bold>Confirmation Dialog"))
                        .lore(mm.deserialize("<gray>Simple Yes/No example."))
                        .build(), e -> openConfirmationDemo(player))
                .bind('S', ItemBuilder.start(Material.COMPARATOR)
                        .name(mm.deserialize("<blue><bold>Settings Menu"))
                        .lore(mm.deserialize("<gray>Interactive toggle examples."))
                        .build(), e -> openSettingsDemo(player))
                .bind('R', ItemBuilder.start(Material.PLAYER_HEAD)
                        .name(mm.deserialize("<aqua><bold>Player Browser"))
                        .lore(mm.deserialize("<gray>Paged menu of online players."))
                        .build(), e -> openPlayerListDemo(player))
                .bind('L', ItemBuilder.start(Material.CHEST)
                        .name(mm.deserialize("<gold><bold>Shop Categories"))
                        .lore(mm.deserialize("<gray>Menu navigation example."))
                        .build(), e -> openShopDemo(player))
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
                .title(mm.deserialize("<gradient:#ff00ff:#00ffff><bold>Animation Gallery"))
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
        
        crafting.open(player, mm.deserialize("<gradient:gold:yellow>Virtual Crafting Table"));
    }

    /**
     * CONFIRMATION DEMO: Simple Yes/No
     */
    private void openConfirmationDemo(Player player) {
        MenuBuilder.chest(3)
                .title(mm.deserialize("<red>Are you sure?"))
                .layout(
                        ".........",
                        "..Y...N..",
                        "........."
                )
                .bind('Y', ItemBuilder.start(Material.LIME_WOOL)
                        .name(mm.deserialize("<green><bold>CONFIRM"))
                        .lore(mm.deserialize("<gray>Click to proceed."))
                        .build(), e -> {
                            player.sendMessage(mm.deserialize("<green>Action confirmed!"));
                            player.closeInventory();
                        })
                .bind('N', ItemBuilder.start(Material.RED_WOOL)
                        .name(mm.deserialize("<red><bold>CANCEL"))
                        .lore(mm.deserialize("<gray>Click to go back."))
                        .build(), e -> openMainMenu(player))
                .open(player);
    }

    /**
     * SETTINGS DEMO: Toggles
     */
    private void openSettingsDemo(Player player) {
        MenuBuilder.chest(3)
                .title(mm.deserialize("<blue>Personal Settings"))
                .layout(
                        "FSFSFSFSF",
                        "S.A.B.C.S",
                        "FSFSFSFSF"
                )
                .bind('F', MenuPresets.filler(Material.CYAN_STAINED_GLASS_PANE))
                .bind('S', MenuPresets.filler(Material.BLUE_STAINED_GLASS_PANE))
                .bind('A', ItemBuilder.start(Material.NOTE_BLOCK)
                        .name(mm.deserialize("<yellow>Sound Effects: <green>ON"))
                        .build(), e -> player.sendActionBar(mm.deserialize("<yellow>Sounds toggled!")))
                .bind('B', ItemBuilder.start(Material.BLAZE_POWDER)
                        .name(mm.deserialize("<light_purple>Particles: <green>ON"))
                        .build(), e -> player.sendActionBar(mm.deserialize("<light_purple>Particles toggled!")))
                .bind('C', ItemBuilder.start(Material.FEATHER)
                        .name(mm.deserialize("<aqua>Flight: <red>OFF"))
                        .build(), e -> player.sendActionBar(mm.deserialize("<aqua>Flight toggled!")))
                .open(player);
    }

    /**
     * PLAYER LIST: Paged head menu
     */
    private void openPlayerListDemo(Player player) {
        List<Integer> contentSlots = new ArrayList<>();
        for (int i = 0; i < 45; i++) contentSlots.add(i);

        PagedMenu paged = new PagedMenu(6, contentSlots);

        for (Player online : Bukkit.getOnlinePlayers()) {
            paged.addContent(MenuButton.of(
                ItemBuilder.start(Material.PLAYER_HEAD)
                    .name(mm.deserialize("<yellow>" + online.getName()))
                    .lore(mm.deserialize("<gray>Click to teleport!"))
                    .editMeta(meta -> ((SkullMeta) meta).setOwningPlayer(online))
                    .build(),
                e -> {
                    player.teleport(online.getLocation());
                    player.sendMessage(mm.deserialize("<green>Teleported to " + online.getName()));
                }
            ));
        }

        paged.setButton(45, paged.createPreviousPageButton());
        paged.setButton(49, ItemBuilder.start(Material.BARRIER).name(mm.deserialize("<red>Back")).build(), e -> openMainMenu(player));
        paged.setButton(53, paged.createNextPageButton());

        paged.open(player, mm.deserialize("<gradient:aqua:blue>Online Players <gray>(Page " + (paged.getCurrentPage() + 1) + ")"));
    }

    /**
     * SHOP DEMO: Categories
     */
    private void openShopDemo(Player player) {
        MenuBuilder.chest(3)
                .title(mm.deserialize("<gold>Select Category"))
                .layout(
                        "FFFFFFFFF",
                        "F.B.W.A.F",
                        "FFFFXFFFF"
                )
                .bind('F', MenuPresets.filler(Material.ORANGE_STAINED_GLASS_PANE))
                .bind('B', ItemBuilder.start(Material.GRASS_BLOCK).name(mm.deserialize("<green>Blocks")).build(), e -> player.sendMessage("Opening Blocks..."))
                .bind('W', ItemBuilder.start(Material.DIAMOND_SWORD).name(mm.deserialize("<red>Weapons")).build(), e -> player.sendMessage("Opening Weapons..."))
                .bind('A', ItemBuilder.start(Material.IRON_CHESTPLATE).name(mm.deserialize("<blue>Armor")).build(), e -> player.sendMessage("Opening Armor..."))
                .bind('X', ItemBuilder.start(Material.ARROW).name(mm.deserialize("<yellow>Back")).build(), e -> openMainMenu(player))
                .open(player);
    }

    /**
     * PROFILE DEMO: Stats Dashboard
     */
    private void openProfileDemo(Player player) {
        // This is triggered from a subcommand or can be added to main menu
        MenuBuilder.chest(5)
                .title(mm.deserialize("<light_purple>" + player.getName() + "'s Profile"))
                .layout(
                        "FFFFFFFFF",
                        "F.......F",
                        "F...H...F",
                        "F.S.L.K.F",
                        "FFFFFFFFF"
                )
                .bind('F', MenuPresets.filler(Material.PURPLE_STAINED_GLASS_PANE))
                .bind('H', ItemBuilder.start(Material.PLAYER_HEAD)
                        .name(mm.deserialize("<yellow><bold>" + player.getName()))
                        .editMeta(meta -> ((SkullMeta) meta).setOwningPlayer(player)))
                .bind('S', ItemBuilder.start(Material.EXPERIENCE_BOTTLE)
                        .name(mm.deserialize("<green>Level: <white>" + player.getLevel())))
                .bind('L', ItemBuilder.start(Material.HEART_OF_THE_SEA)
                        .name(mm.deserialize("<red>Health: <white>" + (int) player.getHealth())))
                .bind('K', ItemBuilder.start(Material.GOLDEN_SWORD)
                        .name(mm.deserialize("<gold>Kills: <white>1,234")))
                .open(player);
    }

    private String formatName(String name) {
        return Arrays.stream(name.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
