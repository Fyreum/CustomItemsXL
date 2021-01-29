package de.fyreum.customitemsxl.cooldowns;

import de.fyreum.customitemsxl.skill.Skill;

import java.util.HashMap;
import java.util.Map;

public class PlayerCooldowns {

    private final Map<Skill, Long> cooldowns;

    public PlayerCooldowns(Map<Skill, Long> cooldowns) {
        this.cooldowns = cooldowns;
    }

    public PlayerCooldowns() {
        this.cooldowns = new HashMap<>();
    }

    public void activateCooldown(Skill ability) {
        this.cooldowns.put(ability, System.currentTimeMillis() + ability.getCooldown()/20*1000);
    }

    public boolean hasCooldowns() {
        for (Skill ability : cooldowns.keySet()) {
            if (hasCooldown(ability)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCooldown(Skill ability) {
        if (cooldowns.get(ability) == null) {
            return false;
        }
        if (System.currentTimeMillis() > cooldowns.get(ability)) {
            cooldowns.remove(ability);
            return false;
        } else {
            return true;
        }
    }

}
