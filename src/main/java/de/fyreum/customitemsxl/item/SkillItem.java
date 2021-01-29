package de.fyreum.customitemsxl.item;

import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.skill.AbilityFeedback;
import de.fyreum.customitemsxl.skill.Skill;
import de.fyreum.customitemsxl.logger.Debuggable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import java.util.UUID;

public class SkillItem implements Debuggable {

    private final Skill ability;

    public SkillItem(String name, UUID uuid, Recipe recipe, Skill ability) {
        this.ability = ability;
    }

    public Skill getAbility() {
        return ability;
    }

    public AbilityFeedback cast(Player player) {
        try {
            AbilityFeedback feedback = getAbility().cast(player);
            if (feedback.equals(AbilityFeedback.SUCCESS)) {
                CustomItemsXL.inst().getCooldownManager().activateCooldown(player.getUniqueId(), ability);
            }
            return feedback;
        } catch (Exception e) {
            e.printStackTrace();
            return AbilityFeedback.EXCEPTION;
        }
    }

    @Override
    public String getDebugInfo() {
        return null;
    }
}
