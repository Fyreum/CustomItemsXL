package de.fyreum.customitemsxl.skill;

import de.fyreum.customitemsxl.skill.target.TargetFinder;
import org.bukkit.entity.Player;

public class TargetingDamageSkill implements TargetingSkill {

    private final long cooldown;
    private double damage;
    private TargetFinder targetFinder;

    public TargetingDamageSkill(long cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public AbilityFeedback cast(Player player) {
        return null;
    }

    @Override
    public TargetFinder getTargetFinder() {
        return targetFinder;
    }
}
