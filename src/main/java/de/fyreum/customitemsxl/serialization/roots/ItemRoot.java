package de.fyreum.customitemsxl.serialization.roots;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import org.bukkit.inventory.ItemStack;

public class ItemRoot extends RootBase<ItemStack> {

    private String serialized;
    private ItemStack deserialized;

    public ItemRoot(String id, String serialized) throws ConfigSerializationError {
        super(id);
        this.serialized = serialized;
        this.deserialized = CustomItemsXL.inst().getItemFactory().deserialize(serialized);
    }

    public ItemRoot(String id, ItemStack deserialized) {
        super(id);
        this.serialized = CustomItemsXL.inst().getItemFactory().serialize(deserialized);
        this.deserialized = deserialized;
    }

    @Override
    public String getSerialized() {
        return serialized;
    }

    @Override
    public String setSerialized(String s) {
        return this.serialized = s;
    }

    @Override
    public ItemStack getDeserialized() {
        return deserialized;
    }

    @Override
    public void setDeserialized(ItemStack deserialized) {
        this.deserialized = deserialized;
    }
}
