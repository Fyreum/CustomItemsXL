package de.fyreum.customitemsxl.recipe.ingredients;

import de.fyreum.customitemsxl.serialization.roots.Root;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class ItemIngredient implements IRecipeIngredient {

    private final String serialized;
    private final RecipeChoice choice;

    public ItemIngredient(Root<ItemStack> root) {
        this.serialized = root.getId();
        this.choice = new RecipeChoice.ExactChoice(root.getDeserialized());
    }

    @Override
    public RecipeChoice getRecipeChoice() {
        return choice;
    }

    @Override
    public String getSerialized() {
        return serialized;
    }

    @Override
    public String toString() {
        return serialized;
    }
}
