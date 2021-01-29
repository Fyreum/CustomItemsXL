package de.fyreum.customitemsxl.serialization.file;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.serialization.roots.ItemRoot;
import de.fyreum.customitemsxl.serialization.roots.RecipeRoot;
import de.fyreum.customitemsxl.serialization.roots.Root;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RootFileManager {

    private final Set<RootFile<Root<ItemStack>>> itemRootFiles;
    private final Set<RootFile<Root<IRecipe>>> recipeRootFiles;

    public RootFileManager() {
        this.itemRootFiles = new HashSet<>();
        this.recipeRootFiles = new HashSet<>();
    }

    public void loadItems(File items) {
        CustomItemsXL.LOGGER.info("Loading items...");
        File[] files = items.listFiles();
        for (int i = 0; i < files.length; i++) {
            itemRootFiles.add(new ItemRootFile(files[i]));
            if (i == files.length - 1) {
                CustomItemsXL.LOGGER.info("Loaded " + files.length + " files, total items: " + getItems().size());
            }
        }
    }

    public void loadRecipes(File recipes) {
        CustomItemsXL.LOGGER.info("Loading recipes...");
        File[] files = recipes.listFiles();
        for (int i = 0; i < files.length; i++) {
            recipeRootFiles.add(new RecipeRootFile(files[i]));
            if (i == files.length - 1) {
                CustomItemsXL.LOGGER.info("Loaded " + files.length + " files, total recipes: " + getRecipes().size());
            }
        }
    }

    public void saveRoots() {
        CustomItemsXL.LOGGER.info("Saving files...");
        for (RootFile<Root<ItemStack>> file : itemRootFiles) {
            file.reload();
        }
        for (RootFile<Root<IRecipe>> file : recipeRootFiles) {
            file.reload();
        }
    }

    public Root<ItemStack> addItem(String fileName, String key, ItemStack item) {
        RootFile<Root<ItemStack>> file = getMatchingItemRootFile(fileName);
        if (file == null) {
            file = new ItemRootFile(new File(CustomItemsXL.ITEMS, fileName));
        }
        ItemRoot root = new ItemRoot(key, item);
        file.addRoot(root);
        itemRootFiles.add(file);
        return root;
    }

    public Root<IRecipe> addRecipe(String fileName, String key, IRecipe recipe) {
        RootFile<Root<IRecipe>> file = getMatchingRecipeRootFile(fileName);
        if (file == null) {
            file = new RecipeRootFile(new File(CustomItemsXL.RECIPES, fileName));
        }

        file.remove(key);

        RecipeRoot root = new RecipeRoot(key, recipe);
        file.addRoot(root);
        recipeRootFiles.add(file);
        return root;
    }

    @Nullable
    public RootFile<Root<ItemStack>> getMatchingItemRootFile(String fileName) {
        for (RootFile<Root<ItemStack>> file : itemRootFiles) {
            if (file.getFile().getName().equalsIgnoreCase(fileName)) {
                return file;
            }
        }
        return null;
    }

    @Nullable
    public RootFile<Root<IRecipe>> getMatchingRecipeRootFile(String fileName) {
        for (RootFile<Root<IRecipe>> file : recipeRootFiles) {
            if (file.getFile().getName().equalsIgnoreCase(fileName)) {
                return file;
            }
        }
        return null;
    }

    @Nullable
    public Root<ItemStack> getMatchingItemRoot(String id) {
        for (Root<ItemStack> root : getItemRoots()) {
            if (root.getId().equalsIgnoreCase(id)) {
                return root;
            }
        }
        return null;
    }

    @Nullable
    public Root<IRecipe> getMatchingRecipeRoot(String id) {
        for (Root<IRecipe> root : getRecipeRoots()) {
            if (root.getId().equalsIgnoreCase(id)) {
                return root;
            }
        }
        return null;
    }

    @Nullable
    public ItemStack getMatchingItem(String id) {
        Root<ItemStack> root = getMatchingItemRoot(id);
        if (root == null) {
            return null;
        }
        return root.getDeserialized();
    }

    @Nullable
    public IRecipe getMatchingRecipe(String id) {
        Root<IRecipe> root = getMatchingRecipeRoot(id);
        if (root == null) {
            return null;
        }
        return root.getDeserialized();
    }

    public Set<RootFile<Root<ItemStack>>> getItemRootFiles() {
        return itemRootFiles;
    }

    public Set<RootFile<Root<IRecipe>>> getRecipeRootFiles() {
        return recipeRootFiles;
    }

    public Set<Root<ItemStack>> getItemRoots() {
        Set<Root<ItemStack>> roots = new HashSet<>();
        for (RootFile<Root<ItemStack>> file : itemRootFiles) {
            roots.addAll(file.getRoots());
        }
        return roots;
    }

    public Set<Root<IRecipe>> getRecipeRoots() {
        Set<Root<IRecipe>> roots = new HashSet<>();
        for (RootFile<Root<IRecipe>> file : recipeRootFiles) {
            roots.addAll(file.getRoots());
        }
        return roots;
    }

    public Set<ItemStack> getItems() {
        Set<ItemStack> items = new HashSet<>();
        for (Root<ItemStack> file : getItemRoots()) {
            items.add(file.getDeserialized());
        }
        return items;
    }

    public Set<IRecipe> getRecipes() {
        Set<IRecipe> items = new HashSet<>();
        for (Root<IRecipe> file : getRecipeRoots()) {
            items.add(file.getDeserialized());
        }
        return items;
    }
}
