package de.fyreum.customitemsxl.filter;

import org.bukkit.inventory.ItemStack;

public interface FilteredSubject {

    boolean isTarget(ItemStack item);

    void filter(ItemStack item);

}
