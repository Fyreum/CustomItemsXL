package de.fyreum.customitemsxl.util;

import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;
import java.util.List;

public enum EquipmentSlotFinder {

    HAND(EquipmentSlot.HAND, "main", "1"),
    OFF_HAND(EquipmentSlot.OFF_HAND, "off", "2"),
    FEET(EquipmentSlot.FEET, "3"),
    LEGS(EquipmentSlot.LEGS, "4"),
    CHEST(EquipmentSlot.CHEST, "5"),
    HEAD(EquipmentSlot.HEAD, "6");

    private final EquipmentSlot slot;
    private String[] aliases;

    EquipmentSlotFinder(EquipmentSlot slot, String... aliases) {
        this.slot = slot;
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

    public EquipmentSlot getSlot() {
        return slot;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static boolean unbound(String s) {
        return s.equalsIgnoreCase("all") | s.equalsIgnoreCase("0");
    }

    public static EquipmentSlot getByName(String name) {
        for (EquipmentSlotFinder finder : values()) {
            if (finder.name().equalsIgnoreCase(name) || Util.contains(finder.aliases, name)) {
                return finder.slot;
            }
        }
        return null;
    }
}
