package de.fyreum.customitemsxl.serialization;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.recipe.IShapedRecipe;
import de.fyreum.customitemsxl.recipe.IShapelessRecipe;
import de.fyreum.customitemsxl.recipe.ingredients.IRecipeIngredient;
import de.fyreum.customitemsxl.recipe.ingredients.ItemIngredient;
import de.fyreum.customitemsxl.recipe.ingredients.MaterialIngredient;
import de.fyreum.customitemsxl.recipe.result.RecipeResult;
import de.fyreum.customitemsxl.serialization.roots.Root;
import de.fyreum.customitemsxl.util.SecuredStringBuilder;
import de.fyreum.customitemsxl.util.StringKeyed;
import de.fyreum.customitemsxl.util.Util;
import de.fyreum.customitemsxl.util.Validate;
import de.fyreum.customitemsxl.util.ValidationException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeFactory {

    /*
    CONCEPT of Serialization

    VANILLA:

     - shaped
     id: {type=shaped;result=diamond_pickaxe;amount=1;shape=d,d,d, ,s, , ,s, ;ingredients=d:diamond,s:stick;group=tool;}
     - shapeless
     id: {type=shapeless;result=diamond_pickaxe;amount=1;ingredients=diamond:3,stick:2;group=tool;}

    CUSTOM:

     - shaped
     id: {type=shaped;result=item_1;amount=1;shape=d,d,d, ,s, , ,s, ;ingredients=d:item_2,s:item_3;group=custom;}
     - shapeless
     id: {type=shapeless;result=item_1;amount=1;ingredients=item_2:3,item_3:2;group=custom;}
     */
    public String serialize(IRecipe recipe) {
        StringBuilder sb = new StringBuilder();

        if (recipe instanceof ShapedRecipe) {
            IShapedRecipe shapedRecipe = (IShapedRecipe) recipe;

            sb.append("type=shaped").append(";");
            sb.append("result=").append(shapedRecipe.getRecipeResult().getStringKey()).append(";");
            sb.append("amount=").append(shapedRecipe.getAmount()).append(";");
            sb.append("shape=").append(Util.toShapeString(shapedRecipe.getShape())).append(";");
            sb.append("ingredients=").append(toString(shapedRecipe.getIngredients())).append(";");
            if (!shapedRecipe.getGroup().isEmpty()) {
                sb.append("group=").append(shapedRecipe.getGroup()).append(";");
            }
        } else {
            IShapelessRecipe shapelessRecipe = (IShapelessRecipe) recipe;

            sb.append("type=shapeless").append(";");
            sb.append("result=").append(shapelessRecipe.getRecipeResult().getStringKey()).append(";");
            sb.append("amount=").append(shapelessRecipe.getAmount()).append(";");
            sb.append("ingredients=").append(toIngredientString(shapelessRecipe.getIngredients())).append(";");
            if (!shapelessRecipe.getGroup().isEmpty()) {
                sb.append("group=").append(shapelessRecipe.getGroup()).append(";");
            }
        }
        return "{" + sb + "}";
    }

    public IRecipe deserialize(NamespacedKey rKey, String serialized) {
        if (serialized.isEmpty()) {
            throw new ConfigSerializationError("Couldn't find any value");
        }
        try {
            char[] allChars = Validate.braceFormat(serialized.toCharArray());

            SecuredStringBuilder typeBuilder = new SecuredStringBuilder(Key.TYPE.getStringKey());
            SecuredStringBuilder resultBuilder = new SecuredStringBuilder(Key.RESULT.getStringKey());
            SecuredStringBuilder amountBuilder = new SecuredStringBuilder(Key.AMOUNT.getStringKey());
            SecuredStringBuilder shapeBuilder = new SecuredStringBuilder(Key.SHAPE.getStringKey());
            SecuredStringBuilder ingredientsBuilder = new SecuredStringBuilder(Key.INGREDIENTS.getStringKey());
            SecuredStringBuilder groupBuilder = new SecuredStringBuilder(Key.GROUP.getStringKey());

            StringBuilder keyBuilder = new StringBuilder();

            Key key = null;

            for (int i = 1; i < allChars.length - 1; i++) {
                char current = allChars[i];

                if (key == null) {
                    if (Util.handleChar(Validate.noSpace(current, i), '=', i, keyBuilder)) {
                        String keyName = keyBuilder.toString();
                        keyBuilder = new StringBuilder();

                        key = Validate.notNull(Key.getByKey(keyName), "Couldn't identify key '" + keyName + "'");
                    }
                } else {
                    switch (key) {
                        case TYPE:
                            if (Util.handleChar(current, i, typeBuilder)) {
                                key = null;
                            }
                            break;
                        case RESULT:
                            if (Util.handleChar(current, i, resultBuilder)) {
                                key = null;
                            }
                            break;
                        case AMOUNT:
                            if (Util.handleChar(current, i, amountBuilder)) {
                                key = null;
                            }
                            break;
                        case SHAPE:
                            if (Util.handleChar(current, i, shapeBuilder, ':')) {
                                key = null;
                            }
                            break;
                        case INGREDIENTS:
                            if (Util.handleChar(current, i, ingredientsBuilder, ':')) {
                                key = null;
                            }
                            break;
                        case GROUP:
                            if (Util.handleChar(current, i, groupBuilder)) {
                                key = null;
                            }
                            break;
                    }
                }
            }
            boolean shaped = isShaped(Validate.builderValue(typeBuilder, "Missing statement 'type=TYPE'"));

            int amount = 1;

            if (!amountBuilder.isEmpty()) {
                amount = Util.parseInt(amountBuilder.toString(), 0, 1, "Cannot set an lower amount than '1'");
            }

            String group = groupBuilder.isEmpty() ? "" : groupBuilder.toString();

            String ig = Validate.builderValue(ingredientsBuilder);

            if (shaped) {
                String shapeString = Validate.builderValue(shapeBuilder, "You need to set an shape for shaped recipes");
                Validate.check(shapeString.length() == 11, "Wrong shape format, found '" + shapeString + "' but require: '___:___:___'");

                String[] shape = Validate.length(shapeString.split(":"), 3, "Wrong shape format, found '" + shapeString + "' but require: '___:___:___'");
                
                // CHAR1:INGREDIENT1,CHAR2:INGREDIENT2;
                Map<Character, IRecipeIngredient> ingredients = new HashMap<>();

                String[] split = ig.split(",");
                for (String igString : split) {
                    // CHAR:INGREDIENT
                    String[] s = Validate.length(igString.split(":"), 2, "Wrong Ingredient format, found '" + igString + "' but require: 'CHAR:INGREDIENT'");

                    char c = Validate.length(s[0], 1, "").charAt(0);

                    ingredients.put(c, buildIngredient(s[1]));
                }

                CustomItemsXL.LOGGER.debug(DebugMode.EXTENDED, "Creating IShapedRecipe '" + rKey.getKey() + "'...");
                return new IShapedRecipe(rKey, buildResult(resultBuilder.toString(), amount), shape, ingredients, group);
            } else {
                Validate.check(shapeBuilder.isEmpty(), "You cannot set an shape for shapeless recipes (Make sure that type=TYPE is the first statement)");

                // INGREDIENT1:AMOUNT2,INGREDIENT1:AMOUNT2;
                Map<IRecipeIngredient, Integer> ingredients = new HashMap<>();

                String[] split = ig.split(",");
                for (String igString : split) {
                    // INGREDIENT:AMOUNT
                    String[] s = Validate.length(igString.split(":"), 2, "Wrong Ingredient format, found '" + igString + "' but require: 'INGREDIENT:AMOUNT'");

                    IRecipeIngredient ingredient = buildIngredient(s[0]);
                    int a = Util.parseInt(s[1], 0, 1, "Ingredient amount has to be between 1 and 9");

                    ingredients.put(ingredient, a);
                }

                CustomItemsXL.LOGGER.debug(DebugMode.EXTENDED, "Creating IShapelessRecipe '" + rKey.getKey() + "'...");
                return new IShapelessRecipe(rKey, buildResult(resultBuilder.toString(), amount), ingredients, group);
            }
        } catch (ConfigSerializationError | ValidationException e) {
            throw new ConfigSerializationError("An error happened during deserialization of '" + serialized + "' | " + e.getMessage());
        }
    }

    private RecipeResult buildResult(String s, int a) {
        Root<ItemStack> root = CustomItemsXL.inst().getRootFileManager().getMatchingItemRoot(s);
        if (root != null) {
            return new RecipeResult(Util.setAmount(root.getDeserialized(), a), root.getId());
        } else {
            Material m = Validate.notNull(Material.getMaterial(s.toUpperCase()), "Couldn't find matching item for '" + s + "'");
            return new RecipeResult(m, a);
        }
    }

    private IRecipeIngredient buildIngredient(String s) {
        Root<ItemStack> i = CustomItemsXL.inst().getRootFileManager().getMatchingItemRoot(s);
        if (i != null) {
            return new ItemIngredient(i);
        } else {
            Material m = Validate.notNull(Material.getMaterial(s.toUpperCase()), "Couldn't identify ingredient '" + s + "'");
            return new MaterialIngredient(m);
        }
    }

    public static boolean isShaped(String s) {
        if (s.equalsIgnoreCase("shaped")) {
            return true;
        } else if (s.equalsIgnoreCase("shapeless")) {
            return false;
        } else {
            throw new ConfigSerializationError("Cannot identify recipe type '" + s + "'");
        }
    }

    public static String toString(Map<Character, IRecipeIngredient> ingredients) {
        return toString(ingredients, false);
    }

    public static String toString(Map<Character, IRecipeIngredient> ingredients, boolean brace) {
        Map<String, String> m = new HashMap<>();
        ingredients.forEach((c, i) -> m.put(String.valueOf(c), i.getSerialized()));
        return Util.toString(m, brace);
    }

    public static String toIngredientString(Map<IRecipeIngredient, Integer> ingredients) {
        return toIngredientString(ingredients, false);
    }

    public static String toIngredientString(Map<IRecipeIngredient, Integer> ingredients, boolean brace) {
        List<String> l = new ArrayList<>();
        ingredients.forEach((in, i) -> l.add(in.getSerialized() + ":" + i));
        return Util.toString(l, brace);
    }

    public enum Key implements StringKeyed {

        TYPE,
        RESULT,
        AMOUNT,
        SHAPE,
        INGREDIENTS,
        GROUP;

        @Override
        public String getStringKey() {
            return name().toLowerCase();
        }

        public static Key getByKey(String key) {
            for (Key value : values()) {
                if (value.name().equalsIgnoreCase(key)) {
                    return value;
                }
            }
            return null;
        }
    }
}
