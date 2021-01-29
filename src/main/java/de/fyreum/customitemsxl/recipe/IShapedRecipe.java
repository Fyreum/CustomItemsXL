package de.fyreum.customitemsxl.recipe;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.recipe.ingredients.IRecipeIngredient;
import de.fyreum.customitemsxl.recipe.result.RecipeResult;
import de.fyreum.customitemsxl.serialization.RecipeFactory;
import de.fyreum.customitemsxl.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IShapedRecipe extends ShapedRecipe implements IRecipe {

    private final RecipeResult result;
    private final int amount;
    private final Map<Character, IRecipeIngredient> ingredients;

    public IShapedRecipe(NamespacedKey key, RecipeResult result, String[] shape, @NotNull Map<Character, IRecipeIngredient> ingredients) {
        this(key, result, shape, ingredients, "");
    }

    public IShapedRecipe(NamespacedKey key, RecipeResult result, String[] shape, @NotNull Map<Character, IRecipeIngredient> ingredients, String group) {
        super(key, result.getItemStack());
        CustomItemsXL.LOGGER.debug(DebugMode.EXTENDED, "Creating IShapedRecipe...");
        this.result = result;
        this.amount = result.getItemStack().getAmount();
        this.shape(shape);
        this.ingredients = ingredients;
        CustomItemsXL.LOGGER.debug(DebugMode.EXTENDED, "Ingredients: " + ingredients.toString());
        ingredients.forEach((c, i) -> this.setIngredient(c, i.getRecipeChoice()));
        this.setGroup(group);
    }

    @Override
    public RecipeResult getRecipeResult() {
        return result;
    }

    @NotNull
    public Map<Character, IRecipeIngredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        if (getGroup().isEmpty()) {
            return "[result=" + result.getStringKey() + " x " + amount + ";shape=" + Util.toShapeString(getShape()) + ";" + "ingredients=" + RecipeFactory.toString(ingredients) + "]";
        } else {
            return "[result=" + result.getStringKey() + " x " + amount + ";shape=" + Util.toShapeString(getShape()) + ";" + "ingredients=" + RecipeFactory.toString(ingredients) + ";group=" + getGroup() + "]";
        }
    }
}
