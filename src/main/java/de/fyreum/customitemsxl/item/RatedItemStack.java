package de.fyreum.customitemsxl.item;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.rating.Rated;
import org.bukkit.inventory.ItemStack;

public class RatedItemStack extends ItemStack implements Rated {

    private final String id;
    private final Rarity rarity;

    public RatedItemStack(String serialized, String id, Rarity rarity) {
        this(CustomItemsXL.inst().getItemFactory().deserialize(serialized), id, rarity);
    }

    public RatedItemStack(ItemStack item, String id, Rarity rarity) {
        super(item);
        this.id = id;
        this.rarity = rarity;
    }

    public String getId() {
        return id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return CustomItemsXL.inst().getItemFactory().serialize(this);
    }
}
