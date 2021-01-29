package de.fyreum.customitemsxl.serialization;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.misc.NumberUtil;
import de.fyreum.customitemsxl.util.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ItemFactory {

    public String serialize(ItemStack item) {
        return serialize(item, true);
    }

    public String serialize(ItemStack item, boolean brace) {
        StringBuilder sb = new StringBuilder();
        ItemMeta meta = item.getItemMeta();

        sb.append("material=").append(item.getType().name().toLowerCase()).append(";");
        if (meta.hasDisplayName()) {
            sb.append("name=").append(meta.getDisplayName()).append(";");
        }
        if (meta.getLore() != null) {
            sb.append("lore=").append(Util.toString(meta.getLore())).append(";");
        }
        if (meta.hasEnchants()) {
            sb.append("enchants=").append(toString(meta.getEnchants())).append(";");
        }
        if (meta.hasAttributeModifiers()) {
            sb.append("modifiers=").append(toString(meta.getAttributeModifiers()));
        }
        if (meta.isUnbreakable()) {
            sb.append("unbreakable=true").append(";");
        }
        if (!meta.getItemFlags().isEmpty()) {
            sb.append("flags=").append(toString(meta.getItemFlags())).append(";");
        }
        Damageable damageable = (Damageable) meta;
        if (damageable.hasDamage()) {
            sb.append("damage=").append(damageable.getDamage()).append(";");
        }

        return brace ? "{" + sb.toString() + "}" : sb.toString();
    }

    public ItemStack deserialize(String serialized) throws ConfigSerializationError {
        if (serialized.isEmpty()) {
            throw new ConfigSerializationError("Couldn't find any value");
        }
        try {
            char[] allChars = Validate.braceFormat(serialized.toCharArray());

            SecuredStringBuilder materialBuilder = new SecuredStringBuilder(Key.MATERIAL.getStringKey());
            SecuredStringBuilder nameBuilder = new SecuredStringBuilder(Key.NAME.getStringKey());
            SecuredStringBuilder loreBuilder = new SecuredStringBuilder(Key.LORE.getStringKey());
            SecuredStringBuilder enchantsBuilder = new SecuredStringBuilder(Key.ENCHANTS.getStringKey());
            SecuredStringBuilder modifiersBuilder = new SecuredStringBuilder(Key.MODIFIERS.getStringKey());
            SecuredStringBuilder unbreakableBuilder = new SecuredStringBuilder(Key.UNBREAKABLE.getStringKey());
            SecuredStringBuilder damageBuilder = new SecuredStringBuilder(Key.DAMAGE.getStringKey());
            SecuredStringBuilder flagsBuilder = new SecuredStringBuilder(Key.FLAGS.getStringKey());

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
                        case MATERIAL:
                            if (Util.handleChar(current, i, materialBuilder)) {
                                key = null;
                            }
                            break;
                        case NAME:
                            if (Util.handleChar(current, i, nameBuilder)) {
                                key = null;
                            }
                            break;
                        case LORE:
                            if (Util.handleChar(current, i, loreBuilder, ':')) {
                                key = null;
                            }
                            break;
                        case ENCHANTS:
                            if (Util.handleChar(current, i, enchantsBuilder, ':')) {
                                key = null;
                            }
                            break;
                        case MODIFIERS:
                            if (Util.handleChar(current, i, modifiersBuilder, ':')) {
                                key = null;
                            }
                            break;
                        case UNBREAKABLE:
                            if (Util.handleChar(current, i, unbreakableBuilder)) {
                                key = null;
                            }
                            break;
                        case DAMAGE:
                            if (Util.handleChar(current, i, damageBuilder)) {
                                key = null;
                            }
                            break;
                        case FLAGS:
                            if (Util.handleChar(current, i, flagsBuilder)) {
                                key = null;
                            }
                            break;
                    }
                }
            }
            String materialName = Validate.builderValue(materialBuilder);
            Material material = Validate.notNull(Material.getMaterial(materialName.toUpperCase()), "Couldn't identify the material '" + materialName + "'");

            ItemStack result = new ItemStack(material);
            ItemMeta meta = result.getItemMeta();

            if (!nameBuilder.isEmpty()) {
                // [...] test_sword [...]
                meta.setDisplayName(MessageUtil.color(nameBuilder.toString()));
            }
            if (!loreBuilder.isEmpty()) {
                // [...] test lore, more than one line; [...]
                meta.setLore(Util.coloredList(loreBuilder.toString().split("<b>")));
            }
            if (!enchantsBuilder.isEmpty()) {
                // [...] sharpness:1,unbreaking:3; [...]
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                String[] splitStrings = enchantsBuilder.toString().split(",");

                for (String enchantString : splitStrings) {
                    String[] split = enchantString.split(":");
                    Validate.check(split.length == 2, "Missing level for enchantment: '" + enchantString + "'");

                    Enchantment enchant = Validate.notNull(EnchantmentFinder.getByName(split[0]), "Couldn't identify the enchantment: '" + split[0] + "'");
                    int level = Util.parseInt(split[1], 0, 1, "Cannot set an level lower than '1' for: " + split[1] + "'");

                    enchantments.put(enchant, level);
                }
                enchantments.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
            }
            if (!modifiersBuilder.isEmpty()) {
                /*
                 keywords:
                 - uuid: random, r
                 - slot: all
                 - operation: 0, 1, 2

                 [...] generic_attack_damage:uuid:name:operation:amount:slot,generic_attack_speed:uuid:name:operation:amount:slot; [...]
                */
                Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
                String[] splitStrings = modifiersBuilder.toString().split(",");

                for (String attributeModifierString : splitStrings) {
                    String[] split = attributeModifierString.split(":");
                    Validate.check(split.length == 6, "Wrong AttributeModifier format, required: 'attribute:uuid:name:operation:amount:slot' but found: '" + attributeModifierString + "'");
                    // strings
                    String at = split[0];
                    String uu = split[1];
                    String na = split[2]; // no parsing needed
                    String op = split[3];
                    String am = split[4];
                    String sl = split[5];
                    // value parsing
                    Attribute attribute = Validate.notNull(AttributeFinder.getByName(at), "Couldn't identify the attribute: '" + at + "'");
                    UUID uuid = Util.uniqueIdFromString(uu);
                    AttributeModifier.Operation operation = Validate.notNull(OperationFinder.getByName(op), "Couldn't identify the operation: '" + op + "'");
                    double amount = Util.parseDouble(am, -1.0, 0.0, "Cannot set an amount lower than '0.0' for: '" + am + "'");
                    EquipmentSlot slot = EquipmentSlotFinder.unbound(sl) ? null : Validate.notNull(EquipmentSlotFinder.getByName(sl), "Couldn't identify the EquipmentSlot: '" + sl + "'");

                    modifiers.put(attribute, new AttributeModifier(uuid, na, amount, operation, slot));
                }
                meta.setAttributeModifiers(modifiers);
            }
            if (!unbreakableBuilder.isEmpty()) {
                String unbreakable = unbreakableBuilder.toString();

                if (unbreakable.equalsIgnoreCase("true")) {
                    meta.setUnbreakable(true);
                } else if (unbreakable.equalsIgnoreCase("false")) {
                    meta.setUnbreakable(false);
                } else {
                    throw new ConfigSerializationError("The setting 'unbreakable' can only be 'true' or 'false'");
                }
            }
            if (!damageBuilder.isEmpty()) {
                String damage = damageBuilder.toString();

                int i = NumberUtil.parseInt(damage);

                ((Damageable) meta).setDamage(i);
            }
            if (!flagsBuilder.isEmpty()) {
                String[] flagStrings = flagsBuilder.toString().split(",");
                ItemFlag[] flags = new ItemFlag[flagStrings.length];

                for (int i = 0; i < flagStrings.length; i++) {
                    ItemFlag flag = null;
                    String current = flagStrings[i];

                    for (ItemFlag value : ItemFlag.values()) {
                        if (value.name().equalsIgnoreCase(current)) {
                            flag = value;
                        }
                    }
                    flags[i] = Validate.notNull(flag, "Couldn't identify ItemFlag '" + current + "'");
                }
                meta.addItemFlags(flags);
            }

            result.setItemMeta(meta);
            return result;
        } catch (ValidationException | DeniedBuilderAccessException e) {
            throw new ConfigSerializationError("An error happened during deserialization of '" + serialized + "' | " + e.getMessage());
        }
    }

    // util

    public static String toString(Map<Enchantment, Integer> map) {
        return toString(map, false);
    }

    public static String toString(Map<Enchantment, Integer> map, boolean brace) {
        Enchantment[] k = map.keySet().toArray(new Enchantment[0]);
        Integer[] v = map.values().toArray(new Integer[0]);
        int iMax = k.length - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(k[i].getName()).append(":").append(v[i].intValue());
            if (i == iMax) {
                return brace ? "{" + b.toString() + "}" : b.toString();
            }
            b.append(",");
        }
    }

    public static String toString(Set<ItemFlag> flags) {
        return toString(flags, false);
    }

    public static String toString(Set<ItemFlag> flags, boolean brace) {
        ItemFlag[] a = flags.toArray(new ItemFlag[0]);
        String[] sa = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            sa[i] = a[i].name();
        }
        return Util.toString(sa, brace);
    }

    public static String toString(Multimap<Attribute, AttributeModifier> modifiers) {
        return toString(modifiers, false);
    }

    public static String toString(Multimap<Attribute, AttributeModifier> modifiers, boolean brace) {
        StringBuilder b = new StringBuilder();

        Map<Attribute, Collection<AttributeModifier>> map = modifiers.asMap();
        int max = modifiers.size();

        int i = 0;
        for (Attribute a : map.keySet()) {
            for (AttributeModifier m : map.get(a)) {
                b.append(a.name()).append(":");
                b.append(m.getUniqueId().toString()).append(":");
                b.append(m.getName()).append(":");
                b.append(m.getOperation().name()).append(":");
                b.append(m.getAmount()).append(":");
                b.append(m.getSlot());
                if (++i != max) {
                    b.append(",");
                } else {
                    b.append(";");
                }
            }
        }
        return brace ? "{" + b.toString() + "}" : b.toString();
    }

    public enum Key implements StringKeyed {

        MATERIAL,
        NAME,
        LORE,
        ENCHANTS,
        MODIFIERS,
        UNBREAKABLE,
        DAMAGE,
        FLAGS,
        SKILL;

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
