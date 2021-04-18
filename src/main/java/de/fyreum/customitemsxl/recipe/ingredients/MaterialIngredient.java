package de.fyreum.customitemsxl.recipe.ingredients;

import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

public class MaterialIngredient implements IRecipeIngredient {

    private final String serialized;
    private final RecipeChoice choice;

    public MaterialIngredient(Material material) {
        this.serialized = material.name();
        this.choice = new RecipeChoice.MaterialChoice(material);
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
