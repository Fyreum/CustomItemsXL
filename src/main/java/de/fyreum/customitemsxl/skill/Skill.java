package de.fyreum.customitemsxl.skill;

import de.fyreum.customitemsxl.cooldowns.CooldownHolder;
import org.bukkit.entity.Player;

public interface Skill extends CooldownHolder {

    AbilityFeedback cast(Player player);

}
