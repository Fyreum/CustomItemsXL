package de.fyreum.customitemsxl.skill;

import org.bukkit.entity.Player;

public class PassiveDamageSkill implements PassiveSkill {

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public AbilityFeedback cast(Player player) {
        return null;
    }
}
