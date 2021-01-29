package de.fyreum.customitemsxl.filter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilteredItemStack implements FilteredSubject {

    private final ItemStack from;
    private final ItemStack to;
    private Material materialTo = null;

    public FilteredItemStack(final @NotNull ItemStack from, final @Nullable ItemStack to) {
        this.from = from;
        this.to = to;
    }

    public FilteredItemStack(final @NotNull ItemStack from, final @Nullable Material to) {
        this.from = from;
        this.to = null;
        this.materialTo = to;
    }

    @Override
    public boolean isTarget(ItemStack item) {
        return from.isSimilar(item);
    }

    @Override
    public void filter(ItemStack item) {
        if (to == null) {
            if (materialTo != null) {
                item.setType(materialTo);
                return;
            }
            item.setType(Material.AIR);
        } else {
            item.setType(to.getType());
            item.setItemMeta(to.getItemMeta());
        }
    }
}
