package de.fyreum.customitemsxl.recipe.editor;

import de.erethon.commons.chat.MessageUtil;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.recipe.IShapedRecipe;
import de.fyreum.customitemsxl.recipe.IShapelessRecipe;
import de.fyreum.customitemsxl.recipe.ingredients.IRecipeIngredient;
import de.fyreum.customitemsxl.recipe.ingredients.ItemIngredient;
import de.fyreum.customitemsxl.recipe.ingredients.MaterialIngredient;
import de.fyreum.customitemsxl.recipe.result.RecipeResult;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.serialization.roots.Root;
import de.fyreum.customitemsxl.util.Util;
import de.fyreum.customitemsxl.util.Validate;
import de.fyreum.customitemsxl.util.ValidationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeEditor implements Listener {

    public static final String EDITOR_ITEM_FILE_NAME = "editor_items.yml";

    private static final int RESULT_INDEX = 14;
    private static final int[] RECIPE_FIELD_INDEXES = new int[]{1,2,3,10,11,12,19,20,21};
    private static final int[] NON_GLASS_INDEXES = new int[]{RESULT_INDEX,16,17};

    private static final NamespacedKey GLASS_KEY = CustomItemsXL.key("editor_glass");
    private static final NamespacedKey TYPE_KEY = CustomItemsXL.key("type_button");
    private static final NamespacedKey FINISH_KEY = CustomItemsXL.key("finish_button");
    private static final NamespacedKey NO_RESULT_KEY = CustomItemsXL.key("no_result_key");

    private static final String DUMMY_STRING = "dummy string";

    private static final ItemStack GLASS = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    private static final ItemStack SHAPED_TYPE_BUTTON = new ItemStack(Material.PAPER);
    private static final ItemStack SHAPELESS_TYPE_BUTTON = new ItemStack(Material.PAPER);
    private static final ItemStack FINISH_BUTTON = new ItemStack(Material.GREEN_DYE);
    private static final ItemStack NO_RESULT_ITEM = new ItemStack(Material.RED_DYE);

    // listener stuff

    private static final Map<UUID, EditSession> currentEditors = new HashMap<>();

    static {
        Arrays.sort(RECIPE_FIELD_INDEXES);
        Arrays.sort(NON_GLASS_INDEXES);
        // buttons
        ItemMeta meta = GLASS.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK + "");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(GLASS_KEY, PersistentDataType.STRING, DUMMY_STRING);
        GLASS.setItemMeta(meta);

        meta = SHAPED_TYPE_BUTTON.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "ShapedRecipe");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.STRING, DUMMY_STRING);
        SHAPED_TYPE_BUTTON.setItemMeta(meta);

        meta = SHAPELESS_TYPE_BUTTON.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "ShapelessRecipe");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.STRING, DUMMY_STRING);
        SHAPELESS_TYPE_BUTTON.setItemMeta(meta);

        meta = FINISH_BUTTON.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Add Recipe ✓");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(FINISH_KEY, PersistentDataType.STRING, DUMMY_STRING);
        FINISH_BUTTON.setItemMeta(meta);

        meta = NO_RESULT_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Missing result");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(NO_RESULT_KEY, PersistentDataType.STRING, DUMMY_STRING);
        NO_RESULT_ITEM.setItemMeta(meta);
    }

    public static void startSession(String id, String file, Player player) {
        currentEditors.put(player.getUniqueId(), new EditSession(id, file, false));
        openInv(player, id, null);
    }

    public static void startSession(String file, IRecipe recipe, Player player) {
        currentEditors.put(player.getUniqueId(), new EditSession(recipe.getRecipeResult().getStringKey(), file, true));
        openInv(player, recipe.getRecipeResult().getStringKey(), recipe);
    }

    private static void openInv(@NotNull Player player, String name, @Nullable IRecipe existing) {
        Inventory inv = Bukkit.createInventory(null, 27, MessageUtil.color("&8[&6Recipe Editor&8] &7" + name));

        for (int i = 0; i < 27; i++) {
            if (!Util.contains(RECIPE_FIELD_INDEXES, i) && !Util.contains(NON_GLASS_INDEXES, i)) {
                inv.setItem(i, GLASS);
            }
        }
        inv.setItem(16, SHAPED_TYPE_BUTTON);
        inv.setItem(17, FINISH_BUTTON);

        if (existing != null) {
            if (existing instanceof IShapedRecipe) {
                IShapedRecipe shaped = (IShapedRecipe) existing;
                String[] shape = shaped.getShape();
                Map<Character, IRecipeIngredient> ingredients = shaped.getIngredients();

                inv.setItem(RESULT_INDEX, shaped.getRecipeResult().getItemStack());

                int k = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        char c = shape[i].toCharArray()[j];
                        for (Character character : ingredients.keySet()) {
                            if (c == character) {
                                inv.setItem(RECIPE_FIELD_INDEXES[k], ingredients.get(character).getRecipeChoice().getItemStack());
                            }
                        }
                        k++;
                    }
                }
            } else {
                IShapelessRecipe shapeless = (IShapelessRecipe) existing;
                Map<IRecipeIngredient, Integer> ingredients = shapeless.getIngredients();

                inv.setItem(RESULT_INDEX, shapeless.getRecipeResult().getItemStack());

                int i = 0;
                for (IRecipeIngredient ingredient : ingredients.keySet()) {
                    for (int j = 0; j < ingredients.get(ingredient); j++) {
                        if (i >= 9) {
                            throw new ConfigSerializationError("Too many ingredients");
                        }
                        inv.setItem(RECIPE_FIELD_INDEXES[i], ingredient.getRecipeChoice().getItemStack());
                        i++;
                    }
                }
            }
        }

        player.openInventory(inv);
    }

    // listener

    @EventHandler
    public void handleInventoryClose(InventoryCloseEvent event) {
        if (!currentEditors.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }
        currentEditors.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        HumanEntity who = event.getWhoClicked();
        if (!currentEditors.containsKey(who.getUniqueId())) {
            return;
        }
        Inventory clicked = event.getClickedInventory();
        if (clicked == null || event.getCurrentItem() == null) {
            return;
        }
        ItemStack item = event.getCurrentItem();

        if (item.equals(GLASS)) {
            event.setCancelled(true);
            return;
        }
        if (item.equals(SHAPED_TYPE_BUTTON)) {
            event.setCancelled(true);
            currentEditors.get(who.getUniqueId()).setShaped(false);
            event.setCurrentItem(SHAPELESS_TYPE_BUTTON);
            return;
        }
        if (item.equals(SHAPELESS_TYPE_BUTTON)) {
            event.setCancelled(true);
            currentEditors.get(who.getUniqueId()).setShaped(true);
            event.setCurrentItem(SHAPED_TYPE_BUTTON);
            return;
        }
        if (item.equals(FINISH_BUTTON)) {
            event.setCancelled(true);
            try {
                saveRecipe(clicked, currentEditors.get(who.getUniqueId()));
            } catch (ValidationException e) {
                event.setCurrentItem(NO_RESULT_ITEM);
                Bukkit.getScheduler().runTaskLater(CustomItemsXL.inst(), () -> event.setCurrentItem(FINISH_BUTTON), 30);
                return;
            }
            MessageUtil.sendMessage(who, "&aRecipe saved!");
            currentEditors.remove(who.getUniqueId());
            who.closeInventory();
        }
    }

    private void saveRecipe(Inventory inv, EditSession session) {
        String file = session.getFile();
        String id = session.getId();

        ItemStack result = Validate.notNull(inv.getItem(RESULT_INDEX), "You need to set an result");

        CustomItemsXL plugin = CustomItemsXL.inst();
        if (session.isShaped()) {
            String shape = "123456789";
            Map<IRecipeIngredient, List<Character>> cache = new HashMap<>();
            Map<Character, IRecipeIngredient> ingredients = new HashMap<>();

            int c = 1;
            for (int index : RECIPE_FIELD_INDEXES) {
                ItemStack item = inv.getItem(index);
                if (item == null || item.getType().equals(Material.AIR)) {
                    shape = shape.replace(Util.getChar(c), ' ');
                    c++;
                    continue;
                }
                IRecipeIngredient ingredient = item.hasItemMeta() ? new ItemIngredient(getRoot(file, id, item)) : new MaterialIngredient(item.getType());

                boolean set = true;
                if (!cache.isEmpty()) {
                    for (IRecipeIngredient key : cache.keySet()) {
                        if (key.getRecipeChoice().equals(ingredient.getRecipeChoice())) {
                            cache.get(key).add(Util.getChar(c));
                            set = false;
                        }
                    }
                }
                if (set) {
                    List<Character> chars = new ArrayList<>();
                    chars.add(Util.getChar(c));
                    cache.put(ingredient, chars);
                }
                c++;
            }

            int i;
            for (IRecipeIngredient ingredient : cache.keySet()) {
                i = ingredients.size();

                List<Character> chars = cache.get(ingredient);

                char c1 = Util.getChar(i);
                for (Character ch : chars) {
                    shape = shape.replace(ch, c1);
                }
                ingredients.put(c1, ingredient);
            }

            RecipeResult recipeResult = getResult(id, result);
            IShapedRecipe recipe = new IShapedRecipe(CustomItemsXL.key(id), recipeResult, Util.toShape(shape), ingredients);

            plugin.getRootFileManager().addRecipe(file, id, recipe);
            plugin.addRecipe(recipe);
        } else {
            Map<IRecipeIngredient, Integer> ingredients = new HashMap<>();

            for (Integer index : RECIPE_FIELD_INDEXES) {
                ItemStack item = inv.getItem(index);
                if (item == null) {
                    continue;
                }
                boolean set = true;
                for (IRecipeIngredient key : ingredients.keySet()) {
                    if (key.getRecipeChoice().getItemStack().isSimilar(item)) {
                        Integer value = ingredients.get(key);
                        ingredients.put(key, value == null ? 1 : value + 1);
                        set = false;
                    }
                }
                if (set) {
                    ingredients.put(item.hasItemMeta() ? new ItemIngredient(getRoot(file, id, item)) : new MaterialIngredient(item.getType()), 1);
                }
            }

            RecipeResult recipeResult = getResult(id, result);
            IShapelessRecipe recipe = new IShapelessRecipe(CustomItemsXL.key(id), recipeResult, ingredients);

            plugin.getRootFileManager().addRecipe(file, id, recipe);
            plugin.addRecipe(recipe);
        }
    }

    private RecipeResult getResult(String id, ItemStack result) {
        RecipeResult recipeResult = null;
        for (Root<ItemStack> root : CustomItemsXL.inst().getRootFileManager().getItemRoots()) {
            if (root.getDeserialized().isSimilar(result)) {
                recipeResult = new RecipeResult(root.getDeserialized(), root.getId());
            }
        }
        if (recipeResult == null) {
            Root<ItemStack> root = getRoot(EDITOR_ITEM_FILE_NAME, id + "_result", result);
            recipeResult = result.hasItemMeta() ? new RecipeResult(root.getDeserialized(), root.getId()) : new RecipeResult(result.getType(), result.getAmount());
        }
        return recipeResult;
    }

    private Root<ItemStack> getRoot(String file, String id, ItemStack item) {
        CustomItemsXL plugin = CustomItemsXL.inst();
        for (Root<ItemStack> root : plugin.getRootFileManager().getItemRoots()) {
            if (root.getDeserialized().isSimilar(item)) {
                return root;
            }
        }
        return plugin.getRootFileManager().addItem(file, id, item);
    }
}
