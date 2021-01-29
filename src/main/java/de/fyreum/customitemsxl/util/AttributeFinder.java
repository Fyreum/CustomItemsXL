package de.fyreum.customitemsxl.util;

import org.bukkit.attribute.Attribute;

import java.util.Arrays;
import java.util.List;

public enum AttributeFinder {

    GENERIC_MAX_HEALTH(Attribute.GENERIC_MAX_HEALTH, "max_health"),
    GENERIC_FOLLOW_RANGE(Attribute.GENERIC_FOLLOW_RANGE, "follow_range"),
    GENERIC_KNOCKBACK_RESISTANCE(Attribute.GENERIC_KNOCKBACK_RESISTANCE, "knockback_resistance"),
    GENERIC_MOVEMENT_SPEED(Attribute.GENERIC_MOVEMENT_SPEED, "movement_speed"),
    GENERIC_FLYING_SPEED(Attribute.GENERIC_FLYING_SPEED, "flying_speed"),
    GENERIC_ATTACK_DAMAGE(Attribute.GENERIC_ATTACK_DAMAGE, "attack_damage"),
    GENERIC_ATTACK_KNOCKBACK(Attribute.GENERIC_ATTACK_KNOCKBACK, "attack_knockback"),
    GENERIC_ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, "attack_speed"),
    GENERIC_ARMOR(Attribute.GENERIC_ARMOR, "armor"),
    GENERIC_ARMOR_TOUGHNESS(Attribute.GENERIC_ARMOR_TOUGHNESS, "armor_toughness"),
    GENERIC_LUCK(Attribute.GENERIC_LUCK, "luck"),
    HORSE_JUMP_STRENGTH(Attribute.HORSE_JUMP_STRENGTH, "jump_strength"),
    ZOMBIE_SPAWN_REINFORCEMENTS(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, "spawn_reinforcements");

    private final Attribute attribute;
    private String[] aliases;

    AttributeFinder(Attribute attribute, String... aliases) {
        this.attribute = attribute;
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

    public Attribute getEnchant() {
        return attribute;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static Attribute getByName(String name) {
        for (AttributeFinder finder : values()) {
            if (finder.name().equalsIgnoreCase(name) || Util.contains(finder.aliases, name)) {
                return finder.attribute;
            }
        }
        return null;
    }
}
