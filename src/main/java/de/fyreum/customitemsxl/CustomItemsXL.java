package de.fyreum.customitemsxl;

import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.factionsxl.FactionsXL;
import de.fyreum.customitemsxl.command.CICommandCache;
import de.fyreum.customitemsxl.filter.FilterListener;
import de.fyreum.customitemsxl.local.ConfigSettings;
import de.fyreum.customitemsxl.local.FilterSettings;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.logger.ILogger;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.recipe.IShapedRecipe;
import de.fyreum.customitemsxl.recipe.IShapelessRecipe;
import de.fyreum.customitemsxl.recipe.editor.RecipeEditor;
import de.fyreum.customitemsxl.serialization.ItemFactory;
import de.fyreum.customitemsxl.serialization.RecipeFactory;
import de.fyreum.customitemsxl.serialization.file.RootFile;
import de.fyreum.customitemsxl.serialization.file.RootFileManager;
import de.fyreum.customitemsxl.serialization.roots.Root;
import de.fyreum.customitemsxl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

public final class CustomItemsXL extends DREPlugin {

    private static CustomItemsXL instance;

    public static final ILogger LOGGER = new ILogger();

    public static File ITEMS;
    public static File RECIPES;

    private ConfigSettings configSettings;
    private FilterSettings filterSettings;
    private RootFileManager rootFileManager;
    private ItemFactory itemFactory;
    private RecipeFactory recipeFactory;
    private FactionsXL factionsXL;
    private CICommandCache commandCache;

    public CustomItemsXL() {
        settings = DREPluginSettings.builder()
                .spigot(true)
                .paper(true)
                .internals(Internals.v1_16_R1)
                .build();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        initFolders();
        initExampleFiles();

        loadCore();
    }

    @Override
    public void onDisable() {
        rootFileManager.saveRoots();
        removeRecipes();
        HandlerList.unregisterAll(this);
    }

    public void initFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        ITEMS = new File(getDataFolder(), "items");
        if (!ITEMS.exists()) {
            ITEMS.mkdir();
        }

        RECIPES = new File(getDataFolder(), "recipes");
        if (!RECIPES.exists()) {
            RECIPES.mkdir();
        }
    }

    public void initExampleFiles() {
        Util.initFile(instance, new File(ITEMS, "ExampleItems.yml"), "items/ExampleItems.yml");
        Util.initFile(instance, new File(RECIPES, "ExampleRecipes.yml"), "recipes/ExampleRecipes.yml");
    }

    public void loadCore() {
        factionsXLHook();

        initialize();

        loadConfigs();

        LOGGER.setDebugMode(configSettings.getDebugMode());

        loadMessages();
        loadRoots();
        loadFilterSettings();

        registerRecipes();
        registerCommands();
        registerListeners();
    }

    public void factionsXLHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("FactionsXL")) {
            factionsXL = (FactionsXL) Bukkit.getPluginManager().getPlugin("FactionsXL");
            if (factionsXL == null) {
                LOGGER.log("&cCouldn't load FactionsXL properly.");
            } else {
                LOGGER.log("&aSuccessfully connected to FactionsXL.");
            }
        }
    }

    public void initialize() {
        itemFactory = new ItemFactory();
        recipeFactory = new RecipeFactory();
    }

    public void loadConfigs() {
        configSettings = new ConfigSettings(new File(getDataFolder(), "config.yml"));
    }

    public void loadMessages() {
        attemptToSaveResource("languages/english.yml", false);
        attemptToSaveResource("languages/german.yml", false);
        getMessageHandler().setDefaultLanguage(configSettings.getLanguage());
    }

    public void loadRoots() {
        rootFileManager = new RootFileManager();
        rootFileManager.loadItems(ITEMS);
        rootFileManager.loadRecipes(RECIPES);
    }

    public void loadFilterSettings() {
        // Copies the default file instead of creating a new empty file, to see the comments as well
        File filterFile = Util.initFile(instance, new File(getDataFolder(), "filter.yml"), "filter.yml");
        filterSettings = new FilterSettings(filterFile);
    }

    public void registerRecipes() {
        LOGGER.info("Adding recipes...");
        Set<IRecipe> recipes = rootFileManager.getRecipes();
        for (IRecipe recipe : recipes) {
            addRecipe(recipe);
        }
        LOGGER.info("Added " + recipes.size() + " recipes");
    }

    public boolean addRecipe(@NotNull IRecipe recipe) {
        if (recipe instanceof IShapedRecipe) {
            return addRecipe((IShapedRecipe) recipe);
        } else if (recipe instanceof IShapelessRecipe) {
            return addRecipe((IShapelessRecipe) recipe);
        } else {
            throw new IllegalArgumentException("Couldn't identify recipe type");
        }
    }

    public boolean addRecipe(@NotNull IShapelessRecipe recipe) {
        LOGGER.debug(DebugMode.MEDIUM, "Adding: " + recipe);

        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe r = iterator.next();
            if (r instanceof ShapedRecipe) {
                ShapedRecipe shaped = (ShapedRecipe) r;
                if (shaped.getKey().equals(recipe.getKey())) {
                    LOGGER.debug(DebugMode.MEDIUM, "Found pre-existing recipe, removing it");
                    iterator.remove();
                }
            } else if (r instanceof ShapelessRecipe) {
                ShapelessRecipe shapeless = (ShapelessRecipe) r;
                if (shapeless.getKey().equals(recipe.getKey())) {
                    LOGGER.debug(DebugMode.MEDIUM, "Found pre-existing recipe, removing it");
                    iterator.remove();
                }
            }
        }
        return Bukkit.addRecipe(recipe);
    }

    public boolean addRecipe(@NotNull IShapedRecipe recipe) {
        LOGGER.debug(DebugMode.MEDIUM, "Adding: " + recipe);

        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe r = iterator.next();
            if (r instanceof ShapedRecipe) {
                ShapedRecipe shaped = (ShapedRecipe) r;
                if (shaped.getKey().getKey().equals(recipe.getKey().getKey())) {
                    LOGGER.debug(DebugMode.MEDIUM, "Found pre-existing recipe, removing it");
                    iterator.remove();
                }
            } else if (r instanceof ShapelessRecipe) {
                ShapelessRecipe shapeless = (ShapelessRecipe) r;
                if (shapeless.getKey().getKey().equals(recipe.getKey().getKey())) {
                    LOGGER.debug(DebugMode.MEDIUM, "Found pre-existing recipe, removing it");
                    iterator.remove();
                }
            }
        }
        return Bukkit.addRecipe(recipe);
    }

    public void removeRecipes() {
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe instanceof IRecipe) {
                iterator.remove();
            }
        }
    }

    public void registerCommands() {
        commandCache = new CICommandCache(instance);
        setCommandCache(commandCache);
        commandCache.register(instance);
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new RecipeEditor(), instance);
        Bukkit.getPluginManager().registerEvents(new FilterListener(), instance);
    }

    public void reload() {
        onDisable();
        loadCore();
    }

    /* "api" stuff */

    @Nullable
    public ItemStack getItemStack(String id) {
        return rootFileManager.getMatchingItem(id);
    }

    @Nullable
    public Root<ItemStack> getItemRoot(String id) {
        return rootFileManager.getMatchingItemRoot(id);
    }

    @Nullable
    public RootFile<Root<ItemStack>> getItemRootFile(String id) {
        return rootFileManager.getMatchingItemRootFile(id);
    }

    @Nullable
    public Root<ItemStack> getItemRoot(ItemStack item) {
        return rootFileManager.getMatchingItemRoot(item);
    }

    @Nullable
    public RootFile<Root<ItemStack>> getItemRootFile(ItemStack item) {
        return rootFileManager.getMatchingItemRootFile(item);
    }

    public Root<ItemStack> register(String file, String id, ItemStack item) {
        return rootFileManager.addItem(file, id, item);
    }

    public Root<IRecipe> register(String file, String id, IRecipe recipe) {
        return rootFileManager.addRecipe(file, id, recipe);
    }

    /* getter */

    public ConfigSettings getConfigSettings() {
        return configSettings;
    }

    public FilterSettings getFilterSettings() {
        return filterSettings;
    }

    public FactionsXL getFactionsXL() {
        return factionsXL;
    }

    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    public RecipeFactory getRecipeFactory() {
        return recipeFactory;
    }

    public RootFileManager getRootFileManager() {
        return rootFileManager;
    }

    @Override
    public CICommandCache getCommandCache() {
        return commandCache;
    }

    /* statics */

    public static NamespacedKey key(String key) {
        return new NamespacedKey(instance, key);
    }

    public static CustomItemsXL inst() {
        return instance;
    }
}
