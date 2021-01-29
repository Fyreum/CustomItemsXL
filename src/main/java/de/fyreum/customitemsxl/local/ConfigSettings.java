package de.fyreum.customitemsxl.local;

import de.erethon.commons.config.DREConfig;
import de.fyreum.customitemsxl.logger.DebugMode;
import de.fyreum.customitemsxl.util.Util;

import java.io.File;

public class ConfigSettings extends DREConfig {

    public static final int CONFIG_VERSION = 1;

    private String language = "german";
    private DebugMode debug = DebugMode.OFF;

    public ConfigSettings(File file) {
        super(file, CONFIG_VERSION);
        if (initialize) {
            initialize();
        }
        load();
    }

    @Override
    public void initialize() {
        if (!config.contains("language")) {
            config.set("language", language);
        }
        if (!config.contains("debug")) {
            config.set("debug", debug.getValue());
        }
        save();
    }

    @Override
    public void load() {
        language = Util.notNullValue(config.getString("language"), language);
        debug = Util.notNullValue(DebugMode.getByString(config.getString("debug", getDebugMode().name())), DebugMode.OFF);
    }

    public DebugMode getDebugMode() {
        return debug;
    }

    public String getLanguage() {
        return language;
    }
}
