package de.fyreum.customitemsxl.skill.condition;

import org.bukkit.entity.Entity;

public interface TargetCondition {

    TargetCondition FACTION = new FactionCondition();

    boolean fullFilled(Entity attacker, Entity target);

}
