package de.fyreum.customitemsxl.recipe.result;

import de.fyreum.customitemsxl.util.StringKeyed;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RecipeResult implements StringKeyed {

    private final ItemStack item;
    private final String key;

    public RecipeResult(ItemStack item, String key) {
        this.item = item;
        this.key = key;
    }

    public RecipeResult(Material m, int amount) {
        this.item = new ItemStack(m, amount);
        this.key = m.name();
    }

    @NotNull
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public String getStringKey() {
        return key;
    }
}
