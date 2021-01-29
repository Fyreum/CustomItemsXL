package de.fyreum.customitemsxl.util;

import org.bukkit.attribute.AttributeModifier;

import java.util.Arrays;
import java.util.List;

public enum OperationFinder {

    ADD_NUMBER(AttributeModifier.Operation.ADD_NUMBER, "add_n", "number", "0"),
    ADD_SCALAR(AttributeModifier.Operation.ADD_SCALAR, "add_s", "scalar", "1"),
    MULTIPLY_SCALAR_1(AttributeModifier.Operation.MULTIPLY_SCALAR_1, "multiply", "multiply_scalar", "2"),

    ;

    private final AttributeModifier.Operation operation;
    private String[] aliases;

    OperationFinder(AttributeModifier.Operation operation, String... aliases) {
        this.operation = operation;
        this.aliases = aliases;
    }

    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }

    public void addAliases(String... aliases) {
        List<String> list = Arrays.asList(this.aliases);
        list.addAll(Arrays.asList(aliases));
        this.aliases = list.toArray(new String[0]);
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static AttributeModifier.Operation getByName(String name) {
        for (OperationFinder finder : values()) {
            if (finder.name().equalsIgnoreCase(name) || Util.contains(finder.aliases, name)) {
                return finder.operation;
            }
        }
        return null;
    }
}
