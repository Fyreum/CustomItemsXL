package de.fyreum.customitemsxl.skill.projectile;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SizedFireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;

import java.util.UUID;

public interface TaggedProjectile {

    TaggedProjectile ARROW = new ProjectileWrapper<>("arrow", UUID.randomUUID(), Arrow.class);
    TaggedProjectile EGG = new ProjectileWrapper<>("egg", UUID.randomUUID(), Egg.class);
    TaggedProjectile ENDER_PEARL = new ProjectileWrapper<>("ender_pearl", UUID.randomUUID(), EnderPearl.class);
    TaggedProjectile FIREBALL = new ProjectileWrapper<>("fireball", UUID.randomUUID(), Fireball.class);
    TaggedProjectile FIREWORK = new ProjectileWrapper<>("firework", UUID.randomUUID(), Firework.class);
    TaggedProjectile FISH_HOOK = new ProjectileWrapper<>("fish_hook", UUID.randomUUID(), FishHook.class);
    TaggedProjectile LARGE_FIREBALL = new ProjectileWrapper<>("large_fireball", UUID.randomUUID(), LargeFireball.class);
    TaggedProjectile LINGERING_POTION = new ProjectileWrapper<>("lingering_potion", UUID.randomUUID(), LingeringPotion.class);
    TaggedProjectile LLAMA_SPIT = new ProjectileWrapper<>("llama_spit", UUID.randomUUID(), LlamaSpit.class);
    TaggedProjectile SHULKER_BULLET = new ProjectileWrapper<>("shulker_bullet", UUID.randomUUID(), ShulkerBullet.class);
    TaggedProjectile SIZED_FIREBALL = new ProjectileWrapper<>("sized_fireball", UUID.randomUUID(), SizedFireball.class);
    TaggedProjectile SMALL_FIREBALL = new ProjectileWrapper<>("small_fireball", UUID.randomUUID(), SmallFireball.class);
    TaggedProjectile SNOWBALL = new ProjectileWrapper<>("snowball", UUID.randomUUID(), Snowball.class);
    TaggedProjectile SPECTRAL_ARROW = new ProjectileWrapper<>("spectral_arrow", UUID.randomUUID(), SpectralArrow.class);
    TaggedProjectile SPLASH_POTION = new ProjectileWrapper<>("splash_potion", UUID.randomUUID(), SplashPotion.class);
    TaggedProjectile THROWN_EXP_BOTTLE = new ProjectileWrapper<>("thrown_exp_bottle", UUID.randomUUID(), ThrownExpBottle.class);
    TaggedProjectile TIPPED_ARROW = new ProjectileWrapper<>("tipped_arrow", UUID.randomUUID(), TippedArrow.class);
    TaggedProjectile TRIDENT = new ProjectileWrapper<>("trident", UUID.randomUUID(), Trident.class);
    TaggedProjectile WITHER_SKULL = new ProjectileWrapper<>("wither_skull", UUID.randomUUID(), WitherSkull.class);

    // methods

    String getName();

    UUID getUniqueId();

    Class<?> getProjectile();

}
