package de.fyreum.customitemsxl.serialization.file;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.serialization.roots.Root;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RootFile<R extends Root<?>> {

    private final File file;

    protected FileConfiguration config;
    protected Map<String, Object> contents;
    protected Set<R> roots;
    private final Set<R> toSave;
    private final Set<String> toDelete;

    public RootFile(File file) {
        this.file = file;
        load();
        this.contents = config.getValues(false);
        this.roots = new HashSet<>();
        this.toSave = new HashSet<>();
        this.toDelete = new HashSet<>();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load() {
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                this.file.createNewFile();
                config = YamlConfiguration.loadConfiguration(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = new YamlConfiguration();
            try {
                config.load(this.file);
            } catch (InvalidConfigurationException | IOException i) {
                CustomItemsXL.LOGGER.warn("The configuration file " + file.getPath() + " seems to be erroneous.");
                CustomItemsXL.LOGGER.warn("This is not a bug. Try to fix the configuration file with http://yamllint.com.");
                String path = file.getPath();
                this.file.renameTo(new File(path + "_backup_" + System.currentTimeMillis()));
                try {
                    this.file.createNewFile();
                    config.load(file);
                } catch (InvalidConfigurationException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException var2) {
            CustomItemsXL.LOGGER.error("Could not save " + this.file.getPath() + "...");
        }
    }

    public void reload() {
        load();
        try {
            for (String key: toDelete) {
                this.config.set(key, null);
            }
            for (R root : toSave) {
                this.config.set(root.getId(), root.getSerialized());
            }
            this.config.save(this.file);
        } catch (IOException var2) {
            CustomItemsXL.LOGGER.error("Could not save " + this.file.getPath() + "...");
        }
    }

    public void addRoot(R r) {
        this.roots.add(r);
        this.toSave.add(r);
    }

    public void remove(String key) {
        roots.removeIf(root -> root.getId().equalsIgnoreCase(key));
        toDelete.add(key);
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Set<R> getRoots() {
        return roots;
    }
}
