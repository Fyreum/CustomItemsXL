package de.fyreum.customitemsxl.filter;

import org.bukkit.inventory.ItemStack;

public class FilteredMaterial implements FilteredSubject {

    @Override
    public boolean isTarget(ItemStack item) {
        return false;
    }

    @Override
    public void filter(ItemStack item) {

    }
}
