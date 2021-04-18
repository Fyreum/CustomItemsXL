package de.fyreum.customitemsxl.util;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.misc.NumberUtil;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Util {

    public static String toString(String[] a) {
        return toString(a, false);
    }

    public static String toString(String[] a, boolean brace) {
        return toString(a, ",", brace);
    }

    public static String toString(String[] a, String br) {
        return toString(a, br, false);
    }

    public static String toString(String[] a, String br, boolean brace) {
        int iMax = a.length - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax) {
                return brace ? "{" + b.toString() + "}" : b.toString();
            }
            b.append(br);
        }
    }

    public static String toString(List<String> l) {
        return toString(l, false);
    }

    public static String toString(List<String> l, boolean brace) {
        return toString(l, ",",  brace);
    }

    public static String toString(List<String> l, String br) {
        return toString(l, br, false);
    }

    public static String toString(List<String> l, String br, boolean brace) {
        String[] a = l.toArray(new String[0]);
        return toString(a, br, brace);
    }

    public static String toString(Map<String, String> m) {
        return toString(m, false);
    }

    public static String toString(Map<String, String> m, boolean brace) {
        String[] k = m.keySet().toArray(new String[0]);
        String[] v = m.values().toArray(new String[0]);
        int iMax = k.length - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(k[i]).append(":").append(v[i]);
            if (i == iMax) {
                return brace ? "{" + b + "}" : b.toString();
            }
            b.append(",");
        }
    }

    public static String toShapeString(String[] a) {
        Validate.length(a, 3, "Wrong shape format, need 3 rows");

        String one = Validate.length(a[0], 3, "Wrong shape format (row 1)");
        String two = Validate.length(a[1], 3, "Wrong shape format (row 2)");
        String three = Validate.length(a[2], 3, "Wrong shape format (row 3)");

        return one + ":" + two + ":" + three;
    }

    public static String[] toShape(String s) {
        Validate.length(s, 9, "Wrong shape format, need 3 rows");

        char[] one = new char[3];
        char[] two = new char[3];
        char[] three = new char[3];

        s.getChars(0, 3, one, 0);
        s.getChars(3, 6, two, 0);
        s.getChars(6, 9, three, 0);

        return new String[]{String.valueOf(one), String.valueOf(two), String.valueOf(three)};
    }

    public static String placeBetweenChars(String s, String p) {
        StringBuilder b = new StringBuilder();
        for (char c : s.toCharArray()) {
            b.append(c).append(p);
        }
        return b.toString();
    }

    public static String placeBetweenChars(String s, String p, String last, int l) {
        StringBuilder b = new StringBuilder();
        for (char c : s.toCharArray()) {
            b.append(c);
            if (s.charAt(l) != c) {
                b.append(p);
            } else {
                b.append(last);
            }
        }
        return b.toString();
    }

    public static String toDebugString(Object[] a) {
        return toDebugString(a, true);
    }

    public static String toDebugString(Object[] a, boolean brace) {
        int iMax = a.length - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(a[i].toString());
            if (i == iMax) {
                return brace ? "{" + b.toString() + "}" : b.toString();
            }
            b.append(",");
        }
    }

    public static String toDebugString(List<Object> l) {
        return toDebugString(l, true);
    }

    public static String toDebugString(List<Object> l, boolean brace) {
        Object[] a = l.toArray(new Object[0]);
        return toDebugString(a, brace);
    }

    public static boolean handleChar(char c, int column, SecuredStringBuilder builder, char... ex) {
        return handleChar(c, ';' , column, builder, ex);
    }

    public static boolean handleChar(char c, int column, SecuredStringBuilder builder) {
        return handleChar(c, ';', column, builder);
    }

    public static boolean handleChar(char c, char e, int column, SecuredStringBuilder builder, char... ex) {
        if (Validate.isCorrectFormatChar(c, e, column, ex)) {
            builder.setAccessible(false);
            return true;
        }
        builder.builder().append(c);
        return false;
    }

    public static boolean handleChar(char c, int column, StringBuilder builder) {
        return handleChar(c, ';', column, builder);
    }

    public static boolean handleChar(char c, int column, StringBuilder builder, char... ex) {
        return handleChar(c, ';' , column, builder, ex);
    }

    public static boolean handleChar(char c, char e, int column, StringBuilder builder, char... ex) {
        if (Validate.isCorrectFormatChar(c, e, column, ex)) {
            return true;
        }
        builder.append(c);
        return false;
    }

    public static String[] color(String... a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = MessageUtil.color(a[i]);
        }
        return a;
    }

    public static List<String> color(List<String> list) {
        List<String> colored = new ArrayList<>();
        for (String s : list) {
            colored.add(MessageUtil.color(s));
        }
        return colored;
    }

    public static List<String> coloredAsList(String... a) {
        return Arrays.asList(color(a));
    }

    public static int parseInt(String s, int d, int a, String msg) {
        int parsed = NumberUtil.parseInt(s, d);
        if (parsed < a) {
            throw new ConfigSerializationError(msg);
        }
        return parsed;
    }

    public static double parseDouble(String s, double d, double a, String msg) {
        double parsed = NumberUtil.parseDouble(s, d);
        if (parsed < a) {
            throw new ConfigSerializationError(msg);
        }
        return parsed;
    }

    public static boolean contains(char[] a, char c) {
        for (char c1 : a) {
            if (c1 == c) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] a, String s) {
        for (String s1 : a) {
            if (s1.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(T[] a, T t) {
        for (T value : a) {
            if (value.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(int[] a, int t) {
        for (int value : a) {
            if (value == t) {
                return true;
            }
        }
        return false;
    }

    public static UUID uniqueIdFromString(String s) {
        UUID uuid;
        if (s.equalsIgnoreCase("random") || s.equalsIgnoreCase("r")) {
            uuid = UUID.randomUUID();
        } else {
            try {
                uuid = UUID.fromString(s);
            } catch (IllegalArgumentException e) {
                throw new ConfigSerializationError("Invalid UUID");
            }
        }
        return uuid;
    }

    /**
     * @return an copy of the ItemStack with the new amount
     */
    public static ItemStack setAmount(ItemStack item, int a) {
        ItemStack i = item.clone();
        i.setAmount(a);
        return i;
    }

    public static boolean containsOne(String s, String... c) {
        for (String s1 : c) {
            if (s.contains(s1)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T notNullValue(@Nullable T v, @NotNull T e) {
        return v == null ? e : v;
    }

    public static <T> List<T> addAll(List<T> list, Collection<T> add) {
        List<T> l = new ArrayList<>(list);
        l.addAll(add);
        return l;
    }

    @SafeVarargs
    public static <T> T[] addToArray(T[] a, T... ts) {
        int oldLength = a.length;
        int newLength = oldLength + ts.length;

        T[] array = Arrays.copyOf(a, newLength);

        System.arraycopy(ts, 0, array, oldLength, newLength - oldLength);
        return array;
    }

    public static int[] addToArray(int[] a, int... ts) {
        int oldLength = a.length;
        int newLength = oldLength + ts.length;

        int[] array = Arrays.copyOf(a, newLength);

        System.arraycopy(ts, 0, array, oldLength, newLength - oldLength);
        return array;
    }

    public static char getChar(int i) {
        if (i < 0 | i > 9) {
            throw new ValidationException("Chars can only be integers between 0-9");
        }
        switch (i) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
        }
        // shouldn't happen
        return ' ';
    }

    public static File initFile(Plugin plugin, File file, String resourcePath) {
        try {
            if (!file.exists()) {
                InputStream link = plugin.getResource(resourcePath);
                if (link != null) {
                    Files.copy(link, file.getAbsoluteFile().toPath());
                } else {
                    CustomItemsXL.LOGGER.error("Couldn't copy " + file.getName() + " into the plugin folder");
                }
            }
        } catch (IOException e) {
            CustomItemsXL.LOGGER.error("Couldn't copy " + file.getName() + " into the plugin folder");
            CustomItemsXL.LOGGER.debug(DebugMode.LOW, e.getMessage());
        }
        return file;
    }
}
