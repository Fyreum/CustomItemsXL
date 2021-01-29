package de.fyreum.customitemsxl.filter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilteredMaterial implements FilteredSubject {

    private final Material from;
    private final Material to;
    private ItemStack itemTo;

    public FilteredMaterial(final @NotNull Material from, final @Nullable Material to) {
        this.from = from;
        this.to = to;
    }

    public FilteredMaterial(final @NotNull Material from, @Nullable final ItemStack to) {
        this.from = from;
        this.to = null;
        this.itemTo = to;
    }

    @Override
    public boolean isTarget(ItemStack item) {
        return item.getType().equals(from);
    }

    @Override
    public void filter(ItemStack item) {
        if (to == null) {
            if (itemTo != null) {
                item.setType(item.getType());
                item.setItemMeta(item.getItemMeta());
                return;
            }
            item.setType(Material.AIR);
        } else {
            item.setType(to);
        }
    }
}
