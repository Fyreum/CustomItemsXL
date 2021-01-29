package de.fyreum.customitemsxl.skill.condition;

import de.erethon.factionsxl.player.FPlayer;
import de.erethon.factionsxl.player.FPlayerCache;
import de.fyreum.customitemsxl.CustomItemsXL;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FactionCondition implements TargetCondition {

    @Override
    public boolean fullFilled(Entity attacker, Entity target) {
        if (CustomItemsXL.inst().getFactionsXL() == null) {
            return true;
        }
        if (!(attacker instanceof Player)) {
            return true;
        }
        FPlayerCache fPlayerCache = CustomItemsXL.inst().getFactionsXL().getFPlayerCache();
        FPlayer fPlayer = fPlayerCache.getByPlayer((Player) attacker);

        if (!fPlayer.hasFaction() || !(target instanceof Player)) {
            return true;
        }

        Player tPlayer = (Player) target;
        FPlayer fTarget = fPlayerCache.getByPlayer(tPlayer);

        if (!fTarget.hasFaction()) {
            return true;
        }
        return !fPlayer.getFaction().getRelation(fTarget).isProtected();
    }
}
