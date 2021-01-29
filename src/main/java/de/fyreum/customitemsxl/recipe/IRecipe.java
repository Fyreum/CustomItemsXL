package de.fyreum.customitemsxl.recipe;

import de.fyreum.customitemsxl.recipe.result.RecipeResult;
import org.bukkit.inventory.Recipe;

public interface IRecipe extends Recipe {

    RecipeResult getRecipeResult();

    int getAmount();

}
