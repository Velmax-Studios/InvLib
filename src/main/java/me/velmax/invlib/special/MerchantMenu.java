package me.velmax.invlib.special;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.MenuWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized menu for virtual merchant (villager) trading.
 */
public class MerchantMenu extends BaseMenu {

    private final List<MerchantRecipe> trades = new ArrayList<>();
    private final Component title;

    public MerchantMenu(@NotNull Component title) {
        super(InventoryType.MERCHANT);
        this.title = title;
    }

    /**
     * Adds a trade to the merchant menu.
     *
     * @param recipe The merchant recipe to add.
     */
    public void addTrade(@NotNull MerchantRecipe recipe) {
        trades.add(recipe);
    }

    /**
     * Adds a trade to the merchant menu with ingredients.
     *
     * @param recipe      The merchant recipe.
     * @param ingredients The ingredients for the trade.
     */
    public void addTrade(@NotNull MerchantRecipe recipe, @NotNull ItemStack... ingredients) {
        recipe.setIngredients(List.of(ingredients));
        addTrade(recipe);
    }

    /**
     * Clears all trades from the merchant menu.
     */
    public void clearTrades() {
        trades.clear();
    }

    /**
     * Gets the current list of trades.
     *
     * @return The trades.
     */
    public List<MerchantRecipe> getTrades() {
        return trades;
    }

    @Override
    public void open(@NotNull Player player, @NotNull Component title) {
        Merchant merchant = Bukkit.createMerchant(title);
        merchant.setRecipes(trades);
        player.openMerchant(merchant, true);
    }

    /**
     * Opens the merchant menu with the predefined title.
     *
     * @param player The player.
     */
    public void open(@NotNull Player player) {
        open(player, title);
    }
}
