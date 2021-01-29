package de.fyreum.customitemsxl.serialization.file;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.serialization.roots.RecipeRoot;
import de.fyreum.customitemsxl.serialization.roots.Root;

import java.io.File;

public class RecipeRootFile extends RootFile<Root<IRecipe>> {

    public RecipeRootFile(File file) {
        super(file);
        CustomItemsXL.LOGGER.debug(DebugMode.LOW, "Loading '" + file.getPath());
        contents.forEach((key, object) -> {
            if (!(object instanceof String)) {
                CustomItemsXL.LOGGER.warn("Found non string object at '" + key + "', continuing by ignoring it");
                return;
            }
            try {
                Root<IRecipe> root = new RecipeRoot(key, CustomItemsXL.key(key), (String) object);
                this.roots.add(root);
            } catch (ConfigSerializationError e) {
                CustomItemsXL.LOGGER.error(file.getPath(), key, e.getMessage());
            }
        });
    }
}
