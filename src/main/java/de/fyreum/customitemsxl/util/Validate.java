package de.fyreum.customitemsxl.util;

import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import org.bukkit.command.CommandSender;

public class Validate {

    private static final char[] formatChars = new char[]{';', '{', '}', '=', ':'};

    public static <T> T notNull(T t, String msg) throws ValidationException {
        if (t == null) {
            throw new ValidationException(msg);
        }
        return t;
    }

    public static char noSpace(char c, int column) throws ValidationException {
        if (c == ' ') {
            throw new ValidationException("Found forbidden space at column: " + column);
        }
        return c;
    }

    public static char[] braceFormat(char[] chars) throws ValidationException {
        if (chars[0] != '{') {
            throw new ValidationException("Expected '{' at column: 0 but found: '" + chars[0] + "'");
        }
        int lastIndex = chars.length - 1;
        if (chars[lastIndex] != '}') {
            throw new ValidationException("Expected '}' at column: " + lastIndex + " but found: '" + chars[0] + "'");
        }
        return chars;
    }

    public static boolean noFormatChar(char c, int column) throws ValidationException {
        for (char f : formatChars) {
            if (f == c) {
                throw new ValidationException("Found unnecessary formatting char: '" + c + "' at column: " + column);
            }
        }
        return true;
    }

    public static boolean noFormatChar(char c, int column, char... exceptions) throws ValidationException {
        for (char f : formatChars) {
            if (f == c) {
                if (Util.contains(exceptions, f)) {
                    return false;
                }
                throw new ValidationException("Found unnecessary formatting char: '" + c + "' at column: " + column);
            }
        }
        return true;
    }

    public static boolean formatChar(char c, int column) throws ValidationException {
        for (char f : formatChars) {
            if (f == c) {
                return true;
            }
        }
        throw new ValidationException("Expected formatting char at column: " + column + " but found '" + c + "'");
    }

    public static boolean isFormatChar(char c) {
        for (char f : formatChars) {
            if (f == c) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFormatChar(char c, char... e) {
        if (Util.contains(e, c)) {
            return false;
        }
        return isFormatChar(c);
    }

    public static boolean isCorrectFormatChar(char c, char e, int column, char... ex) {
        if (Validate.isFormatChar(c, ex)) {
            if (c != e) {
                throw new ConfigSerializationError("Expected '" + e + "' but found wrong formatting char '" + c + "' at column: " + column);
            }
            return true;
        }
        return false;
    }

    public static String builderValue(SecuredStringBuilder builder) throws ValidationException {
        return builderValue(builder, "You need to set an value for: " + builder.getKey());
    }

    public static String builderValue(SecuredStringBuilder builder, String msg) throws ValidationException {
        if (builder.isEmpty()) {
            throw new ValidationException(msg);
        }
        return builder.toString();
    }

    public static String builderValue(StringBuilder builder, String msg) throws ValidationException {
        if (builder.length() == 0) {
            throw new ValidationException(msg);
        }
        return builder.toString();
    }

    public static void check(boolean c, String msg) {
        if (!c) {
            throw new ValidationException(msg);
        }
    }

    public static <T> T[] length(T[] a, int l) {
        return length(a, l, "");
    }

    public static <T> T[] length(T[] a, int l, String msg) {
        if (a.length != l) {
            throw new ValidationException(msg);
        }
        return a;
    }

    public static <T> T[] length(T[] a, int min, int max) {
        return length(a, min, max, "");
    }

    public static <T> T[] length(T[] a, int min, int max, String msg) {
        if (a.length < min | a.length > max) {
            throw new ValidationException(msg);
        }
        return a;
    }

    public static <T> T[] minLength(T[] a, int l) {
        return minLength(a, l, "");
    }

    public static <T> T[] minLength(T[] a, int l, String msg) {
        if (a.length < l) {
            throw new ValidationException(msg);
        }
        return a;
    }

    public static <T> T[] maxLength(T[] a, int m) {
        return maxLength(a, m, "");
    }

    public static <T> T[] maxLength(T[] a, int m, String msg) {
        if (a.length > m) {
            throw new ValidationException(msg);
        }
        return a;
    }

    public static String length(String s, int l, String msg) {
        if (s.length() != l) {
            throw new ValidationException(msg);
        }
        return s;
    }

    public static void senderHasPermission(CommandSender sender, String p) {
        if (p == null || p.isEmpty() || sender.hasPermission(p)) {
            return;
        }
        throw new ValidationException("You don't have the permission to do this");
    }

}
