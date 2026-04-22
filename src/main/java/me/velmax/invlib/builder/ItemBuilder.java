package me.velmax.invlib.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * A fluent API for building ItemStacks.
 */
public final class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item.clone();
    }

    public static @NotNull ItemBuilder start(@NotNull Material material) {
        return new ItemBuilder(material);
    }

    public static @NotNull ItemBuilder from(@NotNull ItemStack item) {
        return new ItemBuilder(item);
    }

    public @NotNull ItemBuilder name(@NotNull Component name) {
        editMeta(meta -> meta.displayName(name));
        return this;
    }

    public @NotNull ItemBuilder lore(@NotNull List<Component> lore) {
        editMeta(meta -> meta.lore(lore));
        return this;
    }

    public @NotNull ItemBuilder lore(@NotNull Component... lore) {
        editMeta(meta -> meta.lore(List.of(lore)));
        return this;
    }

    public @NotNull ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public @NotNull ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public @NotNull ItemBuilder flags(@NotNull ItemFlag... flags) {
        editMeta(meta -> meta.addItemFlags(flags));
        return this;
    }

    public @NotNull ItemBuilder customModelData(@NotNull Integer data) {
        editMeta(meta -> meta.setCustomModelData(data));
        return this;
    }

    public @NotNull ItemBuilder editMeta(@NotNull Consumer<ItemMeta> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            consumer.accept(meta);
            item.setItemMeta(meta);
        }
        return this;
    }

    public @NotNull ItemStack build() {
        return item.clone();
    }
}
