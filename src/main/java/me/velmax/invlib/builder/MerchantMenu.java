package me.velmax.invlib.builder;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.InvLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized menu for virtual Merchant trades.
 */
public final class MerchantMenu extends BaseMenu {

    private Merchant merchant;
    private final List<MerchantRecipe> recipes = new ArrayList<>();

    public MerchantMenu() {
        super(InventoryType.MERCHANT);
    }

    public void addTrade(@NotNull MerchantRecipe recipe) {
        recipes.add(recipe);
        if (merchant != null) {
            merchant.setRecipes(recipes);
        }
    }

    @Override
    public void open(@NotNull Player player, @NotNull Component title) {
        this.merchant = Bukkit.createMerchant(title);
        merchant.setRecipes(recipes);
        player.openMerchant(merchant, true);
    }

    public @Nullable Merchant getMerchant() {
        return merchant;
    }
}
