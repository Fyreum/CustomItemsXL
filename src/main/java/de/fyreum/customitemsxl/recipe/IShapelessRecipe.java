package de.fyreum.customitemsxl.recipe;

import de.fyreum.customitemsxl.recipe.ingredients.IRecipeIngredient;
import de.fyreum.customitemsxl.recipe.result.RecipeResult;
import de.fyreum.customitemsxl.serialization.RecipeFactory;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IShapelessRecipe extends ShapelessRecipe implements IRecipe {

    private final RecipeResult result;
    private final int amount;
    private final Map<IRecipeIngredient, Integer> ingredients;

    public IShapelessRecipe(NamespacedKey key,  @NotNull RecipeResult result, @NotNull Map<IRecipeIngredient, Integer> ingredients) {
        this(key, result, ingredients, "");
    }

    public IShapelessRecipe(NamespacedKey key, @NotNull RecipeResult result,  @NotNull Map<IRecipeIngredient, Integer> ingredients, String group) {
        super(key, result.getItemStack());
        this.result = result;
        this.amount = result.getItemStack().getAmount();
        this.ingredients = ingredients;
        ingredients.forEach(this::addIngredients);
        this.setGroup(group);
    }

    private void addIngredients(IRecipeIngredient choice, int count) {
        while (count-- > 0) {
            this.addIngredient(choice.getRecipeChoice());
        }
    }

    @Override
    public RecipeResult getRecipeResult() {
        return result;
    }

    @NotNull
    public Map<IRecipeIngredient, Integer> getIngredients() {
        return ingredients;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        if (getGroup().isEmpty()) {
            return "[result=" + result.getStringKey() + " x " + amount + "; " + RecipeFactory.toIngredientString(ingredients) + "]";
        } else {
            return "[result=" + result.getStringKey() + " x " + amount + "; " + RecipeFactory.toIngredientString(ingredients) + ";group=" + getGroup() + "]";
        }
    }
}
