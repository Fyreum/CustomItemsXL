package de.fyreum.customitemsxl.filter;

import org.bukkit.inventory.ItemStack;

public class FilteredItemStack implements FilteredSubject {

    private final ItemStack target;
    private final boolean delete;

    public FilteredItemStack(final ItemStack target, final boolean delete) {
        this.target = target;
        this.delete = delete;
    }

    @Override
    public boolean isTarget(ItemStack item) {
        return target.isSimilar(item);
    }

    @Override
    public void filter(ItemStack item) {
        if (delete) {

        } else {

        }
    }
}
