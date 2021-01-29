package de.fyreum.customitemsxl.cooldowns;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.skill.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final CustomItemsXL plugin;
    private final Map<UUID, PlayerCooldowns> allCooldowns;

    public CooldownManager(CustomItemsXL plugin) {
        this.plugin = plugin;
        this.allCooldowns = new HashMap<>();
    }

    public void activateCooldown(UUID uuid, Skill ability) {
        getPlayerCooldowns(uuid).activateCooldown(ability);
    }

    public boolean hasCooldowns(UUID uuid) {
        return getPlayerCooldowns(uuid).hasCooldowns();
    }

    public boolean hasCooldown(UUID uuid, Skill ability) {
        return getPlayerCooldowns(uuid).hasCooldown(ability);
    }

    public PlayerCooldowns getPlayerCooldowns(UUID uuid) {
        if (allCooldowns.get(uuid) == null) {
            allCooldowns.put(uuid, new PlayerCooldowns());
        }
        return allCooldowns.get(uuid);
    }

    public Map<UUID, PlayerCooldowns> getAllCooldowns() {
        return allCooldowns;
    }
}
