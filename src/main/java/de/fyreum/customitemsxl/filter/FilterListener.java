package de.fyreum.customitemsxl.filter;

import de.fyreum.customitemsxl.CustomItemsXL;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class FilterListener implements Listener {

    CustomItemsXL plugin = CustomItemsXL.inst();

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEntityEvent event) {
        // cancels the interaction between Player and Villager
        if (event.getRightClicked() instanceof Villager && plugin.getFilterSettings().isDisableVillagers()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        HumanEntity damager;
        // sets the player who did the damage in this event.
        if (event.getDamager() instanceof Player) {
            damager = (HumanEntity) event.getDamager();
        } else {
            if (!(event.getDamager() instanceof Projectile) || !(((Projectile) event.getDamager()).getShooter() instanceof HumanEntity)) {
                return;
            }
            // sets the damager to the shooter if the event.getDamager() is an arrow.
            damager = (HumanEntity) ((Projectile) event.getDamager()).getShooter();
        }
        if (damager == null) {
            return;
        }
        // reduces the damage of certain weapons
        ItemStack mainHand = damager.getInventory().getItemInMainHand();
        ItemMeta meta = mainHand.getItemMeta();
        if (meta != null && meta.getLore() != null && meta.getLore().contains(plugin.getFilterSettings().getReducedDamageLore())) {
            if (event.getEntity() instanceof Player) {
                event.setDamage(event.getDamage()*plugin.getFilterSettings().getReducedDamageMultiplier());
            }
        }
        // calls the patchItem() method for both hands of the player.
        patchItem(damager, mainHand);
        patchItem(damager, damager.getInventory().getItemInOffHand());
        // continues if the damaged entity is a player.
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        HumanEntity damaged = (HumanEntity) event.getEntity();
        // calls the patchItem() method for the armor content.
        for (ItemStack armor : damaged.getInventory().getArmorContents()) {
            patchItem(damaged, armor);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        // calls the patchItem() method for both of the player hands.
        patchItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        patchItem(event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand());
    }
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        // patches all fished items.
        if (event.getCaught() instanceof Item) {
            patchItem(event.getPlayer(), ((Item) event.getCaught()).getItemStack());
        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        patchItem(event.getWhoClicked(), event.getCurrentItem());
    }
    public void patchItem(HumanEntity entity, ItemStack item) {
        if (!plugin.getFilterSettings().getAffectedWorlds().contains(entity.getWorld().getName())) {
            return;
        }
        filterEnchants(item);
        filterMaterial(item);
    }

    public void filterEnchants(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasEnchants()) {
            return;
        }
        List<NamespacedKey> disabledEnchants = plugin.getFilterSettings().getDisabledEnchants();
        Map<NamespacedKey, Integer> values = plugin.getFilterSettings().getEnchantmentValues();

        for(Map.Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet()) {
            // removes the enchantment, if its on the disabled list.
            if (disabledEnchants.contains(enchant.getKey().getKey())) {
                item.removeEnchantment(enchant.getKey());
                continue;
            }
            // downgrades the enchant, if the level is higher then the maximum value.
            if (enchant.getValue() > values.get(enchant.getKey().getKey())) {
                item.addUnsafeEnchantment(enchant.getKey(), values.get(enchant.getKey().getKey()));
            }
        }
    }

    public void filterMaterial(ItemStack item) {

    }

}
