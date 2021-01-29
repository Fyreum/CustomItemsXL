package de.fyreum.customitemsxl;

import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.factionsxl.FactionsXL;
import de.fyreum.customitemsxl.command.CICommandCache;
import de.fyreum.customitemsxl.cooldowns.CooldownManager;
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
import de.fyreum.customitemsxl.serialization.file.RootFileManager;
import de.fyreum.customitemsxl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;

public final class CustomItemsXL extends DREPlugin {

    private static CustomItemsXL instance;

    public static final ILogger LOGGER = new ILogger();

    public static File ITEMS;
    public static File RECIPES;

    private ConfigSettings configSettings;
    private FilterSettings filterSettings;
    private RootFileManager rootFileManager;
    private CooldownManager cooldownManager;
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

        factionsXLHook();
        initFolders();
        initExampleFiles();

        loadConfigs();

        LOGGER.setDebugMode(configSettings.getDebugMode());

        loadMessages();
        loadCore();

        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        rootFileManager.saveRoots();
    }

    private void factionsXLHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("FactionsXL")) {
            factionsXL = (FactionsXL) Bukkit.getPluginManager().getPlugin("FactionsXL");
            if (factionsXL == null) {
                LOGGER.log("&cCouldn't load FactionsXL properly.");
            } else {
                LOGGER.log("&aSuccessfully connected to FactionsXL.");
            }
        }
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
        Util.initFile(instance, new File(ITEMS, "ExampleRecipes.yml"), "recipes/ExampleRecipes.yml");
    }

    public void loadConfigs() {
        configSettings = new ConfigSettings(new File(getDataFolder(), "config.yml"));
        // Copies the default file instead of creating a new empty file, to see the comments as well
        File filterFile = Util.initFile(instance, new File(getDataFolder(), "filter.yml"), "filter.yml");
        filterSettings = new FilterSettings(filterFile);
    }

    public void loadMessages() {
        attemptToSaveResource("languages/english.yml", false);
        attemptToSaveResource("languages/german.yml", false);
        getMessageHandler().setDefaultLanguage(configSettings.getLanguage());
    }

    public void loadCore() {
        initialize();
        loadRoots();
        registerRecipes();
    }

    public void initialize() {
        cooldownManager = new CooldownManager(instance);
        itemFactory = new ItemFactory();
        recipeFactory = new RecipeFactory();
    }

    public void loadRoots() {
        rootFileManager = new RootFileManager();
        rootFileManager.loadItems(ITEMS);
        rootFileManager.loadRecipes(RECIPES);
    }

    public void registerRecipes() {
        LOGGER.info("Adding recipes...");
        for (IRecipe recipe : rootFileManager.getRecipes()) {
            addRecipe(recipe);
        }
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
        LOGGER.debug(DebugMode.MEDIUM, "Adding: " + recipe.toString());

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
        LOGGER.debug(DebugMode.MEDIUM, "Adding: " + recipe.toString());

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
        getCommand(CICommandCache.LABEL).setTabCompleter(commandCache);
    }

    public void reload() {
        removeRecipes();

        initFolders();
        initExampleFiles();

        loadConfigs();
        loadMessages();
        loadCore();
    }

    public ConfigSettings getConfigSettings() {
        return configSettings;
    }

    public FilterSettings getFilterSettings() {
        return filterSettings;
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(new RecipeEditor(), instance);
    }

    public FactionsXL getFactionsXL() {
        return factionsXL;
    }

    public CooldownManager getCooldownManager(){
        return cooldownManager;
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

    public static NamespacedKey key(String key) {
        return new NamespacedKey(instance, key);
    }

    public static CustomItemsXL inst() {
        return instance;
    }
}
