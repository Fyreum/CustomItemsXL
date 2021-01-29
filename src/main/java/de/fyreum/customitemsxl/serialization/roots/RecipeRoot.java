package de.fyreum.customitemsxl.serialization.roots;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import org.bukkit.NamespacedKey;

public class RecipeRoot extends RootBase<IRecipe> {

    private String serialized;
    private IRecipe deserialized;

    public RecipeRoot(String id, NamespacedKey key, String serialized) throws ConfigSerializationError {
        super(id);
        this.serialized = serialized;
        this.deserialized = CustomItemsXL.inst().getRecipeFactory().deserialize(key, serialized);
    }

    public RecipeRoot(String id, IRecipe deserialized) {
        super(id);
        this.serialized = CustomItemsXL.inst().getRecipeFactory().serialize(deserialized);
        this.deserialized = deserialized;
    }

    @Override
    public String getSerialized() {
        return serialized;
    }

    @Override
    public String setSerialized(String s) {
        return this.serialized = s;
    }

    @Override
    public IRecipe getDeserialized() {
        return deserialized;
    }

    @Override
    public void setDeserialized(IRecipe deserialized) {
        this.deserialized = deserialized;
    }
}
