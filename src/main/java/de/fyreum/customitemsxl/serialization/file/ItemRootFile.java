package de.fyreum.customitemsxl.serialization.file;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.serialization.roots.ItemRoot;
import de.fyreum.customitemsxl.serialization.roots.Root;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ItemRootFile extends RootFile<Root<ItemStack>> {

    public ItemRootFile(File file) {
        super(file);
        CustomItemsXL.LOGGER.debug(DebugMode.LOW, "Loading '" + file.getPath());
        contents.forEach((key, object) -> {
            if (!(object instanceof String)) {
                CustomItemsXL.LOGGER.warn("Found non string object at '" + key + "', continuing by ignoring it");
                return;
            }
            try {
                Root<ItemStack> root = new ItemRoot(key, (String) object);
                this.roots.add(root);
            } catch (ConfigSerializationError e) {
                CustomItemsXL.LOGGER.error(file.getPath(), key, e.getMessage());
            }
        });
    }
}
