package de.fyreum.customitemsxl.skill.condition;

import org.bukkit.entity.Entity;

public class ChanceCondition implements TargetCondition {

    private final double chance;

    public ChanceCondition(double chance) {
        this.chance = chance;
    }

    @Override
    public boolean fullFilled(Entity attacker, Entity target) {
        return Math.random() < chance;
    }
}
