package de.fyreum.customitemsxl.local;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.config.DREConfig;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.filter.FilteredItemStack;
import de.fyreum.customitemsxl.filter.FilteredMaterial;
import de.fyreum.customitemsxl.filter.FilteredSubject;
import de.fyreum.customitemsxl.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterSettings extends DREConfig {

    public static final int CONFIG_VERSION = 1;

    private final Map<NamespacedKey, Integer> enchantmentValues = new HashMap<>();
    private final List<NamespacedKey> disabledEnchants = new ArrayList<>();
    private final List<FilteredSubject> filteredSubjects = new ArrayList<>();
    private boolean disableVillagers = false;
    private double reducedDamageMultiplier = 0.5;
    private String noDamageItemLore = "&5Werkzeug";
    private String reducedDamageLore = "&7Reduzierter Spielerschaden";
    private List<String> affectedWorlds = new ArrayList<>();
    private List<String> noDamageTypes = new ArrayList<>();
    private List<String> ignoreDamageFilterLore = new ArrayList<>();

    public FilterSettings(File file) {
        super(file, CONFIG_VERSION);
        if (initialize) {
            initialize();
        }
        load();
    }

    @Override
    public void initialize() {
        if (!config.contains("disableVillagers")) {
            config.set("disableVillagers", disableVillagers);
        }
        if (!config.contains("reducedDamageMultiplier")) {
            config.set("reducedDamageMultiplier", reducedDamageMultiplier);
        }
        if (!config.contains("noDamageItemLore")) {
            config.set("noDamageItemLore", noDamageItemLore);
        }
        if (!config.contains("reducedDamageLore")) {
            config.set("reducedDamageLore", reducedDamageLore);
        }
        if (!config.contains("affectedWorlds")) {
            config.set("affectedWorlds", affectedWorlds);
        }
        save();
    }

    @Override
    public void load() {
        disableVillagers = config.getBoolean("disableVillagers") ;
        reducedDamageMultiplier = config.getDouble("reducedDamageMultiplier");
        noDamageItemLore = MessageUtil.color(Util.notNullValue(config.getString("noDamageItemLore"), noDamageItemLore));
        reducedDamageLore = MessageUtil.color(Util.notNullValue(config.getString("reducedDamageLore"), reducedDamageLore));
        affectedWorlds = Util.notNullValue(config.getStringList("affectedWorlds"), affectedWorlds);
        noDamageTypes = config.getStringList("noDamageTypes");
        ignoreDamageFilterLore = Util.color(config.getStringList("ignoreDamageFilterLore"));

        for (Enchantment enchantment : Enchantment.values()) {
            if (config.get("enchantments." + enchantment.getName()) == null) {
                // sets the maximum level of the given enchantment to the maximum Integer value.
                enchantmentValues.put(enchantment.getKey(), Integer.MAX_VALUE);
                continue;
            }
            if (config.getInt("enchantments." + enchantment.getName()) <= 0) {
                // adds the enchant to the list of enchantments that will be removed.
                disabledEnchants.add(enchantment.getKey());
                continue;
            }
            // sets the maximum level of the given enchantment to the loaded level.
            enchantmentValues.put(enchantment.getKey(), config.getInt("enchantments." + enchantment.getName()));
        }
        loadItems();
    }

    private void loadItems() {
        ConfigurationSection section = config.getConfigurationSection("filtered");
        if (section == null) {
            return;
        }
        section.getValues(false).forEach((s, o) -> {
            ItemStack item = CustomItemsXL.inst().getItemStack(s);
            ItemStack itemTo = CustomItemsXL.inst().getItemStack((String) o);

            if (item == null) {
                Material material = Material.getMaterial(s.toUpperCase());
                if (material == null) {
                    CustomItemsXL.LOGGER.error("filter.yml", "Couldn't identify key '" + s + "'");
                    return;
                }
                if (itemTo == null) {
                    Material materialTo = Material.getMaterial(((String) o).toUpperCase());
                    filteredSubjects.add(new FilteredMaterial(material, materialTo));
                } else {
                    filteredSubjects.add(new FilteredMaterial(material, itemTo));
                }
            } else {
                if (itemTo == null) {
                    Material materialTo = Material.getMaterial(((String) o).toUpperCase());
                    filteredSubjects.add(new FilteredItemStack(item, materialTo));
                } else {
                    filteredSubjects.add(new FilteredItemStack(item, itemTo));
                }
            }
        });
    }

    public Map<NamespacedKey, Integer> getEnchantmentValues() {
        return enchantmentValues;
    }

    public List<NamespacedKey> getDisabledEnchants() {
        return disabledEnchants;
    }

    public List<FilteredSubject> getFilteredSubjects() {
        return filteredSubjects;
    }

    public boolean isDisableVillagers() {
        return disableVillagers;
    }

    public double getReducedDamageMultiplier() {
        return reducedDamageMultiplier;
    }

    public String getNoDamageItemLore() {
        return noDamageItemLore;
    }

    public String getReducedDamageLore() {
        return reducedDamageLore;
    }

    public List<String> getAffectedWorlds() {
        return affectedWorlds;
    }

    public List<String> getNoDamageTypes() {
        return noDamageTypes;
    }

    public List<String> getIgnoreDamageFilterLore() {
        return ignoreDamageFilterLore;
    }
}
