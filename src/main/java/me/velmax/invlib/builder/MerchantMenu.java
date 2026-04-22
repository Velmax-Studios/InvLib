package me.velmax.invlib.builder;

import me.velmax.invlib.BaseMenu;
import me.velmax.invlib.InvLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized menu for virtual Merchant trades.
 */
public final class MerchantMenu extends BaseMenu {

    private final Merchant merchant;
    private final List<MerchantRecipe> recipes = new ArrayList<>();

    public MerchantMenu(@NotNull Component title) {
        // Merchant doesn't use a normal container inventory for the holder,
        // but we can wrap it to keep the API consistent.
        super(3, title); // Dummy size
        this.merchant = Bukkit.createMerchant(title);
    }

    public void addTrade(@NotNull MerchantRecipe recipe) {
        recipes.add(recipe);
        merchant.setRecipes(recipes);
    }

    @Override
    public void open(@NotNull Player player) {
        player.openMerchant(merchant, true);
    }

    public @NotNull Merchant getMerchant() {
        return merchant;
    }
}
