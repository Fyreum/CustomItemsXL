package de.fyreum.customitemsxl.events;

import de.fyreum.customitemsxl.item.SkillItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ItemAbilityCastEvent extends Event implements Cancellable {

    private final Player caster;
    private final SkillItem abilityItem;
    private final HandlerList handlerList;
    private boolean cancelled;

    public ItemAbilityCastEvent(Player caster, SkillItem abilityItem) {
        this.caster = caster;
        this.abilityItem = abilityItem;
        this.handlerList = new HandlerList();
    }

    public Player getCaster() {
        return caster;
    }

    public SkillItem getAbilityItem() {
        return abilityItem;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
