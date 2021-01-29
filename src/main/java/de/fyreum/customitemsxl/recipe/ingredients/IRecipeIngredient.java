package de.fyreum.customitemsxl.recipe.ingredients;

import org.bukkit.inventory.RecipeChoice;

/**
 * Represents an RecipeChoice for an {@link org.bukkit.inventory.Recipe}.
 *
 * @see ItemIngredient
 * @see MaterialIngredient
 */
public interface IRecipeIngredient {

    RecipeChoice getRecipeChoice();

    String getSerialized();

}
