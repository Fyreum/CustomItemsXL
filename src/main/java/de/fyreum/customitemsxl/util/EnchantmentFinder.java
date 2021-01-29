package de.fyreum.customitemsxl.util;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

public enum EnchantmentFinder {

    PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL, "protection"),
    PROTECTION_FIRE(Enchantment.PROTECTION_FIRE, "fire_protection"),
    PROTECTION_FALL(Enchantment.PROTECTION_FALL, "feather_falling"),
    PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS, "blast_protection"),
    PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE, "projectile_protection"),
    OXYGEN(Enchantment.OXYGEN, "respiration"),
    WATER_WORKER(Enchantment.WATER_WORKER, "aqua_affinity"),
    THORNS(Enchantment.THORNS, "thorns"),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, "depth_strider"),
    FROST_WALKER(Enchantment.FROST_WALKER, "frost_walker"),
    BINDING_CURSE(Enchantment.BINDING_CURSE, "binding_curse"),
    DAMAGE_ALL(Enchantment.DAMAGE_ALL, "sharpness"),
    DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD, "smite"),
    DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, "bane_of_arthropods"),
    KNOCKBACK(Enchantment.KNOCKBACK, "knockback"),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, "fire_aspect"),
    LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS, "looting"),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE, "sweeping"),
    DIG_SPEED(Enchantment.DIG_SPEED, "efficiency"),
    SILK_TOUCH(Enchantment.SILK_TOUCH, "silk_touch"),
    DURABILITY(Enchantment.DURABILITY, "unbreaking"),
    LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS, "fortune"),
    ARROW_DAMAGE(Enchantment.ARROW_DAMAGE, "power"),
    ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK, "punch"),
    ARROW_FIRE(Enchantment.ARROW_FIRE, "flame"),
    ARROW_INFINITE(Enchantment.ARROW_INFINITE, "infinity"),
    LUCK(Enchantment.LUCK, "luck_of_the_sea"),
    LURE(Enchantment.LURE, "lure"),
    LOYALTY(Enchantment.LOYALTY, "loyalty"),
    IMPALING(Enchantment.IMPALING, "impaling"),
    RIPTIDE(Enchantment.RIPTIDE, "riptide"),
    CHANNELING(Enchantment.CHANNELING, "channeling"),
    MULTISHOT(Enchantment.MULTISHOT, "multishot"),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE, "quick_charge"),
    PIERCING(Enchantment.PIERCING, "piercing"),
    MENDING(Enchantment.MENDING, "mending"),
    VANISHING_CURSE(Enchantment.VANISHING_CURSE, "vanishing_curse"),
    SOUL_SPEED(Enchantment.SOUL_SPEED, "soul_speed");

    private final Enchantment enchant;
    private String[] aliases;

    EnchantmentFinder(Enchantment enchant, String... aliases) {
        this.enchant = enchant;
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

    public Enchantment getEnchant() {
        return enchant;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static Enchantment getByName(String name) {
        Enchantment byName = Enchantment.getByName(name);
        if (byName != null) {
            return byName;
        }
        return getByAlias(name);
    }

    public static Enchantment getByAlias(String a) {
        for (EnchantmentFinder finder : values()) {
            if (Util.contains(finder.aliases, a)) {
                return finder.enchant;
            }
        }
        return null;
    }
}
